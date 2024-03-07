package myth_and_magic.datagen;

import myth_and_magic.ExcaliburClaimedCriterion;
import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeCraftedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class MythAndMagicAdvancementsProvider extends FabricAdvancementProvider {
    public MythAndMagicAdvancementsProvider(FabricDataOutput dataGenerator) {
        super(dataGenerator);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        // TODO: add advancements
        // - root; figure out what
        //  -> create magic table
        //   -> create magic iron
        //    -> craft excalibur
        //     -> claim excalibur
        //   -> create magic gold
        // TODO: add translations
        Advancement root = Advancement.Builder.create()
                .display(
                        Items.AMETHYST_SHARD,
                        Text.translatable("advancements.myth_and_magic.root"),
                        Text.translatable("advancements.myth_and_magic.root.desc"),
                        new Identifier("textures/gui/advancements/backgrounds/adventure.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("root", InventoryChangedCriterion.Conditions.items(Items.AMETHYST_SHARD))
                .build(consumer, MythAndMagic.MOD_ID + "/root");
        Advancement craftMagicTable = Advancement.Builder.create().parent(root)
                .display(
                        MythAndMagicBlocks.MAGIC_TABLE_ITEM,
                        Text.translatable("advancements.myth_and_magic.craft_magic_table"),
                        Text.translatable("advancements.myth_and_magic.craft_magic_table.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("craft_magic_table", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "magic_table")))
                .build(consumer, MythAndMagic.MOD_ID + "/craft_magic_table");
        Advancement getMagicIron = Advancement.Builder.create().parent(craftMagicTable)
                .display(
                        MythAndMagicItems.MAGIC_IRON_INGOT,
                        Text.translatable("advancements.myth_and_magic.got_magic_iron"),
                        Text.translatable("advancements.myth_and_magic.got_magic_iron.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("got_magic_iron", InventoryChangedCriterion.Conditions.items(MythAndMagicItems.MAGIC_IRON_INGOT))
                .build(consumer, MythAndMagic.MOD_ID + "/got_magic_iron");
        Advancement craftExcalibur = Advancement.Builder.create().parent(getMagicIron)
                .display(
                        MythAndMagicItems.EXCALIBUR,
                        Text.translatable("advancements.myth_and_magic.craft_excalibur"),
                        Text.translatable("advancements.myth_and_magic.craft_excalibur.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("craft_excalibur", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "excalibur"))
                ).build(consumer, MythAndMagic.MOD_ID + "/craft_excalibur");
        Advancement.Builder.create().parent(craftExcalibur)
                .display(
                        MythAndMagicItems.EXCALIBUR,
                        Text.literal("The True King"),
                        Text.literal("Claim excalibur."),
                        null,
                        AdvancementFrame.CHALLENGE,
                        true,
                        true,
                        false
                ).criterion("claimed_excalibur", ExcaliburClaimedCriterion.Conditions.create()
                ).build(consumer, MythAndMagic.MOD_ID + "/claimed_excalibur");
        Advancement getMagicGold = Advancement.Builder.create().parent(craftMagicTable)
                .display(
                        MythAndMagicItems.MAGIC_GOLD_INGOT,
                        Text.translatable("advancements.myth_and_magic.got_magic_gold"),
                        Text.translatable("advancements.myth_and_magic.got_magic_gold.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                ).criterion("got_magic_gold", InventoryChangedCriterion.Conditions.items(MythAndMagicItems.MAGIC_GOLD_INGOT))
                .build(consumer, MythAndMagic.MOD_ID + "/got_magic_gold");
    }
}