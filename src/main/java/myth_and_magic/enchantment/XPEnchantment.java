package myth_and_magic.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class XPEnchantment extends Enchantment {
    public static int multiply_value = 3;

    public XPEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 11;
    }

    @Override
    public int getMaxPower(int level) {
        return 35;
    }
}