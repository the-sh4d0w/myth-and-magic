package myth_and_magic.mixin;

import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.Property;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin extends ForgingScreenHandlerMixin {
    // gods, I hate anvils
    @Shadow
    @Final
    private Property levelCost;

    @Inject(at = @At("HEAD"), method = "updateResult", cancellable = true)
    public void updateResult(CallbackInfo info) {
        if ((input.getStack(0).getItem().equals(MythAndMagicItems.MAGIC_GOLD_INGOT) && input.getStack(1).getItem().equals(MythAndMagicItems.MAGIC_IRON_INGOT))
                || (input.getStack(1).getItem().equals(MythAndMagicItems.MAGIC_GOLD_INGOT) && input.getStack(0).getItem().equals(MythAndMagicItems.MAGIC_IRON_INGOT))) {
            int amount = Math.min(input.getStack(0).getCount(), input.getStack(1).getCount());
            this.levelCost.set(10 * amount);
            output.setStack(0, new ItemStack(MythAndMagicItems.MAGIC_ALLOY_INGOT, amount));
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "onTakeOutput", cancellable = true)
    public void onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo info) {
        if ((input.getStack(0).getItem().equals(MythAndMagicItems.MAGIC_GOLD_INGOT) && input.getStack(1).getItem().equals(MythAndMagicItems.MAGIC_IRON_INGOT))
                || (input.getStack(1).getItem().equals(MythAndMagicItems.MAGIC_GOLD_INGOT) && input.getStack(0).getItem().equals(MythAndMagicItems.MAGIC_IRON_INGOT))) {
            int amount = Math.min(input.getStack(0).getCount(), input.getStack(1).getCount());
            if (!player.getAbilities().creativeMode) {
                player.addExperienceLevels(-this.levelCost.get());
            }
            input.setStack(0, new ItemStack(input.getStack(0).getItem(), input.getStack(0).getCount() - amount));
            input.setStack(1, new ItemStack(input.getStack(1).getItem(), input.getStack(1).getCount() - amount));
            info.cancel();
        }
    }
}