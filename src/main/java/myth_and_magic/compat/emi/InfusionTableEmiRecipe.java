package myth_and_magic.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import myth_and_magic.recipe.InfusionTableRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class InfusionTableEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final List<EmiIngredient> input;
    private final List<EmiStack> output;
    private final int levelCost;

    public InfusionTableEmiRecipe(InfusionTableRecipe recipe) {
        // this is somewhat cursed, but it works ¯\_(ツ)_/¯
        this.id = recipe.getId();
        this.input = List.of(EmiStack.of(recipe.getInput().getMatchingStacks()[0]
                .setCustomName(Text.literal(recipe.getName()))));
        this.output = List.of(EmiStack.of(recipe.getOutput(null)));
        this.levelCost = recipe.getLevelCost();
    }

    @Override
    public boolean supportsRecipeTree() {
        return true;
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return MythAndMagicEmiPlugin.INFUSION_TABLE_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return input;
    }

    @Override
    public List<EmiStack> getOutputs() {
        return output;
    }

    @Override
    public int getDisplayWidth() {
        return 76;
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1);
        widgets.addSlot(getInputs().get(0), 0, 0);
        widgets.addSlot(getOutputs().get(0), 58, 0).recipeContext(this);
    }
}