package myth_and_magic.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.recipe.RuneTableRecipe;
import myth_and_magic.recipe.MythAndMagicRecipes;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class MythAndMagicEmiPlugin implements EmiPlugin {
    public static final Identifier RUNE_TABLE_ICON = new Identifier(MythAndMagic.MOD_ID, "textures/gui/rune_table_icon.png");
    public static final EmiStack RUNE_TABLE = EmiStack.of(MythAndMagicBlocks.RUNE_TABLE_ITEM);
    public static final EmiRecipeCategory RUNE_TABLE_CATEGORY = new EmiRecipeCategory(new Identifier(MythAndMagic.MOD_ID, "rune_table"),
            RUNE_TABLE, new EmiTexture(RUNE_TABLE_ICON, 0, 0, 16, 16, 16, 16, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(RUNE_TABLE_CATEGORY);
        registry.addWorkstation(RUNE_TABLE_CATEGORY, RUNE_TABLE);
        RecipeManager manager = registry.getRecipeManager();
        for (RuneTableRecipe recipe : manager.listAllOfType(MythAndMagicRecipes.RUNE_TABLE_RECIPE)) {
            registry.addRecipe(new RuneTableEmiRecipe(recipe));
        }
    }
}