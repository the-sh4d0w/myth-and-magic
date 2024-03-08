package myth_and_magic.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiStack;
import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.recipe.MagicTableRecipe;
import myth_and_magic.recipe.MythAndMagicRecipes;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.util.Identifier;

public class MythAndMagicEmiPlugin implements EmiPlugin {
    public static final Identifier MAGIC_TABLE_ICON = new Identifier(MythAndMagic.MOD_ID, "textures/gui/magic_table_icon.png");
    public static final EmiStack MAGIC_TABLE = EmiStack.of(MythAndMagicBlocks.MAGIC_TABLE_ITEM);
    public static final EmiRecipeCategory MAGIC_TABLE_CATEGORY = new EmiRecipeCategory(new Identifier(MythAndMagic.MOD_ID, "magic_table"),
            MAGIC_TABLE, new EmiTexture(MAGIC_TABLE_ICON, 0, 0, 16, 16, 16, 16, 16, 16));

    @Override
    public void register(EmiRegistry registry) {
        registry.addCategory(MAGIC_TABLE_CATEGORY);
        registry.addWorkstation(MAGIC_TABLE_CATEGORY, MAGIC_TABLE);
        RecipeManager manager = registry.getRecipeManager();
        for (MagicTableRecipe recipe : manager.listAllOfType(MythAndMagicRecipes.MAGIC_TABLE_RECIPE)) {
            registry.addRecipe(new MagicTableEmiRecipe(recipe));
        }
    }
}