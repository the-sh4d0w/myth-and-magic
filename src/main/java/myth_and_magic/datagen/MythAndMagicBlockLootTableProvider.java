package myth_and_magic.datagen;

import myth_and_magic.block.MythAndMagicBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class MythAndMagicBlockLootTableProvider extends FabricBlockLootTableProvider {
    public MythAndMagicBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(MythAndMagicBlocks.RUNE_TABLE_BLOCK);
        addDrop(MythAndMagicBlocks.INFUSION_TABLE_BLOCK);
    }
}