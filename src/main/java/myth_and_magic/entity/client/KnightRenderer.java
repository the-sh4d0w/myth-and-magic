package myth_and_magic.entity.client;

import myth_and_magic.MythAndMagic;
import myth_and_magic.entity.KnightEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;

public class KnightRenderer<T extends MobEntity> extends MobEntityRenderer<KnightEntity, KnightModel<KnightEntity>> {
    public KnightRenderer(EntityRendererFactory.Context context) {
        super(context, new KnightModel<>(context.getPart(MythAndMagicModelLayers.KNIGHT)), 1f);
    }

    @Override
    public Identifier getTexture(KnightEntity entity) {
        return new Identifier(MythAndMagic.MOD_ID, "textures/entity/knight.png");
    }
}