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
import net.minecraft.world.World;

import java.util.function.Predicate;

public class MageStaffItem extends RangedWeaponItem {
    public MageStaffItem(Settings settings, int damage) {
        super(settings.maxCount(1).maxDamage(damage));
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return stack -> stack.isIn(MythAndMagicItemTagProvider.RUNE_PROJECTILES);
    }

    @Override
    public int getRange() {
        return 15;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ItemStack projectile = user.getProjectileType(itemStack);
        if (projectile.isEmpty() || !projectile.isIn(MythAndMagicItemTagProvider.RUNE_PROJECTILES)) {
            return TypedActionResult.fail(itemStack);
        } else {
            user.setCurrentHand(hand);
            if (!world.isClient()) {
                RuneProjectileEntity runeProjectileEntity = new RuneProjectileEntity(user, world, projectile, user);
                runeProjectileEntity.setItem(projectile);
                runeProjectileEntity.setVelocity(user, user.getPitch(), user.getYaw(), user.getRoll(), 3f, 0f);
                world.spawnEntity(runeProjectileEntity);
            }
            world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE,
                    SoundCategory.NEUTRAL, 1f, 1f);
            projectile.decrement(1);
            if (projectile.isEmpty()) {
                user.getInventory().removeOne(projectile);
            }
            itemStack.damage(1, user, (p) -> p.sendToolBreakStatus(hand));
            user.getItemCooldownManager().set(this, 40);
            return TypedActionResult.success(itemStack);
        }
    }
}