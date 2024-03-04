package myth_and_magic.block.entity;

import myth_and_magic.MythAndMagic;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class MagicTableBlockEntity extends BlockEntity {
    public MagicTableBlockEntity(BlockPos pos, BlockState state) {
        super(MythAndMagic.MAGIC_TABLE_BLOCK_ENTITY, pos, state);
    }
}
