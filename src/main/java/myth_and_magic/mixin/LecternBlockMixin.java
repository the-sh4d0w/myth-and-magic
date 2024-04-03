package myth_and_magic.mixin;

import myth_and_magic.datagen.MythAndMagicItemTagProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LecternBlock.class)
public class LecternBlockMixin {

    @Unique
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        // this is the only way I could get it to work; very much not optimal
        return LecternBlock.checkType(type, BlockEntityType.LECTERN,
                (world1, pos, state1, blockEntity) -> {
                    if (!world.isClient() && blockEntity.hasBook() && blockEntity.getBook().isIn(MythAndMagicItemTagProvider.SPELL_BOOKS)) {
                        Random random = world.getRandom();
                        if (random.nextInt(30) % 3 == 0) {
                            ((ServerWorld) world).spawnParticles(ParticleTypes.ENCHANT,
                                    (double) pos.getX() + 0.2 + ((float) random.nextInt(7)) / 10,
                                    (double) pos.getY() + 1.2 + ((float) random.nextInt(3)) / 10,
                                    (double) pos.getZ() + 0.2 + ((float) random.nextInt(7)) / 10, 1,
                                    0, 0, 0, 0);
                        }
                    }
                });
    }
}