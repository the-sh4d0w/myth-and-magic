package myth_and_magic.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
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

public class RuneTableRecipe implements Recipe<SimpleInventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final int levelCost;
    private final Identifier id;

    public RuneTableRecipe(Identifier id, ItemStack output, Ingredient input, int levelCost) {
        this.id = id;
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

        @Override
        public RuneTableRecipe read(Identifier id, JsonObject json) {
            RuneTableRecipeJsonFormat recipeJson = new Gson().fromJson(json, RuneTableRecipeJsonFormat.class);
            Ingredient input = Ingredient.fromJson(recipeJson.input);
            Item outputItem = Registries.ITEM.getOrEmpty(new Identifier(recipeJson.outputItem)).get();
            ItemStack output = new ItemStack(outputItem, 1);
            int levelCost = recipeJson.levelCost;
            return new RuneTableRecipe(id, output, input, levelCost);
        }

        @Override
        public RuneTableRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            int levelCost = buf.readInt();
            return new RuneTableRecipe(id, output, input, levelCost);
        }

        @Override
        public void write(PacketByteBuf buf, RuneTableRecipe recipe) {
            recipe.getInput().write(buf);
            // passing null because I don't do anything with that
            buf.writeItemStack(recipe.getOutput(null));
            buf.writeInt(recipe.getLevelCost());
        }
    }

    static class RuneTableRecipeJsonFormat {
        JsonObject input;
        String outputItem;
        int levelCost;
    }
}