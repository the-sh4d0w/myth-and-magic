package myth_and_magic.datagen;

import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class MythAndMagicModelProvider extends FabricModelProvider {
    public MythAndMagicModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleState(MythAndMagicBlocks.RUNE_TABLE_BLOCK);
        blockStateModelGenerator.registerSimpleState(MythAndMagicBlocks.INFUSION_TABLE_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(MythAndMagicItems.EXCALIBUR, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.NARCISSUS_MIRROR, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.MAGE_STAFF, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.GOLDEN_MAGE_STAFF, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.FIRE_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.ICE_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.HEAL_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.LIGHTNING_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.EXPLOSIVE_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.HOME_SPELL, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.GROW_SPELL, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.GLASS_PHIAL, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.LEVEL_PHIAL, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.KNIGHT_STATUE, Models.GENERATED);
    }
}