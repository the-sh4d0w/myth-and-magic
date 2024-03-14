package myth_and_magic.entity.client;

import myth_and_magic.MythAndMagic;
import myth_and_magic.entity.KnightEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class KnightEyeFeatureRenderer extends FeatureRenderer<KnightEntity, KnightModel<KnightEntity>> {
    public KnightEyeFeatureRenderer(FeatureRendererContext<KnightEntity, KnightModel<KnightEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, KnightEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isStatue()) {
            renderModel(this.getContextModel(), new Identifier(MythAndMagic.MOD_ID, "textures/entity/knight_eyes.png"), matrices, vertexConsumers, light, entity, 1, 1, 1);
        }
    }
}