package myth_and_magic.mixin;

import myth_and_magic.MythAndMagic;
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
        if (player.getMainHandStack().isOf(MythAndMagic.EXCALIBUR)) {
            // check if player is owner
            if (!player.getMainHandStack().hasNbt()
                    || !player.getMainHandStack().getOrCreateNbt().contains("owner")
                    || !player.getMainHandStack().getOrCreateNbt().getUuid("owner").equals(player.getUuid())) {
                // cancel attack method; makes sure that the attack does nothing
                // TODO: figure out how to stop the animation and display text on screen
                if (player.getWorld().isClient()) {
                    ((PlayerEntity) player).sendMessage(Text.literal("You are not worthy."), true);
                }
                info.cancel();
            }
        }
    }
}