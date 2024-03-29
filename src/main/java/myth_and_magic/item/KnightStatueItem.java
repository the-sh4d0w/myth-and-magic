package myth_and_magic.item;

import myth_and_magic.entity.KnightEntity;
import myth_and_magic.entity.MythAndMagicEntities;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class KnightStatueItem extends Item {
    public KnightStatueItem(Settings settings) {
        super(settings.maxCount(16));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        } else {
            ItemStack itemStack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            Direction direction = context.getSide();
            KnightEntity knight;
            PlayerEntity player = context.getPlayer();
            if ((knight = MythAndMagicEntities.KNIGHT.spawnFromItemStack((ServerWorld) world, itemStack, player,
                    blockPos, SpawnReason.SPAWN_EGG, true, direction == Direction.UP)) != null) {
                itemStack.decrement(1);
                if (itemStack.getCount() == 0 && !player.getAbilities().creativeMode) {
                    player.getInventory().removeOne(itemStack);
                }
                world.emitGameEvent(player, GameEvent.ENTITY_PLACE, blockPos);
                knight.setOwnerUuid(player.getUuid());
                knight.setStatuePosition(blockPos);
                knight.setStatueYaw(context.getHorizontalPlayerFacing().getOpposite().getHorizontal() * 90);
                knight.snapToStatuePosition();
            }
            return ActionResult.CONSUME;
        }
    }
}