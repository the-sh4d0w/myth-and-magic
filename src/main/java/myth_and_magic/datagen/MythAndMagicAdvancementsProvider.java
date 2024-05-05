package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.criteria.*;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
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
    public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
        // some of this, especially the rune and infusion criteria and the wither task, is a bit cursed, but it works ¯\_(ツ)_/¯
        // myth advancements
        AdvancementEntry myth_root = Advancement.Builder.create()
                .display(
                        MythAndMagicItems.NARCISSUS_MIRROR,
                        Text.translatable("advancements.myth_and_magic.myth.beginning"),
                        Text.translatable("advancements.myth_and_magic.myth.beginning.desc"),
                        new Identifier(MythAndMagic.MOD_ID, "textures/gui/myth_background.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                ).criterion("myth/beginning", TaskCompletedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/beginning").toString());
        AdvancementEntry claimed_excalibur = Advancement.Builder.create().parent(myth_root)
                .display(
                        MythAndMagicItems.EXCALIBUR,
                        Text.translatable("advancements.myth_and_magic.myth.claimed_excalibur"),
                        Text.translatable("advancements.myth_and_magic.myth.claimed_excalibur.desc"),
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
                        Text.translatable("advancements.myth_and_magic.myth.called_excalibur"),
                        Text.translatable("advancements.myth_and_magic.myth.called_excalibur.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("myth/called_excalibur", ExcaliburCalledCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/called_excalibur").toString());
        AdvancementEntry craft_knight_statue = Advancement.Builder.create().parent(myth_root)
                .display(
                        MythAndMagicItems.KNIGHT_STATUE,
                        Text.translatable("advancements.myth_and_magic.myth.craft_knight_statue"),
                        Text.translatable("advancements.myth_and_magic.myth.craft_knight_statue.desc"),
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
                        Text.translatable("advancements.myth_and_magic.myth.knight_protect"),
                        Text.translatable("advancements.myth_and_magic.myth.knight_protect.desc"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                ).criterion("myth/knight_protect", KnightProtectionCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "myth/knight_protect").toString());
        // magic advancements
        AdvancementEntry magic_root = Advancement.Builder.create()
                .display(
                        Items.AMETHYST_SHARD,
                        Text.translatable("advancements.myth_and_magic.magic.beginning"),
                        Text.translatable("advancements.myth_and_magic.magic.beginning.desc"),
                        new Identifier(MythAndMagic.MOD_ID, "textures/gui/magic_background.png"),
                        AdvancementFrame.TASK,
                        false,
                        false,
                        false
                ).criterion("magic/beginning", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/beginning").toString());
        AdvancementEntry craft_rune_table = Advancement.Builder.create().parent(magic_root)
                .display(
                        MythAndMagicBlocks.RUNE_TABLE_ITEM,
                        Text.translatable("advancements.myth_and_magic.magic.craft_rune_table"),
                        Text.translatable("advancements.myth_and_magic.magic.craft_rune_table.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("magic/craft_rune_table", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "rune_table")))
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/craft_rune_table").toString());
        AdvancementEntry got_rune = Advancement.Builder.create().parent(craft_rune_table)
                .display(
                        MythAndMagicItems.FIRE_RUNE,
                        Text.translatable("advancements.myth_and_magic.magic.got_rune"),
                        Text.translatable("advancements.myth_and_magic.magic.got_rune.desc"),
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
                        Text.translatable("advancements.myth_and_magic.magic.used_heal_rune"),
                        Text.translatable("advancements.myth_and_magic.magic.used_heal_rune.desc"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("magic/used_heal_rune", HealRuneUsedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/used_heal_rune").toString());
        AdvancementEntry craft_infusion_table = Advancement.Builder.create().parent(magic_root)
                .display(
                        MythAndMagicBlocks.INFUSION_TABLE_BLOCK,
                        Text.translatable("advancements.myth_and_magic.magic.craft_infusion_table"),
                        Text.translatable("advancements.myth_and_magic.magic.craft_infusion_table.desc"),
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
                        Text.translatable("advancements.myth_and_magic.magic.got_level_phial"),
                        Text.translatable("advancements.myth_and_magic.magic.got_level_phial.desc"),
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
                        Text.translatable("advancements.myth_and_magic.magic.upgrade_enchantment"),
                        Text.translatable("advancements.myth_and_magic.magic.upgrade_enchantment.desc"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .criterion("magic/upgrade_enchantment", EnchantmentUpgradedCriterion.Conditions.create())
                .build(consumer, new Identifier(MythAndMagic.MOD_ID, "magic/upgrade_enchantment").toString());
        Advancement.Builder.create().parent(craft_infusion_table)
                .display(
                        MythAndMagicItems.GROW_SPELL,
                        Text.translatable("advancements.myth_and_magic.magic.got_spell"),
                        Text.translatable("advancements.myth_and_magic.magic.got_spell.desc"),
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
                        Text.translatable("advancements.myth_and_magic.tasks.killed_wither"),
                        Text.translatable("advancements.myth_and_magic.tasks.killed_wither.desc"),
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