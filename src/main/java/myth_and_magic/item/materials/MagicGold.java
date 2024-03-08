package myth_and_magic.item.materials;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class MagicGold implements ArmorMaterial {
    @Override
    public int getDurability(ArmorItem.Type type) {
        return 250;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_GOLD;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(MythAndMagicItems.MAGIC_GOLD_INGOT);
    }

    @Override
    public String getName() {
        return "magic_gold";
    }

    @Override
    public float getToughness() {
        return 0f;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}