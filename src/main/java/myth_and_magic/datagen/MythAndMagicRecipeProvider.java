package myth_and_magic.datagen;

import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SmithingTransformRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class MythAndMagicRecipeProvider extends FabricRecipeProvider {
    public MythAndMagicRecipeProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MythAndMagicItems.EXCALIBUR).pattern("m")
                .pattern("m").pattern("s").input('m', MythAndMagicItems.MAGIC_ALLOY_INGOT)
                .input('s', Items.STICK).criterion(FabricRecipeProvider.hasItem(MythAndMagicItems.MAGIC_ALLOY_INGOT),
                        FabricRecipeProvider.conditionsFromItem(MythAndMagicItems.MAGIC_ALLOY_INGOT)).criterion(
                        FabricRecipeProvider.hasItem(Items.STICK), FabricRecipeProvider.conditionsFromItem(Items.STICK)
                ).offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicBlocks.MAGIC_TABLE_BLOCK).pattern("oao")
                .pattern(" b ").pattern("sss").input('o', Items.OBSIDIAN).input('a', Items.AMETHYST_SHARD)
                .input('b', Items.STONE_BRICKS).input('s', Items.STONE_BRICK_SLAB)
                .criterion(FabricRecipeProvider.hasItem(Items.OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE_BRICKS), FabricRecipeProvider.conditionsFromItem(Items.STONE_BRICKS))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE_BRICK_SLAB), FabricRecipeProvider.conditionsFromItem(Items.STONE_BRICK_SLAB))
                .offerTo(exporter);
    }
}