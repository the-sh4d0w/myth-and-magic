package myth_and_magic.mixin;

import myth_and_magic.item.MythAndMagicItems;
import myth_and_magic.item.TarnkappeArmorItem;
import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "discard")
    public void discard(CallbackInfo info) {
        Entity entity = (Entity) (Object) this;
        if (entity instanceof ItemEntity && ((ItemEntity) entity).getStack().isOf(MythAndMagicItems.EXCALIBUR)) {
            if (((ItemEntity) entity).getStack().getOrCreateNbt().contains("owner")) {
                UUID uuid = ((ItemEntity) entity).getStack().getOrCreateNbt().getUuid("owner");
                PlayerData playerData = StateSaverAndLoader.getPlayerState(entity.getWorld(), uuid);
                playerData.swordDestroyed = true;
                playerData.data = ((ItemEntity) entity).getStack().getNbt();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "shouldSpawnSprintingParticles", cancellable = true)
    public void shouldSpawnSprintingParticles(CallbackInfoReturnable<Boolean> info) {
        Entity entity = (Entity) (Object) this;
        for (ItemStack itemStack: entity.getArmorItems()) {
            if (itemStack.getItem() instanceof TarnkappeArmorItem) {
                info.setReturnValue(false);
            }
        }
    }
}