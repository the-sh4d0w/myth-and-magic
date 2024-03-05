package myth_and_magic;

import myth_and_magic.item.ExcaliburSwordItem;
import myth_and_magic.screen.MagicTableScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MythAndMagicClient implements ClientModInitializer {
	private static KeyBinding keyBinding;

	@Override
	public void onInitializeClient() {
		HandledScreens.register(MythAndMagic.MAGIC_TABLE_SCREEN_HANDLER, MagicTableScreen::new);
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.myth_and_magic.call", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT,
				"category.myth_and_magic.name"));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				ClientPlayNetworking.send(ExcaliburSwordItem.CALL_SWORD_PACKET_ID, PacketByteBufs.empty());
			}
		});
	}
}