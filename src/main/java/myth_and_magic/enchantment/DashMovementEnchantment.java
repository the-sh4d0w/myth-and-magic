package myth_and_magic.enchantment;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DashMovementEnchantment extends MovementEnchantment {
    public DashMovementEnchantment() {
        super(Rarity.COMMON);
    }

    @Override
    public int getMinPower(int level) {
        return 11;
    }

    @Override
    public int getMaxPower(int level) {
        return 35;
    }

    public static boolean move(ServerPlayerEntity player, ItemStack armor) {
        if (!player.hasVehicle() && !player.isSubmergedInWater() && player.getHungerManager().getFoodLevel() > 6 && !player.isOnGround()) {
            player.addVelocity(player.getRotationVector().normalize());
            player.velocityModified = true;
            SoundEvent soundEvent = SoundEvents.BLOCK_SAND_BREAK;
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent,
                    SoundCategory.PLAYERS, 1.0f, 1.0f);
            player.playSound(soundEvent, 1.0f, 1.0f);
            // cost of using
            player.addExhaustion(2);
            return armor.damage(1, Random.create(), player);
        }
        return false;
    }
}