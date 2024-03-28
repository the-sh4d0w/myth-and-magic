package myth_and_magic.block;

import myth_and_magic.block.entity.InfusionTableBlockEntity;
import myth_and_magic.datagen.MythAndMagicBlockTagProvider;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class InfusionTableBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0, 11, 0, 16, 14, 16);
    public static final VoxelShape LEG_ONE_SHAPE = Block.createCuboidShape(1, 0, 1, 3, 11, 3);
    public static final VoxelShape LEG_TWO_SHAPE = Block.createCuboidShape(13, 0, 1, 15, 11, 3);
    public static final VoxelShape LEG_THREE_SHAPE = Block.createCuboidShape(13, 0, 13, 15, 11, 15);
    public static final VoxelShape LEG_FOUR_SHAPE = Block.createCuboidShape(1, 0, 13, 3, 11, 15);
    public static final VoxelShape SHAPE = VoxelShapes.union(TOP_SHAPE, LEG_ONE_SHAPE, LEG_TWO_SHAPE, LEG_THREE_SHAPE, LEG_FOUR_SHAPE);
    public static final List<BlockPos> POWER_PROVIDER_OFFSETS = BlockPos.stream(-3, 0, -3, 3, 2, 3
    ).map(BlockPos::toImmutable).toList();
    public static final int MIN_POWER = 20;

    public InfusionTableBlock(Settings settings) {
        super(settings.strength(1));
    }

    public static int getPower(World world, BlockPos tablePos, BlockPos providerOffset) {
        // very much not optimal; should probably rework this
        if (world.getBlockState(tablePos.add(providerOffset)).isIn(MythAndMagicBlockTagProvider.INFUSION_POWER_PROVIDER)
                && world.getBlockState(tablePos.add(providerOffset.getX() / 2, providerOffset.getY(),
                providerOffset.getZ() / 2)).isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
            return world.getBlockState(tablePos.add(providerOffset)).get(CandleBlock.CANDLES);
        } else {
            return 0;
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        for (BlockPos blockPos : POWER_PROVIDER_OFFSETS) {
            if (random.nextInt(16) != 0 || InfusionTableBlock.getPower(world, pos, blockPos) == 0) {
                continue;
            }
            world.addParticle(ParticleTypes.ENCHANT, (double) pos.getX() + 0.5, (double) pos.getY() + 1.75,
                    (double) pos.getZ() + 0.5, (double) ((float) blockPos.getX() + random.nextFloat()) - 0.5,
                    (float) blockPos.getY() - random.nextFloat() - 1.0f, (double) ((float) blockPos.getZ() + random.nextFloat()) - 0.5);
        }
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