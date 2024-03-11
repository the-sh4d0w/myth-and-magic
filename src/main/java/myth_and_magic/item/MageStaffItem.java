package myth_and_magic.item;

import myth_and_magic.datagen.MythAndMagicItemTagProvider;
import myth_and_magic.entity.RuneProjectileEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class MageStaffItem extends RangedWeaponItem {
    public MageStaffItem(Settings settings) {
        super(settings.maxCount(1).maxDamage(150));
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.isIn(MythAndMagicItemTagProvider.RUNE_PROJECTILES);
    }

    @Override
    public int getRange() {
        return 20;
    }

    public static float getPullProgress(int useTicks) {
        float f = (float) useTicks / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 60000;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        ItemStack itemStack = user.getProjectileType(stack);
        if (!itemStack.isEmpty()) {
            int i = this.getMaxUseTime(stack) - remainingUseTicks;
            float f = getPullProgress(i);
            if (f == 1f) {
                if (!world.isClient()) {
                    RuneProjectileEntity runeProjectileEntity = new RuneProjectileEntity(user, world, itemStack);
                    runeProjectileEntity.setItem(itemStack);
                    runeProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), 2f, 0f);
                    world.spawnEntity(runeProjectileEntity);
                }
                world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE,
                        SoundCategory.NEUTRAL, 1f, 1f);
                itemStack.decrement(1);
                if (itemStack.isEmpty()) {
                    ((PlayerEntity) user).getInventory().removeOne(itemStack);
                }
                stack.damage(1, user, (p) -> {
                    p.sendToolBreakStatus(user.getActiveHand());
                });
            }
        }
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack projectile = user.getProjectileType(itemStack);
        if (projectile.isEmpty() || projectile.isOf(Items.ARROW)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        }
    }
}