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
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
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
import net.minecraft.registry.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import myth_and_magic.item.ExcaliburSwordItem;

import java.util.List;

public class MythAndMagic implements ModInitializer {
    public static final String MOD_ID = "myth_and_magic";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // criteria; maybe too many?
    public static final ExcaliburClaimedCriterion EXCALIBUR_CLAIMED = Criteria.register(ExcaliburClaimedCriterion.ID.toString(), new ExcaliburClaimedCriterion());
    public static final ExcaliburCalledCriterion EXCALIBUR_CALLED = Criteria.register(ExcaliburCalledCriterion.ID.toString(), new ExcaliburCalledCriterion());
    public static final TaskCompletedCriterion TASK_COMPLETED = Criteria.register(TaskCompletedCriterion.ID.toString(), new TaskCompletedCriterion());
    public static final EnchantmentUpgradedCriterion ENCHANTMENT_UPGRADED = Criteria.register(EnchantmentUpgradedCriterion.ID.toString(), new EnchantmentUpgradedCriterion());
    public static final RecipeInfusionCriterion RECIPE_INFUSION = Criteria.register(RecipeInfusionCriterion.ID.toString(), new RecipeInfusionCriterion());
    public static final RecipeRuneCriterion RECIPE_RUNE = Criteria.register(RecipeRuneCriterion.ID.toString(), new RecipeRuneCriterion());
    public static final HealRuneUsedCriterion HEAL_RUNE_USED = Criteria.register(HealRuneUsedCriterion.ID.toString(), new HealRuneUsedCriterion());
    public static final KnightProtectionCriterion KNIGHT_PROTECT = Criteria.register(KnightProtectionCriterion.ID.toString(), new KnightProtectionCriterion());
    // network packet ids
    public static final Identifier CALL_SWORD_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "call_sword");
    public static Identifier MOVE_PACKET_ID = new Identifier(MythAndMagic.MOD_ID, "move");
    // advancements for worthiness tasks
    public static final List<String> tasks_two = List.of("minecraft:end/kill_dragon", "minecraft:adventure/kill_all_mobs",
            "myth_and_magic:tasks/kill_wither");
    public static final List<String> tasks_one = List.of("minecraft:story/cure_zombie_villager", "minecraft:adventure/hero_of_the_village",
            "minecraft:adventure/kill_a_mob", "myth_and_magic:magic/used_heal_rune");


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
                    ExcaliburSwordItem.callSword(player);
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
            PlayerData playerData = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
            int value = 0;
            if (tasks_two.contains(advancement.id().toString())) {
                value = 2;
            }
            if (tasks_one.contains(advancement.id().toString())) {
                value = 1;
            }
            playerData.worthiness += value;
            if (playerData.worthiness > ExcaliburSwordItem.MAX_WORTHINESS) {
                playerData.worthiness = ExcaliburSwordItem.MAX_WORTHINESS;
            }
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
                                                        PlayerData playerState = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
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
                                            PlayerData playerState = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
                                            context.getSource().sendFeedback(() -> Text.translatable(
                                                    "command." + MOD_ID + ".worthiness_get_response",
                                                    playerState.worthiness), false);
                                            return playerState.worthiness;
                                        })))));
        CommandRegistrationCallback.EVENT.register(
                ((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("excalibur")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("setDestroyed")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                            PlayerData playerData = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
                                            playerData.swordDestroyed = true;
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(CommandManager.literal("unbind")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            PlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                            PlayerData playerData = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
                                            playerData.boundSword = false;
                                            playerData.swordDestroyed = false;
                                            return Command.SINGLE_SUCCESS;
                                        })))
                        .then(CommandManager.literal("updateTasks")
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                        .executes(context -> {
                                            // not optimal
                                            ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                                            PlayerData playerData = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
                                            playerData.worthiness = 1;
                                            for (String advancementId : tasks_two) {
                                                AdvancementEntry advancement = player.getServer().getAdvancementLoader()
                                                        .get(Identifier.splitOn(advancementId, ':'));
                                                if (player.getAdvancementTracker().getProgress(advancement).isDone()) {
                                                    playerData.worthiness += 2;
                                                }
                                            }
                                            for (String advancementId : tasks_one) {
                                                AdvancementEntry advancement = player.getServer().getAdvancementLoader()
                                                        .get(Identifier.splitOn(advancementId, ':'));
                                                if (player.getAdvancementTracker().getProgress(advancement).isDone()) {
                                                    playerData.worthiness += 1;
                                                }
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        }))))));
    }
}