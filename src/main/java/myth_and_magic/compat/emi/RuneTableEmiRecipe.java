package myth_and_magic.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import myth_and_magic.item.MythAndMagicItems;
import myth_and_magic.recipe.RuneTableRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class RuneTableEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final int levelCost;

    public RuneTableEmiRecipe(RecipeEntry<RuneTableRecipe> recipe) {
        this.id = recipe.id();
        this.input = List.of(EmiIngredient.of(recipe.value().getInput()));
        this.output = List.of(EmiStack.of(recipe.value().getResult(null)));
        this.levelCost = recipe.value().getLevelCost();
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return MythAndMagicEmiPlugin.RUNE_TABLE_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        ArrayList<EmiIngredient> arrayList = new ArrayList<>();
        arrayList.add(input.get(0));
        arrayList.addAll(List.of(EmiIngredient.of(Ingredient.ofItems(MythAndMagicItems.RUNE)),
                EmiIngredient.of(Ingredient.ofItems(MythAndMagicItems.LEVEL_PHIAL), levelCost)));
        return arrayList;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 128;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 78, 1);
        widgets.addSlot(EmiIngredient.of(Ingredient.ofItems(MythAndMagicItems.RUNE)), 0, 0);
        widgets.addSlot(EmiIngredient.of(Ingredient.ofItems(MythAndMagicItems.LEVEL_PHIAL)).setAmount(levelCost), 26, 0);
        widgets.addSlot(getInputs().get(0), 52, 0);
        widgets.addSlot(getOutputs().get(0), 110, 0).recipeContext(this);
    }
}