package myth_and_magic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class MovementEnchantment extends Enchantment {
    // TODO: this needs some improvement; make more 'agnostic'
    public MovementEnchantment(Rarity weight) {
        super(weight, EnchantmentTarget.ARMOR_LEGS, new EquipmentSlot[]{EquipmentSlot.LEGS});
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return !(other instanceof MovementEnchantment);
    }

    public static void move(ServerPlayerEntity player) {
        // TODO: add cooldown
        ItemStack armor = player.getInventory().armor.get(1);
        int level;
        boolean armor_break = false;
        if ((level = EnchantmentHelper.getLevel(MythAndMagicEnchantments.TELEPORT_ENCHANTMENT, armor)) > 0) {
            armor_break = TeleportMovementEnchantment.move(player, armor, level);
        } else if ((level = EnchantmentHelper.getLevel(MythAndMagicEnchantments.DASH_ENCHANTMENT, armor)) > 0) {
            armor_break = DashMovementEnchantment.move(player, armor, level);
        }
        if (armor_break) {
            armor.decrement(1);
        }
    }
}