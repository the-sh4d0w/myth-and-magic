package myth_and_magic.item;

import myth_and_magic.util.PlayerData;
import myth_and_magic.util.StateSaverAndLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NarcissusMirrorItem extends Item {
    public NarcissusMirrorItem(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            return TypedActionResult.success(player.getStackInHand(hand));
        }
        PlayerData playerState = StateSaverAndLoader.getPlayerState(world, player.getUuid());
        MutableText text = Text.translatable(playerState.worthiness >= ExcaliburSwordItem.REQUIRED_WORTHINESS ?
                "item.myth_and_magic.narcissus_mirror.worthy" : "item.myth_and_magic.narcissus_mirror.not_worthy");
        text.append(" ");
        text.append(Text.translatable(playerState.boundSword ? "item.myth_and_magic.narcissus_mirror.sword_bound"
                : "item.myth_and_magic.narcissus_mirror.sword_not_bound"));
        player.sendMessage(text, true);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}