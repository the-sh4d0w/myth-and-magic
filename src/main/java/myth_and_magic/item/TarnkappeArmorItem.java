package myth_and_magic.item;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.materials.MagicAlloyArmorMaterial;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.List;

public class TarnkappeArmorItem extends ArmorItem {
    public static final float REDUCED_HEALTH = 6.0f;
    // assuming that 9 hearts is always standard health; TODO: fix
    public static final float NORMAL_HEALTH = 18.0f;

    public TarnkappeArmorItem(Settings settings) {
        super(new MagicAlloyArmorMaterial(), Type.HELMET, settings.rarity(Rarity.EPIC));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".tarnkappe.tooltip"));
    }
}