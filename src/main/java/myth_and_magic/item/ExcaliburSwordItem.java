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
        if (itemStack.hasNbt() && itemStack.getOrCreateNbt().contains("owner")) {
            PlayerEntity player = world.getPlayerByUuid(itemStack.getOrCreateNbt().getUuid("owner"));
            if (player != null) {
                tooltip.add(Text.translatable("item.myth_and_magic.excalibur.bound_tooltip").append(player.getName()));
            } else {
                tooltip.add(Text.translatable("item.myth_and_magic.excalibur.bound_tooltip").append(Text.literal("Unknown")));
            }
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        // TODO: give better feedback; maybe text on screen
        if (player.getStackInHand(hand).hasNbt()) {
            if (player.getStackInHand(hand).getOrCreateNbt().contains("owner")
                    && player.getStackInHand(hand).getOrCreateNbt().getUuid("owner").equals(player.getUuid())) {
                player.sendMessage(Text.literal("You are the owner."));
            } else {
                player.sendMessage(Text.literal("You are not the owner."));
            }
            return TypedActionResult.fail(player.getStackInHand(hand));
        } else {
            NbtCompound nbtData = new NbtCompound();
            nbtData.putUuid("owner", player.getUuid());
            player.getStackInHand(hand).setNbt(nbtData);
            player.sendMessage(Text.literal("You are now the owner."));
            player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
            return TypedActionResult.success(player.getStackInHand(hand));
        }
    }
}