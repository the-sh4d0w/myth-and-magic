package myth_and_magic.item.materials;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class MagicIron implements ToolMaterial {
    // yes, this is kinda OP, that's the point
    @Override
    public int getDurability() {
        return -1;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 10.0f;
    }

    @Override
    public float getAttackDamage() {
        return 5.0f;
    }

    @Override
    public int getMiningLevel() {
        return 4;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(MythAndMagicItems.MAGIC_IRON_INGOT);
    }
}