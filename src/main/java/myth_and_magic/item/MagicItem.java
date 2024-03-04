package myth_and_magic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class MagicItem extends Item {
    // class for all magic items (currently just adds glint)
    public MagicItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
}