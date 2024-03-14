package myth_and_magic.entity.animation;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

/**
 * Made with Blockbench 4.9.4 by Sh4d0w
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 *
 * @author Author
 */
public class KnightAnimation {

    public static final Animation KNIGHT_STATUE = Animation.Builder.create(0.0F)
            .addBoneAnimation("arm_upper_left", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("arm_upper_right", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("arm_lower_right", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -80.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("arm_lower_left", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 80.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("sword", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(-120.0F, 0.0F, -10.0F), Transformation.Interpolations.LINEAR)
            ))
            .addBoneAnimation("sword", new Transformation(Transformation.Targets.TRANSLATE,
                    new Keyframe(0.0F, AnimationHelper.createTranslationalVector(1.0F, -2.0F, -1.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();
}