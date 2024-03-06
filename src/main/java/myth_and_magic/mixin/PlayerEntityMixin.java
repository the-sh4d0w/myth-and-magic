package myth_and_magic.mixin;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method = "attack", cancellable = true)
    public void attack(Entity target, CallbackInfo info) {
        LivingEntity player = (LivingEntity) (Object) this;
        // check if using excalibur
        if (player.getMainHandStack().isOf(MythAndMagicItems.EXCALIBUR)) {
            // check if player is owner
            if (!player.getMainHandStack().hasNbt() || !player.getMainHandStack().getOrCreateNbt().contains(MythAndMagic.MOD_ID + ".owner")
                    || !player.getMainHandStack().getOrCreateNbt().getUuid(MythAndMagic.MOD_ID + ".owner").equals(player.getUuid())) {
                if (player.getWorld().isClient()) {
                    // cancel attack method; makes sure that the attack does nothing
                    if (player.getMainHandStack().getOrCreateNbt().contains(MythAndMagic.MOD_ID + ".owner")
                            && !player.getMainHandStack().getOrCreateNbt().getUuid(MythAndMagic.MOD_ID + ".owner").equals(player.getUuid())) {
                        ((PlayerEntity) player).sendMessage(Text.translatable("item.myth_and_magic.excalibur.bound_other"), true);
                    } else {
                        ((PlayerEntity) player).sendMessage(Text.translatable("item.myth_and_magic.excalibur.bound_none"), true);
                    }
                }
                info.cancel();
            }
        }
    }
}