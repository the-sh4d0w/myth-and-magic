package myth_and_magic.item.materials;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class MagicGold implements ToolMaterial {
    // yes, this is kinda OP, that's the point
    @Override
    public int getDurability() {
        return -1;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 8.0f;
    }

    @Override
    public float getAttackDamage() {
        return 4.0f;
    }

    @Override
    public int getMiningLevel() {
        return 3;
    }

    @Override
    public int getEnchantability() {
        return 20;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(MythAndMagicItems.MAGIC_GOLD_INGOT);
    }
}