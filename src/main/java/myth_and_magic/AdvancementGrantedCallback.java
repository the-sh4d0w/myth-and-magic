package myth_and_magic;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

public interface AdvancementGrantedCallback {
    Event<AdvancementGrantedCallback.Grant> EVENT = EventFactory.createArrayBacked(Grant.class,
            (listeners) -> (player, advancement) -> {
                for (Grant listener : listeners) {
                    listener.grant(player, advancement);
                }
            });

    @FunctionalInterface
    public interface Grant {
        void grant(PlayerEntity player, Advancement advancement);
    }
}