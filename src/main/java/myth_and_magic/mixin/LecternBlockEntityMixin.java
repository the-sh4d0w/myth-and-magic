package myth_and_magic.mixin;

import myth_and_magic.datagen.MythAndMagicItemTagProvider;
import net.minecraft.block.entity.LecternBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityMixin {

    @Inject(at = @At("HEAD"), method = "hasBook", cancellable = true)
    public void hasBook(CallbackInfoReturnable<Boolean> info) {
        // why is this necessary???
        LecternBlockEntity entity = (LecternBlockEntity) (Object) this;
        if (entity.getBook().isIn(MythAndMagicItemTagProvider.SPELL_BOOKS)) {
            info.setReturnValue(true);
        }
    }
}