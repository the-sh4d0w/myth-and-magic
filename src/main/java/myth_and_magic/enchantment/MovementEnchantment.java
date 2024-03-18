package myth_and_magic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MovementEnchantment extends Enchantment {
    // TODO: this needs some improvement; make more 'agnostic'
    // cooldown reduced once per tick
    public static final int MAX_COOLDOWN = 30;
    public static int cooldown = 0;

    public MovementEnchantment(Rarity weight) {
        super(weight, EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !(other instanceof MovementEnchantment);
    }

    public static void move(ServerPlayerEntity player) {
        if (cooldown == 0) {
            ItemStack armor = player.getInventory().armor.get(1);
            int level;
            boolean armor_break = false;
            if ((level = EnchantmentHelper.getLevel(MythAndMagicEnchantments.TELEPORT_ENCHANTMENT, armor)) > 0) {
                armor_break = TeleportMovementEnchantment.move(player, armor);
            } else if ((level = EnchantmentHelper.getLevel(MythAndMagicEnchantments.DASH_ENCHANTMENT, armor)) > 0) {
                armor_break = DashMovementEnchantment.move(player, armor);
            }
            if (armor_break) {
                armor.decrement(1);
                SoundEvent soundEvent = SoundEvents.ENTITY_ITEM_BREAK;
                player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
                player.playSound(soundEvent, 1.0f, 1.0f);
            }
            cooldown = MAX_COOLDOWN;
        }
    }
}