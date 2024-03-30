package myth_and_magic.compat.jade;

import myth_and_magic.MythAndMagic;
import myth_and_magic.entity.KnightEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.util.CommonProxy;

import java.util.UUID;

public enum KnightOwnerProvider implements IEntityComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        UUID ownerUuid = ((KnightEntity) entityAccessor.getEntity()).getOwnerUuid();
        if (ownerUuid != null && CommonProxy.getLastKnownUsername(ownerUuid) != null) {
            tooltip.add(Text.translatable("jade.myth_and_magic.owner", CommonProxy.getLastKnownUsername(ownerUuid)));
        }
    }

    @Override
    public Identifier getUid() {
        return new Identifier(MythAndMagic.MOD_ID, "knight");
    }
}