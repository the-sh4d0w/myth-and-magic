package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
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
        blockStateModelGenerator.registerSimpleState(MythAndMagic.MAGIC_TABLE_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(MythAndMagic.EXCALIBUR, Models.HANDHELD);
        itemModelGenerator.register(MythAndMagic.MAGIC_IRON_INGOT, Models.GENERATED);
        itemModelGenerator.register(MythAndMagic.MAGIC_GOLD_INGOT, Models.GENERATED);
    }
}