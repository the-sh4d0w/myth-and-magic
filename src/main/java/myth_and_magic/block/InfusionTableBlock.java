package myth_and_magic.block;

import myth_and_magic.block.entity.InfusionTableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InfusionTableBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0, 11, 0, 16, 14, 16);
    public static final VoxelShape LEG_ONE_SHAPE = Block.createCuboidShape(1, 0, 1, 3, 11, 3);
    public static final VoxelShape LEG_TWO_SHAPE = Block.createCuboidShape(13, 0, 1, 15, 11, 3);
    public static final VoxelShape LEG_THREE_SHAPE = Block.createCuboidShape(13, 0, 13, 15, 11, 15);
    public static final VoxelShape LEG_FOUR_SHAPE = Block.createCuboidShape(1, 0, 13, 3, 11, 15);
    public static final VoxelShape SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_ONE_SHAPE, LEG_TWO_SHAPE, LEG_THREE_SHAPE, LEG_FOUR_SHAPE);

    public InfusionTableBlock(Settings settings) {
        super(settings.strength(1));
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.WOOD;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new InfusionTableBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = ((InfusionTableBlockEntity) world.getBlockEntity(pos));
            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InfusionTableBlockEntity) {
                ItemScatterer.spawn(world, pos, ((InfusionTableBlockEntity) blockEntity));
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, MythAndMagicBlocks.INFUSION_TABLE_BLOCK_ENTITY,
                (world1, pos, state1, blockEntity) -> blockEntity.tick(world1, pos, state1));
    }
}