package myth_and_magic.screen;

import myth_and_magic.block.entity.InfusionTableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class InfusionTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final InfusionTableBlockEntity blockEntity;

    public InfusionTableScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(1));
    }

    public InfusionTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockEntity blockEntity, PropertyDelegate propertyDelegate) {
        super(MythAndMagicScreenHandlers.INFUSION_TABLE_SCREEN_HANDLER, syncId);
        checkSize((Inventory) blockEntity, 2);
        this.inventory = (Inventory) blockEntity;
        playerInventory.onOpen(playerInventory.player);
        this.propertyDelegate = propertyDelegate;
        this.blockEntity = (InfusionTableBlockEntity) blockEntity;
        this.addSlot(new Slot(inventory, 0, 44, 35));
        this.addSlot(new OutputSlot(inventory, 1, 116, 35, this, this.blockEntity));
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
        addProperties(propertyDelegate);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot == 0) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (invSlot == 1) {
                int amount;
                if (!player.getAbilities().creativeMode) {
                    amount = Math.min(player.experienceLevel / this.getLevelCost(), this.getSlot(0).getStack().getCount());
                    player.addExperienceLevels(-this.getLevelCost() * amount);
                } else {
                    amount = this.getSlot(0).getStack().getCount();
                }
                this.blockEntity.removeInput(amount);
                originalStack.setCount(amount);
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, 1, false)) {
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getLevelCost() {
        return this.propertyDelegate.get(0);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        player.getInventory().offerOrDrop(this.blockEntity.getStack(0));
    }

    static class OutputSlot extends Slot {
        private final InfusionTableScreenHandler screenHandler;
        private final InfusionTableBlockEntity blockEntity;

        public OutputSlot(Inventory inventory, int index, int x, int y, InfusionTableScreenHandler screenHandler, InfusionTableBlockEntity blockEntity) {
            super(inventory, index, x, y);
            this.screenHandler = screenHandler;
            this.blockEntity = blockEntity;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            if (!player.getWorld().isClient()) {
                this.blockEntity.triggerCriterion((ServerPlayerEntity) player);
            }
            if (!player.getAbilities().creativeMode) {
                player.addExperienceLevels(-this.screenHandler.getLevelCost());
            }
            this.blockEntity.removeInput(1);
            super.onTakeItem(player, stack);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public boolean canTakeItems(PlayerEntity player) {
            return player.getAbilities().creativeMode || player.experienceLevel >= this.screenHandler.getLevelCost();
        }
    }
}