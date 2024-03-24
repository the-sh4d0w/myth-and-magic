package myth_and_magic.item;

import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class DiceOfDestinyItem extends Item {
    public DiceOfDestinyItem(Settings settings) {
        super(settings.maxCount(17));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (world.isClient()) {
            return TypedActionResult.success(itemStack);
        }
        PlayerData playerData = StateSaverAndLoader.getPlayerState(player);
        switch (player.getRandom().nextInt(25)) {
            case 0, 1, 2 -> {
                // gives the player 10 diamonds
                ItemStack stack = Items.DIAMOND.getDefaultStack();
                stack.setCount(10);
                player.getInventory().insertStack(stack);
            }
            case 3, 4, 5, 6 -> {
                // fills inventory with light gray glass panes
                PlayerInventory inventory = player.getInventory();
                for (int i = 9; i < 36; i++) {
                    if (inventory.main.get(i).isEmpty()) {
                        inventory.insertStack(i, Items.LIGHT_GRAY_STAINED_GLASS_PANE.getDefaultStack());
                    }
                }
            }
            case 7, 8, 9, 10, 11 -> {
                // adds two hearts until next death
                EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(attribute.getValue() + 3 * 2);
            }
            case 12 -> {
                // gives the player one netherite ingot
                player.getInventory().insertStack(Items.NETHERITE_INGOT.getDefaultStack());
            }
            case 13, 14, 15, 16 -> {
                // removes on heart until next death
                EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                attribute.setBaseValue(attribute.getValue() + 2);
            }
            case 17, 18, 19 -> {
                // teleport player to random location
                double d = player.getX();
                double e = player.getY();
                double f = player.getZ();
                for (int i = 0; i < 16; ++i) {
                    double g = player.getX() + (player.getRandom().nextDouble() - 0.5) * 25;
                    double h = MathHelper.clamp(player.getY() + (double) (player.getRandom().nextInt(16) - 8),
                            world.getBottomY(), (world.getBottomY() + ((ServerWorld) world).getLogicalHeight() - 1));
                    double j = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 31;
                    if (player.hasVehicle()) {
                        player.stopRiding();
                    }
                    Vec3d vec3d = player.getPos();
                    if (!player.teleport(g, h, j, true)) {
                        continue;
                    }
                    world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(player));
                    SoundEvent soundEvent = SoundEvents.ENTITY_ENDERMAN_TELEPORT;
                    world.playSound(null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    player.playSound(soundEvent, 1.0f, 1.0f);
                    break;
                }
            }
            case 20, 21 -> {
                // move player upwards
                player.addVelocity(0, 3, 0);
                player.velocityModified = true;
            }
            case 22, 23, 24 -> {
                // spawn some zombies
                for (int i = 0; i < player.getRandom().nextInt(5); i++) {
                    EntityType.ZOMBIE.spawn((ServerWorld) world, player.getBlockPos().add(i, 1, i * 2 + 1), SpawnReason.MOB_SUMMONED);
                }
            }
        }
        ((ServerWorld) world).spawnParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY() + 1, player.getZ(),
                3, 0, 0, 0, 0.5);
        ((ServerWorld) world).spawnParticles(ParticleTypes.WAX_ON, player.getX(), player.getY() + 1, player.getZ(),
                10, 0, 0, 0, 3);
        ((ServerWorld) world).spawnParticles(ParticleTypes.WAX_OFF, player.getX(), player.getY() + 1, player.getZ(),
                3, 0, 0, 0, 3);
        itemStack.decrement(1);
        if (itemStack.getCount() == 0 && !player.getAbilities().creativeMode) {
            player.getInventory().removeOne(itemStack);
        }
        return TypedActionResult.success(itemStack);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
    }
}