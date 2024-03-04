package myth_and_magic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MagicIronIngot extends Item {
    public MagicIronIngot(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}