package myth_and_magic.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import myth_and_magic.MythAndMagic;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class MagicTableRecipe implements Recipe<SimpleInventory> {
    private final Ingredient input;
    private final Ingredient addition;
    private final ItemStack output;
    private final Identifier id;

    public MagicTableRecipe(Identifier id, ItemStack output, Ingredient input, Ingredient addition) {
        this.id = id;
        this.output = output;
        this.input = input;
        this.addition = addition;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient() || inventory.size() < 2) {
            return false;
        }
        return input.test(inventory.getStack(0)) && addition.test(inventory.getStack(1));
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

    public Ingredient getAddition() {
        return addition;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<MagicTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "magic_table";
    }

    public static class Serializer implements RecipeSerializer<MagicTableRecipe> {
        // this should probably throw an error, but I'm too lazy to do that
        private Serializer() {
        }

        public static final Serializer INSTANCE = new Serializer();
        public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "magic_table_recipe");

        @Override
        public MagicTableRecipe read(Identifier id, JsonObject json) {
            MagicTableRecipeJsonFormat recipeJson = new Gson().fromJson(json, MagicTableRecipeJsonFormat.class);
            Ingredient input = Ingredient.fromJson(recipeJson.input);
            Ingredient addition = Ingredient.fromJson(recipeJson.addition);
            Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem)).get();
            ItemStack output = new ItemStack(outputItem, recipeJson.outputAmount);
            return new MagicTableRecipe(id, output, input, addition);
        }

        @Override
        public MagicTableRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            Ingredient addition = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            return new MagicTableRecipe(id, output, input, addition);
        }

        @Override
        public void write(PacketByteBuf buf, MagicTableRecipe recipe) {
            recipe.getInput().write(buf);
            recipe.getAddition().write(buf);
            // passing null because I don't do anything with that
            buf.writeItemStack(recipe.getOutput(null));
        }
    }

    static class MagicTableRecipeJsonFormat {
        JsonObject input;
        JsonObject addition;
        String outputItem;
        int outputAmount;
    }
}