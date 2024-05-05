package myth_and_magic.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import myth_and_magic.MythAndMagic;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RecipeRuneCriterion extends AbstractCriterion<RecipeRuneCriterion.Conditions> {
    public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "recipe_rune");

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, Identifier recipeId) {
        trigger(player, conditions -> conditions.matches(recipeId));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public static class Conditions implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.listOf().fieldOf("recipe_ids").forGetter(Conditions::getRecipeIds)
        ).apply(instance, Conditions::new));
        private List<Identifier> recipeIds;

        public List<Identifier> getRecipeIds() {
            return recipeIds;
        }

        public Conditions(List<Identifier> recipeIds) {
            super();
            this.recipeIds = recipeIds;
        }

        public static AdvancementCriterion<Conditions> create(Identifier... recipeIds) {
            return MythAndMagic.RECIPE_RUNE.create(new Conditions(Arrays.stream(recipeIds).toList()));
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
        public Optional<LootContextPredicate> player() {
            return Optional.empty();
        }
    }
}