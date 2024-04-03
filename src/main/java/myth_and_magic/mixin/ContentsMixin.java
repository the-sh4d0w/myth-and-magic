package myth_and_magic.mixin;

import myth_and_magic.datagen.MythAndMagicItemTagProvider;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookScreen.Contents.class)
public interface ContentsMixin {
    @Inject(at = @At("HEAD"), method = "create", cancellable = true)
    private static void create(ItemStack stack, CallbackInfoReturnable<BookScreen.Contents> info) {
        // why???
        if (stack.isIn(MythAndMagicItemTagProvider.SPELL_BOOKS)) {
            info.setReturnValue(new BookScreen.WrittenBookContents(stack));
        }
    }
}