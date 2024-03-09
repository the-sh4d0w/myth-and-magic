package myth_and_magic;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.enchantment.MovementEnchantment;
import myth_and_magic.enchantment.MythAndMagicEnchantments;
import myth_and_magic.item.MythAndMagicItems;
import myth_and_magic.item.TarnkappeArmorItem;
import myth_and_magic.recipe.MythAndMagicRecipes;
import myth_and_magic.screen.MythAndMagicScreenHandlers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.command.CommandException;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
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
    public static final String MOD_ID = "myth_and_magic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // criteria
    public static ExcaliburClaimedCriterion EXCALIBUR_CLAIMED = Criteria.register(new ExcaliburClaimedCriterion());
    // network packet ids
    public static final Identifier CALL_SWORD_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "call_sword");
    public static Identifier MOVE_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "move");
    // TODO: add enchantments
    // - teleport to trident (original, I know; maybe)
    // - movement
    // TODO: legendary items
    // - Tarnkappe (or equivalent; full invisibility but half health)
    // TODO: more magic -> what? (spells, staffs, armor/clothing, magic table to create special items)
    // - staff with gems? that give specific powers (movement, attack)
    // - magic table to upgrade vanilla items (argonium -> magic iron?)
    // TODO: translate everything
    // TODO: translate advancements
    // TODO: add JEI support
    // TODO: add EMI support for anvil combining
    // TODO: add power providers to magic table (like enchantment table)

    @Override
    public void onInitialize() {
        // register custom content
        MythAndMagicItems.registerItems();
        MythAndMagicEnchantments.registerEnchantments();
        MythAndMagicScreenHandlers.registerScreenHandlers();
        MythAndMagicBlocks.registerBlocks();
        MythAndMagicRecipes.registerRecipes();

        // register item group
        ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(MythAndMagicBlocks.RUNE_TABLE_BLOCK))
                .displayName(Text.literal("Myth & Magic")).entries(((displayContext, entries) -> {
                    entries.add(MythAndMagicBlocks.EXCALIBUR_BLOCK_ITEM);
                    entries.add(MythAndMagicBlocks.RUNE_TABLE_ITEM);
                    entries.add(MythAndMagicItems.RUNE);
                    entries.add(MythAndMagicItems.FIRE_RUNE);
                    entries.add(MythAndMagicItems.MAGIC_ALLOY_INGOT);
                    entries.add(MythAndMagicItems.EXCALIBUR);
                    entries.add(MythAndMagicItems.MAGE_STAFF);
                    entries.add(MythAndMagicItems.TARNKAPPE);
                })).build();
        Registry.register(Registries.ITEM_GROUP, new Identifier(MOD_ID, "item_group"), ITEM_GROUP);

        // handle packet to get key press
        ServerPlayNetworking.registerGlobalReceiver(CALL_SWORD_PACKET_ID,
                (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf,
                 PacketSender responseSender) -> {
                    PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                    if (playerState.boundSword) {
                        if (player.getInventory().containsAny(Set.of(MythAndMagicItems.EXCALIBUR)) && player.getInventory().containsAny(stack ->
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
        ServerPlayNetworking.registerGlobalReceiver(MOVE_PACKET_ID,
                (MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf,
                 PacketSender responseSender) -> {
                    MovementEnchantment.move(player);
                });

        // change health on (un)equip of Tarnkappe
        ServerEntityEvents.EQUIPMENT_CHANGE.register((LivingEntity user, EquipmentSlot slot, ItemStack previous, ItemStack current) -> {
            if (slot.isArmorSlot() && user.isAlive() && !previous.getItem().equals(current.getItem())) {
                EntityAttributeInstance attribute = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
                if (current.getItem().equals(MythAndMagicItems.TARNKAPPE)) {
                    attribute.setBaseValue(TarnkappeArmorItem.REDUCED_HEALTH);
                    user.setHealth(TarnkappeArmorItem.REDUCED_HEALTH);
                    user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false));
                } else if (previous.getItem().equals(MythAndMagicItems.TARNKAPPE)) {
                    attribute.setBaseValue(TarnkappeArmorItem.NORMAL_HEALTH);
                    user.setHealth(TarnkappeArmorItem.NORMAL_HEALTH);
                    user.removeStatusEffect(StatusEffects.INVISIBILITY);
                }
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