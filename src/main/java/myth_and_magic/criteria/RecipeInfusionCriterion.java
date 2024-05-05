package myth_and_magic.criteria;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import myth_and_magic.MythAndMagic;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeInfusionCriterion extends AbstractCriterion<RecipeInfusionCriterion.Conditions> {
    public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "recipe_infusion");

    @Override
    protected Conditions conditionsFromJson(JsonObject json, Optional<LootContextPredicate> playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        List<Identifier> recipeIds = new java.util.ArrayList<>(List.of());
        for (JsonElement element : json.getAsJsonArray("recipe_ids")) {
            recipeIds.add(new Identifier(element.getAsString()));
        }
        return new Conditions(recipeIds);
    }

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, Identifier recipeId) {
        trigger(player, conditions -> conditions.matches(recipeId));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private List<Identifier> recipeIds;

        public Conditions(List<Identifier> recipeIds) {
            super(Optional.empty());
            this.recipeIds = recipeIds;
        }

        public static AdvancementCriterion<Conditions> create(Identifier... recipeIds) {
            return MythAndMagic.RECIPE_INFUSION.create(new Conditions(Arrays.stream(recipeIds).toList()));
        }

        boolean matches(Identifier matchRecipeId) {
            for (Identifier recipeId : this.recipeIds) {
                if (recipeId.equals(matchRecipeId)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public JsonObject toJson() {
            JsonObject jsonObject = super.toJson();
            JsonArray recipeIds = new JsonArray();
            for (Identifier recipeId : this.recipeIds) {
                recipeIds.add(recipeId.toString());
            }
            jsonObject.add("recipe_ids", recipeIds);
            return jsonObject;
        }
    }
}