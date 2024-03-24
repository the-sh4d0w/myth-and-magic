package myth_and_magic.criteria;

import com.google.gson.JsonObject;
import myth_and_magic.MythAndMagic;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class HealRuneUsedCriterion extends AbstractCriterion<HealRuneUsedCriterion.Conditions> {
    private static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "heal_rune_used");

    @Override
    protected Conditions conditionsFromJson(JsonObject json, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions();
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