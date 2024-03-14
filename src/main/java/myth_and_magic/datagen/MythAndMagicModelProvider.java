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
        blockStateModelGenerator.registerSimpleState(MythAndMagicBlocks.EXCALIBUR_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(MythAndMagicItems.EXCALIBUR, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.MAGE_STAFF, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.TARNKAPPE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.FIRE_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.ICE_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.HEAL_RUNE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.KNIGHT_STATUE, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.MAGIC_ALLOY_INGOT, Models.GENERATED);
    }
}