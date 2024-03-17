package myth_and_magic;

import myth_and_magic.entity.MythAndMagicEntities;
import myth_and_magic.entity.client.KnightModel;
import myth_and_magic.entity.client.KnightRenderer;
import myth_and_magic.entity.client.MythAndMagicModelLayers;
import myth_and_magic.screen.InfusionTableScreen;
import myth_and_magic.screen.RuneTableScreen;
import myth_and_magic.screen.MythAndMagicScreenHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MythAndMagicClient implements ClientModInitializer {
    private static final KeyBinding CALL_EXCALIBUR_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.myth_and_magic.call", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.myth_and_magic.name"));
    private static final KeyBinding DASH_KEYBINDING = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.myth_and_magic.move", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "category.myth_and_magic.name"));

    @Override
    public void onInitializeClient() {
        HandledScreens.register(MythAndMagicScreenHandlers.RUNE_TABLE_SCREEN_HANDLER, RuneTableScreen::new);
        HandledScreens.register(MythAndMagicScreenHandlers.INFUSION_TABLE_SCREEN_HANDLER, InfusionTableScreen::new);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (CALL_EXCALIBUR_KEYBINDING.wasPressed()) {
                ClientPlayNetworking.send(MythAndMagic.CALL_SWORD_PACKET_ID, PacketByteBufs.empty());
            }
            while (DASH_KEYBINDING.wasPressed()) {
                ClientPlayNetworking.send(MythAndMagic.MOVE_PACKET_ID, PacketByteBufs.empty());
            }
        });
        EntityRendererRegistry.register(MythAndMagicEntities.RUNE_PROJECTILE, FlyingItemEntityRenderer::new);
        EntityRendererRegistry.register(MythAndMagicEntities.KNIGHT, KnightRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MythAndMagicModelLayers.KNIGHT, KnightModel::getTexturedModelData);
    }
}