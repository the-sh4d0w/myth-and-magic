package myth_and_magic.datagen;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.MythAndMagicBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class MythAndMagicBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> INFUSION_POWER_PROVIDER = TagKey.of(RegistryKeys.BLOCK, new Identifier(MythAndMagic.MOD_ID, "infusion_power_provider"));

    public MythAndMagicBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).add(MythAndMagicBlocks.RUNE_TABLE_BLOCK);
        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).add(MythAndMagicBlocks.RUNE_TABLE_BLOCK);
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE).add(MythAndMagicBlocks.INFUSION_TABLE_BLOCK);
        getOrCreateTagBuilder(INFUSION_POWER_PROVIDER).add(Blocks.CANDLE);
    }
}