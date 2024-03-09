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
        Advancement.Builder.create().parent(root)
                .display(
                        MythAndMagicBlocks.RUNE_TABLE_ITEM,
                        Text.translatable("advancements.myth_and_magic.craft_rune_table"),
                        Text.translatable("advancements.myth_and_magic.craft_rune_table.desc"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .criterion("craft_rune_table", RecipeCraftedCriterion.Conditions.create(
                        new Identifier(MythAndMagic.MOD_ID, "rune_table")))
                .build(consumer, MythAndMagic.MOD_ID + "/craft_rune_table");
        Advancement.Builder.create().parent(root)
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
    }
}