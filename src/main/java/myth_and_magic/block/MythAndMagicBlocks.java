package myth_and_magic.block;

import myth_and_magic.MythAndMagic;
import myth_and_magic.block.entity.InfusionTableBlockEntity;
import myth_and_magic.block.entity.RuneTableBlockEntity;
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
    public static final Block RUNE_TABLE_BLOCK = register("rune_table", new RuneTableBlock(FabricBlockSettings.create()));
    public static final BlockEntityType<RuneTableBlockEntity> RUNE_TABLE_BLOCK_ENTITY = register("rune_table_entity",
            FabricBlockEntityTypeBuilder.create(RuneTableBlockEntity::new, RUNE_TABLE_BLOCK).build());
    public static final BlockItem RUNE_TABLE_ITEM = register("rune_table", new BlockItem(RUNE_TABLE_BLOCK,
            new FabricItemSettings().maxCount(1)));
    public static final Block INFUSION_TABLE_BLOCK = register("infusion_table", new InfusionTableBlock(FabricBlockSettings.create()));
    public static final BlockEntityType<InfusionTableBlockEntity> INFUSION_TABLE_BLOCK_ENTITY = register("infusion_table_entity",
            FabricBlockEntityTypeBuilder.create(InfusionTableBlockEntity::new, INFUSION_TABLE_BLOCK).build());
    public static final BlockItem INFUSION_TABLE_BLOCK_ITEM = register("infusion_table", new BlockItem(INFUSION_TABLE_BLOCK,
            new FabricItemSettings().maxCount(1)));
    public static final Block EXCALIBUR_BLOCK = register("excalibur_block", new ExcaliburBlock(FabricBlockSettings.create()));
    public static final BlockItem EXCALIBUR_BLOCK_ITEM = register("excalibur_block", new BlockItem(EXCALIBUR_BLOCK,
            new FabricItemSettings()));

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