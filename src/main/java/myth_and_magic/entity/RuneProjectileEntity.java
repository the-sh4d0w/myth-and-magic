package myth_and_magic.entity;

import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class RuneProjectileEntity extends ThrownItemEntity {
    enum RuneType {
        FIRE,
        ICE,
        HEAL,
        LIGHTNING,
        EXPLOSIVE
    }

    private RuneType type;

    public RuneProjectileEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public RuneProjectileEntity(LivingEntity livingEntity, World world, ItemStack itemStack) {
        super(MythAndMagicEntities.RUNE_PROJECTILE, livingEntity, world);
        if (itemStack.isOf(MythAndMagicItems.FIRE_RUNE)) {
            this.type = RuneType.FIRE;
        } else if (itemStack.isOf(MythAndMagicItems.ICE_RUNE)) {
            this.type = RuneType.ICE;
        } else if (itemStack.isOf(MythAndMagicItems.HEAL_RUNE)) {
            this.type = RuneType.HEAL;
        } else if (itemStack.isOf(MythAndMagicItems.LIGHTNING_RUNE)) {
            this.type = RuneType.LIGHTNING;
        } else if (itemStack.isOf(MythAndMagicItems.EXPLOSIVE_RUNE)) {
            this.type = RuneType.EXPLOSIVE;
        }
    }

    @Override
    protected Item getDefaultItem() {
        return MythAndMagicItems.RUNE;
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        if (!world.isClient() && this.type != null) {
            DefaultParticleType particle = switch (this.type) {
                case FIRE -> ParticleTypes.FLAME;
                case ICE -> ParticleTypes.SNOWFLAKE;
                case HEAL -> ParticleTypes.HAPPY_VILLAGER;
                case LIGHTNING -> ParticleTypes.ELECTRIC_SPARK;
                case EXPLOSIVE -> ParticleTypes.WHITE_ASH;
            };
            ((ServerWorld) world).spawnParticles(particle, this.getX(), this.getY(), this.getZ(), 5,
                    0, 0, 0, 0.05);
        }
        super.tick();
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        World world = this.getWorld();
        if (!world.isClient() && this.type != null) {
            world.sendEntityStatus(this, (byte) 3);
            Entity entity = entityHitResult.getEntity();
            switch (this.type) {
                case FIRE -> {
                    entity.setFireTicks(7 * 20);
                    entity.setFrozenTicks(0);
                }
                case ICE -> {
                    entity.damage(entity.getDamageSources().magic(), 2);
                    entity.setFrozenTicks(30 * 20);
                    entity.setFireTicks(0);
                }
                case HEAL -> {
                    if (entity instanceof LivingEntity) {
                        ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH,
                                10, 0, false, false));
                    }
                }
                case LIGHTNING -> {
                    LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
                    if (lightning != null) {
                        lightning.refreshPositionAfterTeleport(entity.getPos());
                        world.spawnEntity(lightning);
                    }
                }
                case EXPLOSIVE -> {
                    world.createExplosion(this, this.getX(), this.getBodyY(0.0625), this.getZ(), 3,
                            World.ExplosionSourceType.NONE);
                }
            }
            ((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 3,
                    0, 0, 0, 0.025);
        }
        this.discard();
        super.onEntityHit(entityHitResult);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        if (!world.isClient()) {
            ((ServerWorld) world).spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 3,
                    0, 0, 0, 0.025);
        }
        this.discard();
        super.onBlockHit(blockHitResult);
    }
}