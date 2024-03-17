package myth_and_magic.recipe;

import myth_and_magic.MythAndMagic;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicRecipes {
    // works for now
    public static final RuneTableRecipe.Serializer RUNE_TABLE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            RuneTableRecipe.Serializer.ID, RuneTableRecipe.Serializer.INSTANCE);
    public static final RuneTableRecipe.Type RUNE_TABLE_RECIPE = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(MythAndMagic.MOD_ID, RuneTableRecipe.Type.ID), RuneTableRecipe.Type.INSTANCE);
    public static final InfusionTableRecipe.Serializer INFUSION_TABLE_SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            InfusionTableRecipe.Serializer.ID, InfusionTableRecipe.Serializer.INSTANCE);
    public static final InfusionTableRecipe.Type INFUSION_TABLE_RECIPE = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(MythAndMagic.MOD_ID, InfusionTableRecipe.Type.ID), InfusionTableRecipe.Type.INSTANCE);

    public static void registerRecipes() {
        MythAndMagic.LOGGER.info("Registering Recipes for %s".formatted(MythAndMagic.MOD_ID));
    }
}