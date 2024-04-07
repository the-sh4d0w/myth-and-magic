package myth_and_magic.mixin;

import myth_and_magic.enchantment.MythAndMagicEnchantments;
import myth_and_magic.enchantment.XPEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;dropXp()V"), method = "drop", cancellable = true)
    public void drop(DamageSource source, CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (source.getAttacker() != null && source.getAttacker().isPlayer()) {
            PlayerEntity player = (PlayerEntity) source.getAttacker();
            ItemStack weapon = player.getMainHandStack();
            if (EnchantmentHelper.getLevel(MythAndMagicEnchantments.XP_ENCHANTMENT, weapon) > 0) {
                if (entity.getWorld() instanceof ServerWorld && !entity.isExperienceDroppingDisabled() && (entity.shouldAlwaysDropXp() || entity.playerHitTimer > 0 && entity.shouldDropXp() && entity.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_LOOT))) {
                    ExperienceOrbEntity.spawn((ServerWorld) entity.getWorld(), entity.getPos(), entity.getXpToDrop() * XPEnchantment.multiply_value);
                }
                info.cancel();
            }
        }
    }
}