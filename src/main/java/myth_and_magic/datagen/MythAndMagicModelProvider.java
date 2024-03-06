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
        blockStateModelGenerator.registerSimpleState(MythAndMagicBlocks.MAGIC_TABLE_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(MythAndMagicItems.EXCALIBUR, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagicItems.MAGIC_IRON_INGOT, Models.GENERATED);
        itemModelGenerator.register(MythAndMagicItems.MAGIC_GOLD_INGOT, Models.GENERATED);
    }
}