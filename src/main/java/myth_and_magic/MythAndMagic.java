package myth_and_magic;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import myth_and_magic.item.ArgoniumIngotItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import myth_and_magic.item.ExcaliburSwordItem;

import java.util.Set;

public class MythAndMagic implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("myth_and_magic");
    public static final Item EXCALIBUR = Registry.register(Registries.ITEM, new Identifier("myth_and_magic",
            "excalibur"), new ExcaliburSwordItem(new FabricItemSettings()));
    public static final Item ARGONIUM_INGOT = Registry.register(Registries.ITEM, new Identifier("myth_and_magic",
            "argonium_ingot"), new ArgoniumIngotItem(new FabricItemSettings()));

    @Override
    public void onInitialize() {
        // register item groups
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(EXCALIBUR);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(ARGONIUM_INGOT);
        });

        // handle packet to get call sword key press
        ServerPlayNetworking.registerGlobalReceiver(ExcaliburSwordItem.CALL_SWORD_PACKET_ID, (MinecraftServer server, ServerPlayerEntity player,
                                                                                              ServerPlayNetworkHandler handler, PacketByteBuf buf,
                                                                                              PacketSender responseSender) -> {
            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
            if (playerState.boundSword) {
                if (player.getInventory().containsAny(Set.of(EXCALIBUR)) && player.getInventory().containsAny(stack ->
                        stack.getOrCreateNbt().contains("myth_and_magic.owner") && stack.getOrCreateNbt().getUuid(
                                "myth_and_magic.owner").equals(player.getUuid()))) {
                    player.sendMessage(Text.literal("Sword is already in inventory."));
                } else {
                    player.sendMessage(Text.literal("Summoning sword."));
                }
            } else {
                player.sendMessage(Text.literal("No sword is bound to you."));
            }
        });

        // register commands for setting and getting worthiness
        // TODO: improve commands; handle .getPlayer() returning null (command not executed by player)
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("worthiness")
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            PlayerData playerState = StateSaverAndLoader.getPlayerState(context.getSource().getPlayer());
                                            playerState.worthiness = IntegerArgumentType.getInteger(context, "value");;
                                            return 1;
                                        })))
                        .then(CommandManager.literal("get")
                                .executes(context -> {
                                    PlayerData playerState = StateSaverAndLoader.getPlayerState(context.getSource().getPlayer());
                                    context.getSource().sendFeedback(() -> Text.literal("%s".formatted(playerState.worthiness)), false);
                                    return playerState.worthiness;
                                }))));
    }
}