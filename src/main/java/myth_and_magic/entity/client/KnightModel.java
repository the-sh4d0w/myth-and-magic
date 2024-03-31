package myth_and_magic.entity.client;

import myth_and_magic.entity.KnightEntity;
import myth_and_magic.entity.animation.KnightAnimation;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

// Made with Blockbench 4.9.4 ny Sh4d0w
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class KnightModel<T extends KnightEntity> extends SinglePartEntityModel<T> {
    private final ModelPart base;

    /**
     * private final ModelPart torso;
     * private final ModelPart head;
     * private final ModelPart arm_upper_right;
     * private final ModelPart arm_lower_right;
     * private final ModelPart sword;
     * private final ModelPart arm_upper_left;
     * private final ModelPart armer_lower_left;
     * private final ModelPart leg_right;
     * private final ModelPart leg_left;
     **/
    public KnightModel(ModelPart root) {
        this.base = root.getChild("base");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 2.0F));

        ModelPartData torso = base.addChild("torso", ModelPartBuilder.create().uv(22, 24).cuboid(-9.0F, -18.0F, -3.0F, 18.0F, 18.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -18.0F, 0.0F));

        ModelPartData head = torso.addChild("head", ModelPartBuilder.create().uv(22, 0).cuboid(-6.0F, -11.0F, -5.0F, 12.0F, 12.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -19.0F, -1.0F));

        ModelPartData arm_upper_right = torso.addChild("arm_upper_right", ModelPartBuilder.create().uv(4, 42).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-12.0F, -18.0F, 0.0F));

        ModelPartData arm_lower_right = arm_upper_right.addChild("arm_lower_right", ModelPartBuilder.create().uv(5, 61).cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData sword = arm_lower_right.addChild("sword", ModelPartBuilder.create().uv(0, 11).cuboid(-3.0F, 6.0F, -0.5F, 6.0F, 19.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 31).cuboid(-2.0F, 25.0F, -0.5F, 4.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-5.0F, 3.0F, -0.5F, 10.0F, 3.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-2.0F, -3.0F, -0.5F, 4.0F, 6.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        ModelPartData arm_upper_left = torso.addChild("arm_upper_left", ModelPartBuilder.create().uv(64, 42).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(12.0F, -18.0F, 0.0F));

        ModelPartData arm_lower_left = arm_upper_left.addChild("arm_lower_left", ModelPartBuilder.create().uv(65, 61).cuboid(-2.5F, 0.0F, -2.5F, 5.0F, 12.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 12.0F, 0.0F));

        ModelPartData leg_right = torso.addChild("leg_right", ModelPartBuilder.create().uv(28, 48).cuboid(-3.0F, 0.0F, -3.0F, 6.0F, 18.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(-4.0F, 0.0F, 0.0F));

        ModelPartData leg_left = torso.addChild("leg_left", ModelPartBuilder.create().uv(40, 72).cuboid(-3.0F, 1.0F, -2.0F, 6.0F, 18.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(4.0F, -1.0F, -1.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart getPart() {
        return base;
    }

    @Override
    public void setAngles(KnightEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.animateMovement(KnightAnimation.KNIGHT_WALK, limbAngle, limbDistance, 2f, 2.5f);
        this.updateAnimation(entity.statueAnimationState, KnightAnimation.KNIGHT_STATUE, animationProgress, 1f);
        this.updateAnimation(entity.attackAnimationState, KnightAnimation.KNIGHT_ATTACK, animationProgress, 1f);
        this.updateAnimation(entity.wakeupAnimationState, KnightAnimation.KNIGHT_WAKE_UP, animationProgress, 1f);
    }
}