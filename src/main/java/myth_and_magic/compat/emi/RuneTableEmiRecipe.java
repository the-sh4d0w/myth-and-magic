package myth_and_magic.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import myth_and_magic.recipe.RuneTableRecipe;
import net.minecraft.util.Identifier;

import java.util.List;

public class RuneTableEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiIngredient> addition;
    private final List<EmiStack> output;

    public RuneTableEmiRecipe(RuneTableRecipe recipe) {
        this.id = recipe.getId();
        this.input = List.of(EmiIngredient.of(recipe.getInput()));
        this.addition = List.of(EmiIngredient.of(recipe.getAddition()));
        this.output = List.of(EmiStack.of(recipe.getOutput(null)));
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
        List<EmiIngredient> inputs = new java.util.ArrayList<>(input);
        inputs.addAll(addition);
        return inputs;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 102;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 52, 1);
        widgets.addSlot(getInputs().get(1), 0, 0);
        widgets.addSlot(getInputs().get(0), 26, 0);
        widgets.addSlot(getOutputs().get(0), 84, 0).recipeContext(this);
    }
}