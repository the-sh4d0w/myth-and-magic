package myth_and_magic.item;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.materials.MagicAlloyToolMaterial;
import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

public class ExcaliburSwordItem extends SwordItem {
    public static final int MIN_WORTHINESS = 0;
    public static final int MAX_WORTHINESS = 10;
    public static final int REQUIRED_WORTHINESS = 7;

    public ExcaliburSwordItem(Settings settings) {
        super(new MagicAlloyToolMaterial(), 5, -2f, settings.rarity(Rarity.EPIC));
    }

    public static void createSword(ServerPlayerEntity player) {
        createSword(player, false);
    }

    public static void createSword(ServerPlayerEntity player, boolean newSword) {
        PlayerData playerData = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
        ItemStack itemStack = MythAndMagicItems.EXCALIBUR.getDefaultStack();
        NbtCompound nbtData;
        if (playerData.data.isEmpty() || newSword) {
            nbtData = new NbtCompound();
            nbtData.putUuid("owner", player.getUuid());
            nbtData.putString("player_name", player.getName().getString());
        } else {
            nbtData = playerData.data;
        }
        itemStack.setNbt(nbtData);
        player.getInventory().offerOrDrop(itemStack);
        playerData.boundSword = true;
        playerData.swordDestroyed = false;
    }

    public static void callSword(ServerPlayerEntity player) {
        PlayerData playerState = StateSaverAndLoader.getPlayerState(player.getWorld(), player.getUuid());
        if (playerState.boundSword) {
            if (player.getInventory().containsAny(Set.of(MythAndMagicItems.EXCALIBUR)) && player.getInventory().containsAny(stack ->
                    stack.getOrCreateNbt().contains("owner") && stack.getOrCreateNbt().getUuid("owner").equals(player.getUuid()))) {
                player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.inventory"), true);
            } else if (playerState.swordDestroyed) {
                createSword(player);
                MythAndMagic.EXCALIBUR_CALLED.trigger(player);
                player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.summon"), true);
            } else {
                player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.cant_summon"), true);
            }
        } else {
            player.sendMessage(Text.translatable("item.myth_and_magic.excalibur.not_bound"), true);
        }
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.myth_and_magic.excalibur.tooltip"));
        // show who the sword is bound to
        if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains("owner")) {
            if (world != null) {
                PlayerEntity player = world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("owner"));
                if (player != null) {
                    tooltip.add(Text.translatable("item.myth_and_magic.excalibur.tooltip_bound").append(
                            ((MutableText) player.getName()).formatted(Formatting.GOLD)));
                    if (!player.getName().toString().equals(itemStack.getOrCreateNbt().getString("player_name"))) {
                        NbtCompound nbtData = itemStack.getOrCreateNbt();
                        nbtData.putString("player_name", player.getName().getString());
                        itemStack.setNbt(nbtData);
                    }
                    return;
                }
            }
            tooltip.add(Text.translatable("item.myth_and_magic.excalibur.tooltip_bound").append(
                    Text.literal(itemStack.getOrCreateNbt().getString("player_name")).formatted(Formatting.GOLD)));
        }
    }
}