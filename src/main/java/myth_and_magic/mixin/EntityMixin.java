package myth_and_magic.mixin;

import myth_and_magic.item.MythAndMagicItems;
import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("HEAD"), method = "discard")
    public void discard(CallbackInfo info) {
        ItemEntity entity = (ItemEntity) (Object) this;
        if (entity instanceof ItemEntity && entity.getStack().isOf(MythAndMagicItems.EXCALIBUR)) {
            if (entity.getStack().getOrCreateNbt().contains("owner")) {
                UUID uuid = entity.getStack().getOrCreateNbt().getUuid("owner");
                PlayerData playerData = StateSaverAndLoader.getPlayerState(entity.getWorld(), uuid);
                playerData.swordDestroyed = true;
                playerData.data = entity.getStack().getNbt();
            }
        }
    }
}