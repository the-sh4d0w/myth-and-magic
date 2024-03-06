package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class MythAndMagicRecipeProvider extends FabricRecipeProvider {
    public MythAndMagicRecipeProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MythAndMagic.EXCALIBUR).pattern("m")
                .pattern("m").pattern("s").input('m', MythAndMagic.MAGIC_IRON_INGOT)
                .input('s', Items.STICK).criterion(FabricRecipeProvider.hasItem(MythAndMagic.MAGIC_IRON_INGOT),
                        FabricRecipeProvider.conditionsFromItem(MythAndMagic.MAGIC_IRON_INGOT)).criterion(
                        FabricRecipeProvider.hasItem(Items.STICK), FabricRecipeProvider.conditionsFromItem(Items.STICK)
                ).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagic.MAGIC_TABLE_BLOCK).pattern("oao")
                .pattern(" b ").pattern("sss").input('o', Items.OBSIDIAN).input('a', Items.AMETHYST_SHARD)
                .input('b', Items.STONE_BRICKS).input('s', Items.STONE_BRICK_SLAB)
                .criterion(FabricRecipeProvider.hasItem(Items.OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE_BRICKS), FabricRecipeProvider.conditionsFromItem(Items.STONE_BRICKS))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE_BRICK_SLAB), FabricRecipeProvider.conditionsFromItem(Items.STONE_BRICK_SLAB))
                .offerTo(exporter);
    }
}