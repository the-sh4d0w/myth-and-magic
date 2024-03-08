package myth_and_magic.item;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicItems {
    public static final Item EXCALIBUR = register("excalibur", new ExcaliburSwordItem(new FabricItemSettings()));
    public static final Item TARNKAPPE = register("tarnkappe", new TarnkappeArmorItem(new FabricItemSettings()));
    public static final Item MAGIC_IRON_INGOT = register("magic_iron_ingot", new MagicItem(new FabricItemSettings()));
    public static final Item MAGIC_GOLD_INGOT = register("magic_gold_ingot", new MagicItem(new FabricItemSettings()));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MythAndMagic.MOD_ID, name), item);
    }

    public static void registerItems() {
        MythAndMagic.LOGGER.info("Registering Items for %s".formatted(MythAndMagic.MOD_ID));
    }
}