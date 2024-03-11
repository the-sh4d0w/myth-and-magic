package myth_and_magic;

import myth_and_magic.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MythAndMagicDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(MythAndMagicAdvancementsProvider::new);
        pack.addProvider(MythAndMagicBlockLootTableProvider::new);
        pack.addProvider(MythAndMagicBlockTagProvider::new);
        pack.addProvider(MythAndMagicItemTagProvider::new);
        pack.addProvider(MythAndMagicModelProvider::new);
        pack.addProvider(MythAndMagicRecipeProvider::new);
    }
}