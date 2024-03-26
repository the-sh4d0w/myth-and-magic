package myth_and_magic;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.criteria.*;
import myth_and_magic.enchantment.MovementEnchantment;
import myth_and_magic.enchantment.MythAndMagicEnchantments;
import myth_and_magic.entity.KnightEntity;
import myth_and_magic.entity.MythAndMagicEntities;
import myth_and_magic.item.MythAndMagicItems;
import myth_and_magic.item.TarnkappeArmorItem;
import myth_and_magic.recipe.MythAndMagicRecipes;
import myth_and_magic.screen.MythAndMagicScreenHandlers;
import myth_and_magic.util.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.command.CommandException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
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
    // criteria; maybe too many?
    public static ExcaliburClaimedCriterion EXCALIBUR_CLAIMED = Criteria.register(new ExcaliburClaimedCriterion());
    public static ExcaliburCalledCriterion EXCALIBUR_CALLED = Criteria.register(new ExcaliburCalledCriterion());
    public static TaskCompletedCriterion TASK_COMPLETED = Criteria.register(new TaskCompletedCriterion());
    public static EnchantmentUpgradedCriterion ENCHANTMENT_UPGRADED = Criteria.register(new EnchantmentUpgradedCriterion());
    public static RecipeInfusionCriterion RECIPE_INFUSION = Criteria.register(new RecipeInfusionCriterion());
    public static RecipeRuneCriterion RECIPE_RUNE = Criteria.register(new RecipeRuneCriterion());
    public static HealRuneUsedCriterion HEAL_RUNE_USED = Criteria.register(new HealRuneUsedCriterion());
    public static KnightProtectionCriterion KNIGHT_PROTECT = Criteria.register(new KnightProtectionCriterion());
    // network packet ids
    public static final Identifier CALL_SWORD_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "call_sword");
    public static Identifier MOVE_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "move");
    // TODO: translate everything
    // TODO: translate advancements
    // TODO: add REI support later
    // TODO: maybe generate book dynamically?

    @Override
    public void onInitialize() {
        // register custom content
        MythAndMagicItems.registerItems();
        MythAndMagicEnchantments.registerEnchantments();
        MythAndMagicScreenHandlers.registerScreenHandlers();
        MythAndMagicBlocks.registerBlocks();
        MythAndMagicRecipes.registerRecipes();
        MythAndMagicEntities.registerEntities();
        FabricDefaultAttributeRegistry.register(MythAndMagicEntities.KNIGHT, KnightEntity.createKnightAttributes());

        // register item group
        ItemGroup ITEM_GROUP = FabricItemGroup.builder().icon(() -> new ItemStack(MythAndMagicBlocks.RUNE_TABLE_BLOCK))
                .displayName(Text.literal("Myth & Magic")).entries(((displayContext, entries) -> {
                    entries.add(MythAndMagicBlocks.EXCALIBUR_ITEM);
                    entries.add(MythAndMagicBlocks.RUNE_TABLE_ITEM);
                    entries.add(MythAndMagicBlocks.INFUSION_TABLE_ITEM);
                    entries.add(MythAndMagicItems.KNIGHT_STATUE);
                    entries.add(MythAndMagicItems.RUNE);
                    entries.add(MythAndMagicItems.FIRE_RUNE);
                    entries.add(MythAndMagicItems.ICE_RUNE);
                    entries.add(MythAndMagicItems.HEAL_RUNE);
                    entries.add(MythAndMagicItems.LIGHTNING_RUNE);
                    entries.add(MythAndMagicItems.EXPLOSIVE_RUNE);
                    entries.add(MythAndMagicItems.HOME_SPELL);
                    entries.add(MythAndMagicItems.GROW_SPELL);
                    entries.add(MythAndMagicItems.GLASS_PHIAL);
                    entries.add(MythAndMagicItems.LEVEL_PHIAL);
                    entries.add(MythAndMagicItems.EXCALIBUR);
                    entries.add(MythAndMagicItems.NARCISSUS_MIRROR);
                    entries.add(MythAndMagicItems.MAGE_STAFF);
                    entries.add(MythAndMagicItems.GOLDEN_MAGE_STAFF);
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
                 PacketSender responseSender) -> MovementEnchantment.move(player));

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

        // add worthiness on advancement granted
        AdvancementGrantedCallback.EVENT.register(((player, advancement) -> {
            PlayerData playerData = StateSaverAndLoader.getPlayerState(player);
            int value = switch (advancement.getId().toString()) {
                // TODO: add player healed with rune (1)
                case "minecraft:end/kill_dragon", "minecraft:adventure/kill_all_mobs",
                        "myth_and_magic:tasks/kill_wither" -> 2;
                case "minecraft:story/cure_zombie_villager", "minecraft:adventure/hero_of_the_village",
                        "minecraft:adventure/kill_a_mob", "myth_and_magic:magic/used_heal_rune" -> 1;
                default -> 0;
            };
            playerData.worthiness += value;
            if (value != 0) {
                MythAndMagic.TASK_COMPLETED.trigger((ServerPlayerEntity) player);
            }
        }));

        // register commands for setting and getting worthiness
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("worthiness")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("set")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    int value = IntegerArgumentType.getInteger(context, "value");
                                                    PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                                    if (ExcaliburSwordItem.MIN_WORTHINESS <= value && value <= ExcaliburSwordItem.MAX_WORTHINESS) {
                                                        PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                                                        playerState.worthiness = value;
                                                        context.getSource().sendFeedback(() -> Text.translatable(
                                                                "command." + MOD_ID + ".worthiness_set_response",
                                                                playerState.worthiness), false);
                                                        return Command.SINGLE_SUCCESS;
                                                    } else {
                                                        throw new CommandException(Text.translatable("command." + MOD_ID + ".value_exception",
                                                                ExcaliburSwordItem.MIN_WORTHINESS, ExcaliburSwordItem.MAX_WORTHINESS));
                                                    }
                                                }))))
                        .then(CommandManager.literal("get")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                            PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                                            context.getSource().sendFeedback(() -> Text.translatable(
                                                    "command." + MOD_ID + ".worthiness_get_response",
                                                    playerState.worthiness), false);
                                            return playerState.worthiness;
                                        })))));
    }
}