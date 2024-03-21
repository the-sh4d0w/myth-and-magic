package myth_and_magic.item;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicItems {
    public static final FoodComponent LEVEL_PHIAL_FOOD = new FoodComponent.Builder().alwaysEdible().build();

    public static final Item EXCALIBUR = register("excalibur", new ExcaliburSwordItem(new FabricItemSettings()));
    public static final Item MAGE_STAFF = register("mage_staff", new MageStaffItem(new FabricItemSettings(), 100));
    public static final Item GOLDEN_MAGE_STAFF = register("golden_mage_staff", new MageStaffItem(new FabricItemSettings(), 300));
    public static final Item TARNKAPPE = register("tarnkappe", new TarnkappeArmorItem(new FabricItemSettings()));
    public static final Item RUNE = register("rune", new Item(new FabricItemSettings()));
    public static final Item FIRE_RUNE = register("fire_rune", new Item(new FabricItemSettings()));
    public static final Item ICE_RUNE = register("ice_rune", new Item(new FabricItemSettings()));
    public static final Item HEAL_RUNE = register("heal_rune", new Item(new FabricItemSettings()));
    public static final Item LIGHTNING_RUNE = register("lightning_rune", new Item(new FabricItemSettings()));
    public static final Item EXPLOSIVE_RUNE = register("explosive_rune", new Item(new FabricItemSettings()));
    public static final Item HOME_SPELL = register("domicilium_spell", new SpellItem(new FabricItemSettings(), SpellItem.Type.HOME));
    public static final Item GROW_SPELL = register("cresco_spell", new SpellItem(new FabricItemSettings(), SpellItem.Type.GROW));
    public static final Item GLASS_PHIAL = register("glass_phial", new Item(new FabricItemSettings()));
    public static final Item LEVEL_PHIAL = register("level_phial", new LevelPhialItem(new FabricItemSettings().food(LEVEL_PHIAL_FOOD)));
    public static final Item KNIGHT_STATUE = register("knight_statue", new KnightStatueItem(new FabricItemSettings()));
    public static final Item MAGIC_ALLOY_INGOT = register("magic_alloy_ingot", new Item(new FabricItemSettings()));

    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MythAndMagic.MOD_ID, name), item);
    }

    public static void registerItems() {
        MythAndMagic.LOGGER.info("Registering Items for %s".formatted(MythAndMagic.MOD_ID));
    }
}