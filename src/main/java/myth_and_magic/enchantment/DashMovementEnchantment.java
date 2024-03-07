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
        super(Rarity.UNCOMMON);
    }

    public static boolean move(ServerPlayerEntity player, ItemStack armor, int level) {
        if (!player.hasVehicle() && player.getHungerManager().getFoodLevel() > 6 && !player.isOnGround()) {
            World world = player.getWorld();
            if (!world.isClient()) {
                player.addVelocity(player.getRotationVector().normalize());
                player.velocityModified = true;
                SoundEvent soundEvent = SoundEvents.BLOCK_SAND_BREAK;
                world.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
                player.playSound(soundEvent, 1.0f, 1.0f);
                // cost of using
                player.addExhaustion(2);
                return armor.damage(1, Random.create(), player);
            }
        }
        return false;
    }
}