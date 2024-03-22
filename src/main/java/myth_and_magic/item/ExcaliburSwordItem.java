package myth_and_magic.item;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.materials.MagicAlloyToolMaterial;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class ExcaliburSwordItem extends SwordItem {
    public static final int MIN_WORTHINESS = 0;
    public static final int MAX_WORTHINESS = 10;
    public static final int REQUIRED_WORTHINESS = 7;

    public ExcaliburSwordItem(Settings settings) {
        super(new MagicAlloyToolMaterial(), 5, -2f, settings.rarity(Rarity.EPIC));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip"));
        // show who the sword is bound to
        if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains("myth_and_magic.owner")) {
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid(MythAndMagic.MOD_ID + ".owner"));
                if (player != null) {
                    tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip_bound").append(
                            ((MutableText) player.getName()).formatted(Formatting.GOLD)));
                    if (!player.getName().toString().equals(itemStack.getOrCreateNbt().getString(MythAndMagic.MOD_ID + ".player_name"))) {
                        NbtCompound nbtData = itemStack.getOrCreateNbt();
                        nbtData.putString(MythAndMagic.MOD_ID + ".player_name", player.getName().getString());
                        itemStack.setNbt(nbtData);
                    }
                    return;
                }
            }
            tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip_bound").append(
                    Text.literal(itemStack.getOrCreateNbt().getString(MythAndMagic.MOD_ID + ".player_name")).formatted(Formatting.GOLD)));
        }
    }
}