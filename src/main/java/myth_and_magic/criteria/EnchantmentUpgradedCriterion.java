package myth_and_magic.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import myth_and_magic.MythAndMagic;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class EnchantmentUpgradedCriterion extends AbstractCriterion<EnchantmentUpgradedCriterion.Conditions> {
    public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "enchantment_upgraded");

    public Identifier getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player) {
        trigger(player, Conditions::requirementsMet);
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

        public static AdvancementCriterion<Conditions> create() {
            return MythAndMagic.ENCHANTMENT_UPGRADED.create(new Conditions());
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