package myth_and_magic.mixin;

import myth_and_magic.item.ExcaliburSwordItem;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V")
    public void init(ItemConvertible item, int count, CallbackInfo info) {
        if (item.asItem() instanceof ExcaliburSwordItem) {
            System.out.println("Excalibur!");
        }
    }
}
