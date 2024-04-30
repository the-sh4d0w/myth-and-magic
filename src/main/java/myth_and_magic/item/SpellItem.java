package myth_and_magic.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.LecternBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.HashMap;

public class SpellItem extends Item {
    private final Type type;

    public SpellItem(Settings settings, Type type) {
        super(settings.maxDamage(type.damage));
        this.type = type;
    }

    @Override
    public boolean isDamageable() {
        // it is actually damageable, but this prevents it from being mended
        return false;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        // this is complicated and annoying
        super.inventoryTick(stack, world, entity, slot, selected);
        if (!stack.getOrCreateNbt().contains("pages")) {
            NbtList list = new NbtList();
            String text = "{\"text\":\"%s\",\"color\":\"dark_purple\",\"italic\":true}".formatted(this.type.text);
            list.add(NbtString.of(text));
            stack.setSubNbt("title", NbtString.of("Lorem Ipsum"));
            stack.setSubNbt("pages", list);
            stack.setSubNbt("author", NbtString.of(((PlayerEntity) entity).getGameProfile().getName()));
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (blockState.isOf(Blocks.LECTERN)) {
            return LecternBlock.putBookIfAbsent(context.getPlayer(), world, blockPos, blockState, context.getStack()) ? ActionResult.success(world.isClient) : ActionResult.PASS;
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (player.getAbilities().creativeMode || player.experienceLevel >= this.type.levelCost) {
            if (world.isClient()) {
                return TypedActionResult.success(itemStack);
            }
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            switch (this.type) {
                case HOME -> {
                    BlockPos spawnPoint = serverPlayer.getSpawnPointPosition();
                    if (spawnPoint == null) {
                        spawnPoint = world.getSpawnPos();
                    }
                    serverPlayer.teleport(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
                    serverPlayer.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1, 1);
                }
                case GROW -> {
                    BlockPos playerPos = player.getBlockPos();
                    BlockPos pos;
                    Fertilizable fertilizable;
                    for (int i = -2; i < 3; i++) {
                        for (int j = -2; j < 3; j++) {
                            pos = playerPos.add(i, 0, j);
                            while (world.getBlockState(pos).getBlock() instanceof Fertilizable && (fertilizable = (Fertilizable) world.getBlockState(pos).getBlock()).isFertilizable(world, pos, world.getBlockState(pos), world.isClient)) {
                                if (world instanceof ServerWorld) {
                                    if (fertilizable.canGrow(world, world.random, pos, world.getBlockState(pos))) {
                                        fertilizable.grow((ServerWorld) world, world.random, pos, world.getBlockState(pos));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!player.getAbilities().creativeMode) {
                serverPlayer.addExperienceLevels(-this.type.levelCost);
                if (itemStack.damage(1, Random.create(), serverPlayer)) {
                    itemStack.decrement(1);
                }
            }
            // prevent spamming the grow spell to fix potential crash
            player.getItemCooldownManager().set(this, 20);
            return TypedActionResult.success(itemStack);
        } else {
            player.sendMessage(Text.translatable("item.myth_and_magic.spell.failed"), true);
            return TypedActionResult.fail(itemStack);
        }
    }

    public enum Type {
        HOME(10, 1, "Tincidunt lobortis feugiat vivamus at augue eget arcu dictum varius."),
        GROW(2, 10, "Nam liber tempor cum soluta nobis eleifend option congue nihil imperdiet doming id quod mazim placerat facer possim assum."),
        TELEPORT(1, 5, "?");

        final int levelCost;
        final int damage;
        final String text;

        Type(int levelCost, int damage, String text) {
            this.levelCost = levelCost;
            this.damage = damage;
            this.text = text;
        }
    }
}