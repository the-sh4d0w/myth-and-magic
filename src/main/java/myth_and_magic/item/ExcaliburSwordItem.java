package myth_and_magic.item;

import myth_and_magic.item.materials.Argonium;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class ExcaliburSwordItem extends SwordItem {
    public static Identifier CALL_SWORD_PACKET_ID = new Identifier("myth_and_magic", "call_sword");

    public ExcaliburSwordItem(Settings settings) {
        super(new Argonium(), 5, -2f, settings.rarity(Rarity.EPIC));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        tooltip.add(Text.translatable("item.myth_and_magic.excalibur.tooltip"));
        if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains("myth_and_magic.owner")) {
            PlayerEntity player = world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("myth_and_magic.owner"));
            if (player != null) {
                tooltip.add(Text.translatable("item.myth_and_magic.excalibur.bound_tooltip").append(player.getName()));
                if (!player.getName().toString().equals(itemStack.getOrCreateNbt().getString("myth_and_magic.player_name"))) {
                    NbtCompound nbtData = itemStack.getOrCreateNbt();
                    nbtData.putString("myth_and_magic.player_name", player.getName().getString());
                    itemStack.setNbt(nbtData);
                }
            } else {
                tooltip.add(Text.translatable("item.myth_and_magic.excalibur.bound_tooltip").append(
                        Text.literal(itemStack.getOrCreateNbt().getString("myth_and_magic.player_name"))));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // TODO: give better feedback; maybe text on screen
        // TODO: figure out what makes someone worthy
        if (player.getStackInHand(hand).hasNbt()) {
            if (player.getStackInHand(hand).getOrCreateNbt().contains("myth_and_magic.owner")
                    && player.getStackInHand(hand).getOrCreateNbt().getUuid("myth_and_magic.owner").equals(player.getUuid())) {
                player.sendMessage(Text.literal("You are the owner."));
            } else {
                player.sendMessage(Text.literal("You are not the owner."));
            }
            return TypedActionResult.fail(player.getStackInHand(hand));
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putUuid("myth_and_magic.owner", player.getUuid());
            nbtData.putString("myth_and_magic.player_name", player.getName().getString());
            player.getStackInHand(hand).setNbt(nbtData);
            player.sendMessage(Text.literal("You are now the owner."));
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
            return TypedActionResult.success(player.getStackInHand(hand));
        }
    }
}