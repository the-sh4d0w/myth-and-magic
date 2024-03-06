package myth_and_magic.recipe;

import myth_and_magic.MythAndMagic;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicRecipes {
    // works for now
    public static final MagicTableRecipe.Serializer SERIALIZER = Registry.register(Registries.RECIPE_SERIALIZER,
            MagicTableRecipe.Serializer.ID, MagicTableRecipe.Serializer.INSTANCE);
    public static final MagicTableRecipe.Type MAGIC_TABLE_RECIPE = Registry.register(Registries.RECIPE_TYPE,
            new Identifier(MythAndMagic.MOD_ID, MagicTableRecipe.Type.ID), MagicTableRecipe.Type.INSTANCE);

    public static void registerRecipes() {
        MythAndMagic.LOGGER.info("Registering Recipes for %s".formatted(MythAndMagic.MOD_ID));
    }
}