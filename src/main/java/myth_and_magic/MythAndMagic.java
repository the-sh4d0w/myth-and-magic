package myth_and_magic;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import myth_and_magic.block.MagicTableBlock;
import myth_and_magic.block.entity.MagicTableBlockEntity;
import myth_and_magic.enchantment.TeleportEvasionEnchantment;
import myth_and_magic.item.MagicItem;
import myth_and_magic.screen.MagicTableScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.command.CommandException;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
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
    public static final String MOD_ID = "myth_and_magic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // items
    public static final Item EXCALIBUR = Registry.register(Registries.ITEM,
            new Identifier(MOD_ID, "excalibur"), new ExcaliburSwordItem(new FabricItemSettings()));
    public static final Item MAGIC_IRON_INGOT = Registry.register(Registries.ITEM,
            new Identifier(MOD_ID, "magic_iron_ingot"), new MagicItem(new FabricItemSettings()));
    public static final Item MAGIC_GOLD_INGOT = Registry.register(Registries.ITEM,
            new Identifier(MOD_ID, "magic_gold_ingot"), new MagicItem(new FabricItemSettings()));
    // enchantments
    public static final Enchantment TELEPORT = Registry.register(Registries.ENCHANTMENT,
            new Identifier(MOD_ID, "teleport_evasion"), new TeleportEvasionEnchantment());
    // magic table block
    public static final Block MAGIC_TABLE_BLOCK = Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "magic_table"),
            new MagicTableBlock(FabricBlockSettings.create().strength(4.0f).requiresTool().luminance(50)));
    public static final BlockEntityType<MagicTableBlockEntity> MAGIC_TABLE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, new Identifier(MOD_ID, "magic_table_entity"),
            FabricBlockEntityTypeBuilder.create(MagicTableBlockEntity::new, MAGIC_TABLE_BLOCK).build());
    public static final BlockItem MAGIC_TABLE_ITEM = Registry.register(Registries.ITEM,
            new Identifier(MOD_ID, "magic_table"), new BlockItem(MAGIC_TABLE_BLOCK, new FabricItemSettings().maxCount(1)));
    public static final ScreenHandlerType<MagicTableScreenHandler> MAGIC_TABLE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER,
            new Identifier(MOD_ID, "magic_table"), new ExtendedScreenHandlerType<>(MagicTableScreenHandler::new));
    // item group
    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(MAGIC_TABLE_ITEM))
            .displayName(Text.literal("Myth & Magic")).entries(((displayContext, entries) -> {
                entries.add(MAGIC_TABLE_ITEM);
                entries.add(MAGIC_IRON_INGOT);
                entries.add(MAGIC_GOLD_INGOT);
                entries.add(EXCALIBUR);
            })).build();
    public static ExcaliburClaimedCriterion EXCALIBUR_CLAIMED = Criteria.register(new ExcaliburClaimedCriterion());
    // TODO: add enchantments
    // - teleport to trident (orignal, I know; maybe)
    // - movement
    // TODO: legendary items
    // - Tarnkappe (or equivalent; full invisibility but half health)
    // TODO: more magic -> what? (spells, staffs, armor/clothing, magic table to create special items)
    // - staff with gems? that give specific powers (movement, attack)
    // - magic table to upgrade vanilla items (argonium -> magic iron?)

    @Override
    public void onInitialize() {
        // register item group
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "item_group"), ITEM_GROUP);

        // handle packet to get call sword key press
        ServerPlayNetworking.registerGlobalReceiver(ExcaliburSwordItem.CALL_SWORD_PACKET_ID,
                (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf,
                 PacketSender responseSender) -> {
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    if (playerState.boundSword) {
                        if (player.getInventory().containsAny(Set.of(EXCALIBUR)) && player.getInventory().containsAny(stack ->
                                stack.getOrCreateNbt().contains(MOD_ID + ".owner") && stack.getOrCreateNbt().getUuid(
                                        MOD_ID + ".owner").equals(player.getUuid()))) {
                            player.sendMessage(Text.literal("Sword is already in inventory."));
                        } else {
                            player.sendMessage(Text.literal("Summoning sword."));
                        }
                    } else {
                        player.sendMessage(Text.literal("No sword is bound to you."));
                    }
                });

        // register commands for setting and getting worthiness
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("worthiness")
                        .requires(source -> source.hasPermissionLevel(2) && source.isExecutedByPlayer())
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            int value = IntegerArgumentType.getInteger(context, "value");
                                            if (ExcaliburSwordItem.MIN_WORTHINESS <= value && value <= ExcaliburSwordItem.MAX_WORTHINESS) {
                                                PlayerData playerState = StateSaverAndLoader.getPlayerState(context.getSource().getPlayer());
                                                playerState.worthiness = value;
                                                context.getSource().sendFeedback(() -> Text.translatable(
                                                        "command." + MOD_ID + ".worthiness_set_response",
                                                        playerState.worthiness), false);
                                                return Command.SINGLE_SUCCESS;
                                            } else {
                                                throw new CommandException(Text.translatable("command." + MOD_ID + ".value_exception",
                                                        ExcaliburSwordItem.MIN_WORTHINESS, ExcaliburSwordItem.MAX_WORTHINESS));
                                            }
                                        })))
                        .then(CommandManager.literal("get")
                                .executes(context -> {
                                    PlayerData playerState = StateSaverAndLoader.getPlayerState(context.getSource().getPlayer());
                                    context.getSource().sendFeedback(() -> Text.translatable(
                                            "command." + MOD_ID + ".worthiness_get_response",
                                            playerState.worthiness), false);
                                    return playerState.worthiness;
                                }))));
    }
}