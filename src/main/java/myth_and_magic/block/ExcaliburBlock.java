package myth_and_magic.block;

import myth_and_magic.MythAndMagic;
import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import myth_and_magic.item.ExcaliburSwordItem;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ExcaliburBlock extends Block {
    // why, Minecraft, why?
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    // shape for north and south
    public static final VoxelShape BLADE_NORTH_SOUTH = Block.createCuboidShape(5.5, 0, 7.5, 10.5, 4, 8.5);
    public static final VoxelShape GUARD_ONE_NORTH_SOUTH = Block.createCuboidShape(2.5, 4, 7.5, 13.5, 5, 8.5);
    public static final VoxelShape GUARD_TWO_NORTH_SOUTH = Block.createCuboidShape(1.5, 5, 7.5, 14.5, 6, 8.5);
    public static final VoxelShape GUARD_THREE_NORTH_SOUTH = Block.createCuboidShape(2.5, 6, 7.5, 13.5, 7, 8.5);
    public static final VoxelShape GRIP_NORTH_SOUTH = Block.createCuboidShape(6.5, 7, 7.5, 9.5, 11, 8.5);
    public static final VoxelShape POMMEL_ONE_NORTH_SOUTH = Block.createCuboidShape(6.5, 11, 7.5, 9.5, 16, 8.5);
    public static final VoxelShape POMMEL_TWO_NORTH_SOUTH = Block.createCuboidShape(9.5, 12, 7.5, 10.5, 15, 8.5);
    public static final VoxelShape POMMEL_THREE_NORTH_SOUTH = Block.createCuboidShape(5.5, 12, 7.5, 6.5, 15, 8.5);
    public static final VoxelShape SHAPE_NORTH_SOUTH = VoxelShapes.union(BLADE_NORTH_SOUTH, GUARD_ONE_NORTH_SOUTH, GUARD_TWO_NORTH_SOUTH,
            GUARD_THREE_NORTH_SOUTH, GRIP_NORTH_SOUTH, POMMEL_ONE_NORTH_SOUTH, POMMEL_TWO_NORTH_SOUTH, POMMEL_THREE_NORTH_SOUTH);
    // shape for east and west
    public static final VoxelShape BLADE_EAST_WEST = Block.createCuboidShape(7.5, 0, 5.5, 8.5, 4, 10.5);
    public static final VoxelShape GUARD_ONE_EAST_WEST = Block.createCuboidShape(7.5, 4, 2.5, 8.5, 5, 13.5);
    public static final VoxelShape GUARD_TWO_EAST_WEST = Block.createCuboidShape(7.5, 5, 1.5, 8.5, 6, 14.5);
    public static final VoxelShape GUARD_THREE_EAST_WEST = Block.createCuboidShape(7.5, 6, 2.5, 8.5, 7, 13.5);
    public static final VoxelShape GRIP_EAST_WEST = Block.createCuboidShape(7.5, 7, 6.5, 8.5, 11, 9.5);
    public static final VoxelShape POMMEL_ONE_EAST_WEST = Block.createCuboidShape(7.5, 11, 6.5, 8.5, 16, 9.5);
    public static final VoxelShape POMMEL_TWO_EAST_WEST = Block.createCuboidShape(7.5, 12, 5.5, 8.5, 15, 6.5);
    public static final VoxelShape POMMEL_THREE_EAST_WEST = Block.createCuboidShape(7.5, 12, 9.5, 8.5, 15, 10.5);
    public static final VoxelShape SHAPE_EAST_WEST = VoxelShapes.union(BLADE_EAST_WEST, GUARD_ONE_EAST_WEST, GUARD_TWO_EAST_WEST,
            GUARD_THREE_EAST_WEST, GRIP_EAST_WEST, POMMEL_ONE_EAST_WEST, POMMEL_TWO_EAST_WEST, POMMEL_THREE_EAST_WEST);

    public ExcaliburBlock(Settings settings) {
        super(settings.strength(-1, 3600000));
        setDefaultState(getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH, SOUTH -> SHAPE_NORTH_SOUTH;
            case EAST, WEST -> SHAPE_EAST_WEST;
            default -> VoxelShapes.fullCube();
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            PlayerData playerData = StateSaverAndLoader.getPlayerState(world, player.getUuid());
            if (!playerData.boundSword) {
                if (playerData.worthiness >= ExcaliburSwordItem.REQUIRED_WORTHINESS) {
                    world.removeBlock(pos, false);
                    ExcaliburSwordItem.createSword((ServerPlayerEntity) player, true);
                    MythAndMagic.EXCALIBUR_CLAIMED.trigger((ServerPlayerEntity) player);
                } else {
                    player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.not_worthy"), true);
                }
            } else {
                player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.already_bound"), true);
            }
        }
        return ActionResult.SUCCESS;
    }
}