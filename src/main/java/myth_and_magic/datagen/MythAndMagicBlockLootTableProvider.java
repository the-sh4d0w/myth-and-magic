package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

public class MythAndMagicBlockLootTableProvider extends FabricBlockLootTableProvider {
    public MythAndMagicBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(MythAndMagic.MAGIC_TABLE_BLOCK);
    }
}