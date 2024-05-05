package myth_and_magic.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class RuneTableRecipe implements Recipe<SimpleInventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final int levelCost;

    public RuneTableRecipe(ItemStack output, Ingredient input, int levelCost) {
        this.output = output;
        this.input = input;
        this.levelCost = levelCost;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient() || inventory.size() < 4) {
            return false;
        }
        return input.test(inventory.getStack(0)) && Ingredient.ofItems(MythAndMagicItems.RUNE).test(inventory.getStack(1))
                && Ingredient.ofItems(MythAndMagicItems.LEVEL_PHIAL).test(inventory.getStack(2))
                && inventory.getStack(2).getCount() >= levelCost;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    public Ingredient getInput() {
        return input;
    }

    public int getLevelCost() {
        return levelCost;
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<RuneTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "rune_table";
    }

    public static class Serializer implements RecipeSerializer<RuneTableRecipe> {
        // this should probably throw an error, but I'm too lazy to do that
        private Serializer() {
        }

        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "rune_table_recipe");
        public static final Codec<RuneTableRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("outputItem").xmap(id -> Registries.ITEM.get(id).getDefaultStack(),
                        itemStack -> Registries.ITEM.getId(itemStack.getItem())).forGetter(r -> r.output),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(RuneTableRecipe::getInput),
                Codec.INT.fieldOf("levelCost").forGetter(RuneTableRecipe::getLevelCost)
        ).apply(instance, RuneTableRecipe::new));

        @Override
        public Codec<RuneTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public RuneTableRecipe read(PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            int levelCost = buf.readInt();
            return new RuneTableRecipe(output, input, levelCost);
        }

        @Override
        public void write(PacketByteBuf buf, RuneTableRecipe recipe) {
            recipe.getInput().write(buf);
            // passing null because I don't do anything with that
            buf.writeItemStack(recipe.getResult(null));
            buf.writeInt(recipe.getLevelCost());
        }
    }
}