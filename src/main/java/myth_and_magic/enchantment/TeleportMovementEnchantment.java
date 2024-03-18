package myth_and_magic.enchantment;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class TeleportMovementEnchantment extends MovementEnchantment {
    protected TeleportMovementEnchantment() {
        super(Rarity.VERY_RARE);
    }

    @Override
    public int getMinPower(int level) {
        return 15 + 20 * (level - 1);
    }

    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 55;
    }

    public static boolean move(ServerPlayerEntity player, ItemStack armor) {
        if (!player.hasVehicle() && !player.isTouchingWaterOrRain() && player.getHungerManager().getFoodLevel() > 12) {
            World world = player.getWorld();
            for (float i = 0; i <= 3; i++) {
                Vec3d vec3d = player.getRotationVector().normalize().multiply(5f).add(player.getPos()).add(0f, i, 0f);
                if (!player.teleport(vec3d.x, vec3d.y, vec3d.z, false)) {
                    continue;
                }
                world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(player));
                SoundEvent soundEvent = SoundEvents.ENTITY_ENDERMAN_TELEPORT;
                world.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
                player.playSound(soundEvent, 1.0f, 1.0f);
                // cost of using
                player.addExhaustion(8);
                return armor.damage(3, Random.create(), player);
            }
        }
        return false;
    }
}