package myth_and_magic;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ExcaliburClaimedCriterion extends AbstractCriterion<ExcaliburClaimedCriterion.Conditions> {
    private static Identifier ID = new Identifier(MythAndMagic.MOD_ID, "excalibur_claimed");

    @Override
    protected Conditions conditionsFromJson(JsonObject json, LootContextPredicate playerPredicate,
                                            AdvancementEntityPredicateDeserializer predicateDeserializer) {
        Conditions conditions = new Conditions();
        return conditions;
    }

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, Conditions::requirementsMet);
    }

    public static class Conditions extends AbstractCriterionConditions {
        public Conditions() {
            super(ID, LootContextPredicate.EMPTY);
        }

        public static Conditions create() {
            return new Conditions();
        }

        boolean requirementsMet() {
            return true;
        }
    }
}