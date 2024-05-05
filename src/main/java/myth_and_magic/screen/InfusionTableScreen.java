package myth_and_magic.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import myth_and_magic.MythAndMagic;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InfusionTableScreen extends HandledScreen<InfusionTableScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(MythAndMagic.MOD_ID, "textures/gui/infusion_table_gui.png");
    private final PlayerEntity player;

    public InfusionTableScreen(InfusionTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.player = inventory.player;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        if (!this.handler.getSlot(1).hasStack() || this.handler.getLevelCost() == 0) {
            return;
        }
        int levelCost = this.handler.getLevelCost();
        Text costText = Text.translatable("block.myth_and_magic.infusion_table.cost", levelCost);
        int colour;
        if (this.handler.getSlot(1).canTakeItems(this.player)) {
            colour = 8453920;
        } else {
            colour = 16736352;
        }
        int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(costText) - 2;
        context.fill(k - 2, 67, this.backgroundWidth - 8, 79, 1325400064);
        context.drawTextWithShadow(this.textRenderer, costText, k, 69, colour);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}