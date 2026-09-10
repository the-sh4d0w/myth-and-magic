package myth_and_magic.item;

import myth_and_magic.item.materials.MagicAlloyArmorMaterial;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
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
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.myth_and_magic.tarnkappe.tooltip"));
    }

    public static void changeHealthIfTarnkappe(LivingEntity user, EquipmentSlot slot, ItemStack previous, ItemStack current) {
        if (slot.isArmorSlot() && user.isAlive() && !previous.getItem().equals(current.getItem())) {
            EntityAttributeInstance attribute = user.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (current.getItem().equals(MythAndMagicItems.TARNKAPPE)) {
                attribute.setBaseValue(TarnkappeArmorItem.REDUCED_HEALTH);
                user.setHealth(TarnkappeArmorItem.REDUCED_HEALTH);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false));
            } else if (previous.getItem().equals(MythAndMagicItems.TARNKAPPE)) {
                attribute.setBaseValue(TarnkappeArmorItem.NORMAL_HEALTH);
                user.setHealth(TarnkappeArmorItem.NORMAL_HEALTH);
                user.removeStatusEffect(StatusEffects.INVISIBILITY);
            }
        }
    }
}