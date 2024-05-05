package myth_and_magic.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import myth_and_magic.MythAndMagic;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class TaskCompletedCriterion extends AbstractCriterion<TaskCompletedCriterion.Conditions> {
    public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "task_completed");

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, myth_and_magic.criteria.TaskCompletedCriterion.Conditions::requirementsMet);
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = Codec.of(Encoder.empty(), Decoder.unit(Conditions::new)).codec();
        public Conditions() {
            super();
        }

        public static AdvancementCriterion<myth_and_magic.criteria.TaskCompletedCriterion.Conditions> create() {
            return MythAndMagic.TASK_COMPLETED.create(new myth_and_magic.criteria.TaskCompletedCriterion.Conditions());
        }

        boolean requirementsMet() {
            return true;
        }

        @Override
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}