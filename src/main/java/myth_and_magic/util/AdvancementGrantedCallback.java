package myth_and_magic.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.player.PlayerEntity;

public interface AdvancementGrantedCallback {
    Event<AdvancementGrantedCallback.Grant> EVENT = EventFactory.createArrayBacked(Grant.class,
            (listeners) -> (player, advancement) -> {
                for (Grant listener : listeners) {
                    listener.grant(player, advancement);
                }
            });

    @FunctionalInterface
    interface Grant {
        void grant(PlayerEntity player, Advancement advancement);
    }
}