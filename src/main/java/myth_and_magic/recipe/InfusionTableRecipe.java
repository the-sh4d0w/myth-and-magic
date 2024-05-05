package myth_and_magic.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import myth_and_magic.MythAndMagic;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class InfusionTableRecipe implements Recipe<SimpleInventory> {
    private final Ingredient input;
    private final ItemStack output;
    private final String name;
    private final int levelCost;

    public InfusionTableRecipe(ItemStack output, Ingredient input, String name, int levelCost) {
        this.output = output;
        this.input = input;
        this.name = name;
        this.levelCost = levelCost;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient() || inventory.size() < 2) {
            return false;
        }
        return input.test(inventory.getStack(0)) && (name.isEmpty() || inventory.getStack(0).getName().getString().equalsIgnoreCase(name));
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

    public String getName() {
        return name;
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
        return InfusionTableRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<InfusionTableRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "infusion_table";
    }

    public static class Serializer implements RecipeSerializer<InfusionTableRecipe> {
        // this should probably also throw an error, but I'm again too lazy to do that
        private Serializer() {
        }

        public static final InfusionTableRecipe.Serializer INSTANCE = new InfusionTableRecipe.Serializer();
        public static final Identifier ID = new Identifier(MythAndMagic.MOD_ID, "infusion_table_recipe");
        public static final Codec<InfusionTableRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("outputItem").xmap(id -> Registries.ITEM.get(id).getDefaultStack(),
                        itemStack -> Registries.ITEM.getId(itemStack.getItem())).forGetter(r -> r.output),
                Ingredient.DISALLOW_EMPTY_CODEC.fieldOf("input").forGetter(InfusionTableRecipe::getInput),
                Codec.STRING.fieldOf("name").orElse("").forGetter(InfusionTableRecipe::getName),
                Codec.INT.fieldOf("levelCost").forGetter(InfusionTableRecipe::getLevelCost)
        ).apply(instance, InfusionTableRecipe::new));

        @Override
        public Codec<InfusionTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public InfusionTableRecipe read(PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            ItemStack output = buf.readItemStack();
            String name = buf.readString();
            int levelCost = buf.readInt();
            return new InfusionTableRecipe(output, input, name, levelCost);
        }

        @Override
        public void write(PacketByteBuf buf, InfusionTableRecipe recipe) {
            recipe.getInput().write(buf);
            // passing null because I don't do anything with that
            buf.writeItemStack(recipe.getResult(null));
            buf.writeString(recipe.getName());
            buf.writeInt(recipe.getLevelCost());
        }
    }
}