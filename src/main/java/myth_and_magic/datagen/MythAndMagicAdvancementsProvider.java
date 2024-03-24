package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.criteria.*;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MythAndMagicAdvancementsProvider extends FabricAdvancementProvider {
    public MythAndMagicAdvancementsProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // some of this, especially the rune and infusion criteria and the wither task, is a bit cursed, but it works ¯\_(ツ)_/¯
        // myth advancements
        Advancement myth_root = Advancement.Builder.create()
                .display(
                        MythAndMagicItems.NARCISSUS_MIRROR,
                        Text.literal("Myth"),
                        Text.literal("A mythical beginning."),
                        new Identifier(MythAndMagic.MOD_ID, "textures/gui/myth_background.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                ).criterion("myth/beginning", TaskCompletedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/beginning").toString());
        Advancement claimed_excalibur = Advancement.Builder.create().parent(myth_root)
                .display(
                        MythAndMagicItems.EXCALIBUR,
                        Text.literal("The True King"),
                        Text.literal("Claim excalibur."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                ).criterion("myth/claimed_excalibur", ExcaliburClaimedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/claimed_excalibur").toString());
        Advancement.Builder.create().parent(claimed_excalibur)
                .display(
                        MythAndMagicItems.EXCALIBUR,
                        Text.literal("Return of the King?"),
                        Text.literal("Call excalibur."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                ).criterion("myth/called_excalibur", ExcaliburCalledCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/called_excalibur").toString());
        Advancement craft_knight_statue = Advancement.Builder.create().parent(myth_root)
                .display(
                        MythAndMagicItems.KNIGHT_STATUE,
                        Text.literal("Knightly something"),
                        Text.literal("Craft knight statue."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("myth/craft_knight_statue", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "knight_statue")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/craft_knight_statue").toString());
        Advancement.Builder.create().parent(craft_knight_statue)
                .display(
                        MythAndMagicItems.KNIGHT_STATUE,
                        Text.literal("Knightly protection?"),
                        Text.literal("Let a knight protect you."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("myth/knight_protect", KnightProtectionCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/knight_protect").toString());
        // magic advancements
        Advancement magic_root = Advancement.Builder.create()
                .display(
                        Items.AMETHYST_SHARD,
                        Text.literal("Magic"),
                        Text.literal("A magical beginning."),
                        new Identifier(MythAndMagic.MOD_ID, "textures/gui/magic_background.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                ).criterion("magic/beginning", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/beginning").toString());
        Advancement craft_rune_table = Advancement.Builder.create().parent(magic_root)
                .display(
                        MythAndMagicBlocks.RUNE_TABLE_ITEM,
                        Text.literal("Something funny 1"),
                        Text.literal("Craft a rune table."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/craft_rune_table", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "rune_table")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/craft_rune_table").toString());
        Advancement got_rune = Advancement.Builder.create().parent(craft_rune_table)
                .display(
                        MythAndMagicItems.FIRE_RUNE,
                        Text.literal("Rune something"),
                        Text.literal("Make a rune."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/got_rune", RecipeRuneCriterion.Conditions.create(new Identifier(MythAndMagic.MOD_ID, "fire_rune"),
                        new Identifier(MythAndMagic.MOD_ID, "ice_rune"), new Identifier(MythAndMagic.MOD_ID, "heal_rune"),
                        new Identifier(MythAndMagic.MOD_ID, "lightning_rune"), new Identifier(MythAndMagic.MOD_ID, "explosive_rune")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/got_rune").toString());
        Advancement.Builder.create().parent(got_rune)
                .display(
                        MythAndMagicItems.HEAL_RUNE,
                        Text.literal("Heal rune stuff"),
                        Text.literal("Heal a player with a rune."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/used_heal_rune", HealRuneUsedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/used_heal_rune").toString());
        Advancement craft_infusion_table = Advancement.Builder.create().parent(magic_root)
                .display(
                        MythAndMagicBlocks.INFUSION_TABLE_BLOCK,
                        Text.literal("Something funny 2"),
                        Text.literal("Craft an infusion table."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/craft_infusion_table", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "infusion_table")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/craft_infusion_table").toString());
        Advancement.Builder.create().parent(craft_infusion_table)
                .display(
                        MythAndMagicItems.LEVEL_PHIAL,
                        Text.literal("Level Down"),
                        Text.literal("Infuse a glass phial with magic to create a level phial."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/got_level_phial", RecipeInfusionCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "level_phial")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/got_level_phial").toString());
        Advancement.Builder.create().parent(craft_infusion_table)
                .display(
                        Items.ENCHANTED_BOOK,
                        Text.literal("Upgrades"),
                        Text.literal("Upgrade an enchantment."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/upgrade_enchantment", EnchantmentUpgradedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/upgrade_enchantment").toString());
        Advancement.Builder.create().parent(craft_infusion_table)
                .display(
                        MythAndMagicItems.GROW_SPELL,
                        Text.literal("Spell something"),
                        Text.literal("Create a spell."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/got_spell", RecipeInfusionCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "cresco_spell"),
                        new Identifier(MythAndMagic.MOD_ID, "domicilium_spell")
                ))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/got_spell").toString());
        // tasks advancements
        Advancement.Builder.create()
                .display(
                        Items.NETHER_STAR,
                        Text.literal("Kill the Wither"),
                        Text.literal("This is used to track the worthiness task."),
                        new Identifier(MythAndMagic.MOD_ID, "textures/gui/tasks_background.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        true
                ).criterion("tasks/killed_wither", OnKilledCriterion.Conditions.createPlayerKilledEntity(
                        EntityPredicate.Builder.create().type(EntityType.WITHER)))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "tasks/kill_wither").toString());
    }
}