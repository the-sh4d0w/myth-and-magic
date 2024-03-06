package myth_and_magic.enchantment;

import myth_and_magic.MythAndMagic;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicEnchantments {
    public static final Enchantment TELEPORT = register("teleport_evasion", new TeleportEvasionEnchantment());

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(MythAndMagic.MOD_ID, name), enchantment);
    }

    public static void registerEnchantments() {
        MythAndMagic.LOGGER.info("Registering Enchantments for %s".formatted(MythAndMagic.MOD_ID));
    }
}