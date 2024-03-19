package myth_and_magic.item;

import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SpellItem extends Item {
    private final Type type;

    public SpellItem(Settings settings, Type type) {
        super(settings.maxDamage(type.damage));
        this.type = type;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
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
            return TypedActionResult.success(itemStack);
        } else {
            player.sendMessage(Text.translatable("item.myth_and_magic.spell.failed"), true);
            return TypedActionResult.fail(itemStack);
        }
    }

    public enum Type {
        HOME(10, 1),
        // teleport to where?
        GROW(2, 10),
        TELEPORT(1, 5);

        final int levelCost;
        final int damage;

        Type(int levelCost, int damage) {
            this.levelCost = levelCost;
            this.damage = damage;
        }
    }
}