package myth_and_magic.block;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.entity.MagicTableBlockEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicBlocks {
    // maybe improve later
    public static final Block MAGIC_TABLE_BLOCK = register("magic_table",
            new MagicTableBlock(FabricBlockSettings.create().strength(4.0f).requiresTool().luminance(10)));
    public static final BlockEntityType<MagicTableBlockEntity> MAGIC_TABLE_BLOCK_ENTITY = register("magic_table_entity",
            FabricBlockEntityTypeBuilder.create(MagicTableBlockEntity::new, MAGIC_TABLE_BLOCK).build());
    public static final BlockItem MAGIC_TABLE_ITEM = register("magic_table", new BlockItem(MAGIC_TABLE_BLOCK,
            new FabricItemSettings().maxCount(1)));

    private static Block register(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MythAndMagic.MOD_ID, name), block);
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> blockEntity) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(MythAndMagic.MOD_ID, name), blockEntity);
    }

    private static BlockItem register(String name, BlockItem blockItem) {
        return Registry.register(Registries.ITEM, new Identifier(MythAndMagic.MOD_ID, name), blockItem);
    }

    public static void registerBlocks() {
        MythAndMagic.LOGGER.info("Registering Blocks for %s".formatted(MythAndMagic.MOD_ID));
    }
}