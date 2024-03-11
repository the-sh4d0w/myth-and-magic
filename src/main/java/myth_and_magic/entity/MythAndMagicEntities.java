package myth_and_magic.entity;

import myth_and_magic.MythAndMagic;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class MythAndMagicEntities {
    public static final EntityType<RuneProjectileEntity> RUNE_PROJECTILE = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(MythAndMagic.MOD_ID, "rune_projectile"),
            FabricEntityTypeBuilder.<RuneProjectileEntity>create(SpawnGroup.MISC, RuneProjectileEntity::new)
                    .dimensions(EntityDimensions.fixed(0.1f, 0.1f)).build());

    public static void registerEntities() {
        MythAndMagic.LOGGER.info("Registering Entities for %s".formatted(MythAndMagic.MOD_ID));
    }
}