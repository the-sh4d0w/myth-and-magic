package myth_and_magic.block.entity;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.InfusionTableBlock;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.recipe.InfusionTableRecipe;
import myth_and_magic.screen.InfusionTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class InfusionTableBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory, SidedInventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    protected final PropertyDelegate propertyDelegate;
    private int levelCost = 0;
    private Identifier currentRecipeId;

    public InfusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(MythAndMagicBlocks.INFUSION_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return InfusionTableBlockEntity.this.levelCost;
            }

            @Override
            public void set(int index, int value) {
                InfusionTableBlockEntity.this.levelCost = value;
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.myth_and_magic.infusion_table");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new InfusionTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    @Override
    public int size() {
        return getItems().size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = getStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }
        markDirty();
    }

    @Override
    public void clear() {
        getItems().clear();
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) {
            return;
        }
        int power = 0;
        for (BlockPos blockPos : InfusionTableBlock.POWER_PROVIDER_OFFSETS) {
            power += InfusionTableBlock.getPower(world, pos, blockPos);
        }
        if (power >= InfusionTableBlock.MIN_POWER) {
            if (this.hasRecipe()) {
                this.craftItem();
            } else if (this.canUpgrade()) {
                this.upgradeItem();
            } else {
                removeStack(OUTPUT_SLOT);
            }
        } else {
            removeStack(OUTPUT_SLOT);
        }
    }

    public void triggerCriterion(ServerPlayerEntity player) {
        if (this.currentRecipeId != null) {
            MythAndMagic.RECIPE_INFUSION.trigger(player, this.currentRecipeId);
        } else {
            MythAndMagic.ENCHANTMENT_UPGRADED.trigger(player);
        }
    }

    private boolean canUpgrade() {
        ItemStack item = this.getStack(INPUT_SLOT);
        return getUpgradeCost(item) != 0;
    }

    private void upgradeItem() {
        this.currentRecipeId = null;
        ItemStack item = this.getStack(INPUT_SLOT).copy();
        int cost = 0;
        Map<Enchantment, Integer> map = EnchantmentHelper.get(item);
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            if (entry.getValue() < entry.getKey().getMaxLevel()) {
                cost += 5 + entry.getValue();
                entry.setValue(entry.getValue() + 1);
            }
        }
        EnchantmentHelper.set(map, item);
        this.propertyDelegate.set(0, cost);
        this.setStack(OUTPUT_SLOT, item);
    }

    private int getUpgradeCost(ItemStack item) {
        int cost = 0;
        Map<Enchantment, Integer> map = EnchantmentHelper.get(item);
        for (Map.Entry<Enchantment, Integer> entry : map.entrySet()) {
            if (entry.getValue() < entry.getKey().getMaxLevel()) {
                cost += 5 + entry.getValue();
            }
        }
        return cost;
    }

    private void craftItem() {
        Optional<InfusionTableRecipe> match = getCurrentRecipe();
        ItemStack result = match.get().getOutput(null);
        this.currentRecipeId = match.get().getId();
        this.propertyDelegate.set(0, match.get().getLevelCost());
        this.setStack(OUTPUT_SLOT, new ItemStack(result.getItem(), result.getCount()));
    }

    public void removeInput(int count) {
        this.removeStack(INPUT_SLOT, count);
    }

    private boolean hasRecipe() {
        return getCurrentRecipe().isPresent();
    }

    private Optional<InfusionTableRecipe> getCurrentRecipe() {
        SimpleInventory inv = new SimpleInventory(this.size());
        for (int i = 0; i < this.size(); i++) {
            inv.setStack(i, this.getStack(i));
        }
        return getWorld().getRecipeManager().getFirstMatch(InfusionTableRecipe.Type.INSTANCE, inv, getWorld());
    }
}