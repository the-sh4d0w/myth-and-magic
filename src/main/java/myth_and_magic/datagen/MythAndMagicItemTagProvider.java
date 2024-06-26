package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class MythAndMagicItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> RUNE_PROJECTILES = TagKey.of(RegistryKeys.ITEM, new Identifier(MythAndMagic.MOD_ID, "rune_projectiles"));
    public static final TagKey<Item> SPELL_BOOKS = TagKey.of(RegistryKeys.ITEM, new Identifier(MythAndMagic.MOD_ID, "spell_books"));

    public MythAndMagicItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(RUNE_PROJECTILES)
                .add(MythAndMagicItems.FIRE_RUNE)
                .add(MythAndMagicItems.ICE_RUNE)
                .add(MythAndMagicItems.HEAL_RUNE)
                .add(MythAndMagicItems.LIGHTNING_RUNE)
                .add(MythAndMagicItems.EXPLOSIVE_RUNE);
        getOrCreateTagBuilder(SPELL_BOOKS)
                .add(MythAndMagicItems.GROW_SPELL)
                .add(MythAndMagicItems.HOME_SPELL);
        // TODO: maybe add by tag?
        getOrCreateTagBuilder(ItemTags.BOOKSHELF_BOOKS)
                .add(MythAndMagicItems.GROW_SPELL)
                .add(MythAndMagicItems.HOME_SPELL);
        getOrCreateTagBuilder(ItemTags.LECTERN_BOOKS)
                .add(MythAndMagicItems.GROW_SPELL)
                .add(MythAndMagicItems.HOME_SPELL);
    }
}