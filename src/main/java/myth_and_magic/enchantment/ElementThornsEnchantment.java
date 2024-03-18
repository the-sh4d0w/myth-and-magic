package myth_and_magic.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.ThornsEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.random.Random;

import java.util.Map;

public class ElementThornsEnchantment extends ThornsEnchantment {
    private final Type elementType;

    public ElementThornsEnchantment(Type elementType) {
        // disabling ICE for now, because frozen ticks are stupid
        super(Rarity.VERY_RARE, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        this.elementType = elementType;
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        Random random = user.getRandom();
        Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.THORNS, user);
        if (ThornsEnchantment.shouldDamageAttacker(level, random)) {
            if (attacker != null) {
                switch (this.elementType) {
                    case FIRE -> attacker.setFireTicks(level * 3 * 20);
                    case ICE -> attacker.setFrozenTicks(level * 3 * 20);
                }
            }
            if (entry != null) {
                entry.getValue().damage(3, user, entity -> entity.sendEquipmentBreakStatus(entry.getKey()));
            }
        }
    }

    public enum Type {
        FIRE,
        ICE
    }
}