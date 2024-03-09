package myth_and_magic.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class ExcaliburBlock extends Block {
    public static final VoxelShape BLADE = Block.createCuboidShape(5.5, 0, 7.5, 10.5, 4, 8.5);
    public static final VoxelShape GUARD_ONE = Block.createCuboidShape(2.5, 4, 7.5, 13.5, 5, 8.5);
    public static final VoxelShape GUARD_TWO = Block.createCuboidShape(1.5, 5, 7.5, 14.5, 6, 8.5);
    public static final VoxelShape GUARD_THREE = Block.createCuboidShape(2.5, 6, 7.5, 13.5, 7, 8.5);
    public static final VoxelShape GRIP = Block.createCuboidShape(6.5, 7, 7.5, 9.5, 11, 8.5);
    public static final VoxelShape POMMEL_ONE = Block.createCuboidShape(6.5, 11, 7.5, 9.5, 16, 8.5);
    public static final VoxelShape POMMEL_TWO = Block.createCuboidShape(9.5, 12, 7.5, 10.5, 15, 8.5);
    public static final VoxelShape POMMEL_THREE = Block.createCuboidShape(5.5, 12, 7.5, 6.5, 15, 8.5);
    public static final VoxelShape SHAPE = VoxelShapes.union(BLADE, GUARD_ONE, GUARD_TWO, GUARD_THREE, GRIP, POMMEL_ONE, POMMEL_TWO, POMMEL_THREE);

    public ExcaliburBlock(Settings settings) {
        super(settings);
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
}
