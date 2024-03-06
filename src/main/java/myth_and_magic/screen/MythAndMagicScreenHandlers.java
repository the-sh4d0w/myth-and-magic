package myth_and_magic.screen;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MythAndMagicScreenHandlers {
    // works for now
    public static final ScreenHandlerType<MagicTableScreenHandler> MAGIC_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(MythAndMagic.MOD_ID, "magic_table"),
                    new ExtendedScreenHandlerType<>(MagicTableScreenHandler::new));

    public static void registerScreenHandlers() {
        MythAndMagic.LOGGER.info("Registering ScreenHandlers for %s".formatted(MythAndMagic.MOD_ID));
    }
}