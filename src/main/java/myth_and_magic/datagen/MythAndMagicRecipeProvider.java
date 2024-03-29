package myth_and_magic.datagen;

import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.item.MythAndMagicItems;
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
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicBlocks.RUNE_TABLE_BLOCK).pattern("gag")
                .pattern(" o ").pattern("bbb").input('o', Items.OBSIDIAN).input('a', Items.AMETHYST_SHARD)
                .input('b', Items.STONE_BRICKS).input('g', Items.GOLD_INGOT)
                .criterion(FabricRecipeProvider.hasItem(Items.OBSIDIAN), FabricRecipeProvider.conditionsFromItem(Items.OBSIDIAN))
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE_BRICKS), FabricRecipeProvider.conditionsFromItem(Items.STONE_BRICKS))
                .criterion(FabricRecipeProvider.hasItem(Items.GOLD_INGOT), FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicBlocks.INFUSION_TABLE_BLOCK).pattern("dad")
                .pattern("s s").input('d', Items.DARK_OAK_PLANKS).input('a', Items.AMETHYST_SHARD).input('s', Items.STICK)
                .criterion(FabricRecipeProvider.hasItem(Items.DARK_OAK_PLANKS), FabricRecipeProvider.conditionsFromItem(Items.DARK_OAK_PLANKS))
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.STICK), FabricRecipeProvider.conditionsFromItem(Items.STICK))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicItems.RUNE).pattern(" s ")
                .pattern("sss").pattern(" s ").input('s', Items.STONE)
                .criterion(FabricRecipeProvider.hasItem(Items.STONE), FabricRecipeProvider.conditionsFromItem(Items.STONE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicItems.GLASS_PHIAL).pattern(" g ")
                .pattern("g g").pattern(" g ").input('g', Items.GLASS)
                .criterion(FabricRecipeProvider.hasItem(Items.GLASS), FabricRecipeProvider.conditionsFromItem(Items.GLASS))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MythAndMagicItems.MAGE_STAFF).pattern("  a")
                .pattern(" s ").pattern("s  ").input('a', Items.AMETHYST_SHARD).input('s', Items.STICK)
                .criterion(FabricRecipeProvider.hasItem(Items.AMETHYST_SHARD), FabricRecipeProvider.conditionsFromItem(Items.AMETHYST_SHARD))
                .criterion(FabricRecipeProvider.hasItem(Items.STICK), FabricRecipeProvider.conditionsFromItem(Items.STICK))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, MythAndMagicItems.GOLDEN_MAGE_STAFF).pattern(" g ")
                .pattern(" sg").input('s', MythAndMagicItems.MAGE_STAFF).input('g', Items.GOLD_INGOT)
                .criterion(FabricRecipeProvider.hasItem(MythAndMagicItems.MAGE_STAFF), FabricRecipeProvider.conditionsFromItem(MythAndMagicItems.MAGE_STAFF))
                .criterion(FabricRecipeProvider.hasItem(Items.GOLD_INGOT), FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, MythAndMagicItems.KNIGHT_STATUE).pattern("ggg")
                .pattern("sns").pattern("tdt").input('g', Items.GOLD_INGOT).input('s', Items.SOUL_SAND)
                .input('n', Items.NETHERITE_INGOT).input('t', Items.STONE).input('d', Items.DIAMOND)
                .criterion(FabricRecipeProvider.hasItem(Items.GOLD_INGOT), FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.SOUL_SAND), FabricRecipeProvider.conditionsFromItem(Items.SOUL_SAND))
                .criterion(FabricRecipeProvider.hasItem(Items.NETHERITE_INGOT), FabricRecipeProvider.conditionsFromItem(Items.NETHERITE_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE), FabricRecipeProvider.conditionsFromItem(Items.STONE))
                .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND), FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, MythAndMagicItems.NARCISSUS_MIRROR).pattern("ggg")
                .pattern("gdg").pattern("ggg").input('g', Items.GOLD_INGOT).input('d', Items.DIAMOND)
                .criterion(FabricRecipeProvider.hasItem(Items.GOLD_INGOT), FabricRecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
                .criterion(FabricRecipeProvider.hasItem(Items.DIAMOND), FabricRecipeProvider.conditionsFromItem(Items.DIAMOND))
                .offerTo(exporter);
    }
}