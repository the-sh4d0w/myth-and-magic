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

    public ExcaliburSwordItem(Settings settings) {
        super(new MagicAlloyToolMaterial(), 5, -2f, settings.rarity(Rarity.EPIC));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip"));
        // show who the sword is bound to
        if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains("myth_and_magic.owner")) {
            PlayerEntity player = world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid(MythAndMagic.MOD_ID + ".owner"));
            if (player != null) {
                tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip_bound").append(
                        ((MutableText) player.getName()).formatted(Formatting.GOLD)));
                if (!player.getName().toString().equals(itemStack.getOrCreateNbt().getString(MythAndMagic.MOD_ID + ".player_name"))) {
                    NbtCompound nbtData = itemStack.getOrCreateNbt();
                    nbtData.putString(MythAndMagic.MOD_ID + ".player_name", player.getName().getString());
                    itemStack.setNbt(nbtData);
                }
            } else {
                tooltip.add(Text.translatable("item." + MythAndMagic.MOD_ID + ".excalibur.tooltip_bound").append(
                        Text.literal(itemStack.getOrCreateNbt().getString(MythAndMagic.MOD_ID + ".player_name")).formatted(Formatting.GOLD)));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // TODO: figure out what makes someone worthy
        if (player.getStackInHand(hand).hasNbt()) {
            if (player.getStackInHand(hand).getOrCreateNbt().contains(MythAndMagic.MOD_ID + ".owner")
                    && !player.getStackInHand(hand).getOrCreateNbt().getUuid(MythAndMagic.MOD_ID + ".owner").equals(player.getUuid())) {
                player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.bound_other"), true);
            }
            return TypedActionResult.fail(player.getStackInHand(hand));
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putUuid(MythAndMagic.MOD_ID + ".owner", player.getUuid());
            nbtData.putString(MythAndMagic.MOD_ID + ".player_name", player.getName().getString());
            player.getStackInHand(hand).setNbt(nbtData);
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
            if (!world.isClient()) {
                MythAndMagic.EXCALIBUR_CLAIMED.trigger((ServerPlayerEntity) player);
            }
            return TypedActionResult.success(player.getStackInHand(hand));
        }
    }
}