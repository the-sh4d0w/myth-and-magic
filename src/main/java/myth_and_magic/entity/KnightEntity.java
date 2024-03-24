package myth_and_magic.entity;

import myth_and_magic.MythAndMagic;
import myth_and_magic.item.MythAndMagicItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class KnightEntity extends PathAwareEntity {
    public final AnimationState statueAnimationState = new AnimationState();
    private int statueAnimationTimeout = 0;
    public final AnimationState attackAnimationState = new AnimationState();
    private int attackAnimationTimeout = 0;
    private static final TrackedData<Boolean> STATUE = DataTracker.registerData(KnightEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> TRACKING = DataTracker.registerData(KnightEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<BlockPos> STATUE_POSITION = DataTracker.registerData(KnightEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Float> STATUE_YAW = DataTracker.registerData(KnightEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(KnightEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    protected KnightEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d hitPos, Hand hand) {
        if (this.getOwnerUuid() != null && player.getUuid().equals(this.getOwnerUuid()) && player.isSneaking() && player.getMainHandStack().isEmpty()) {
            if (!this.getWorld().isClient()) {
                this.discard();
                player.getInventory().insertStack(MythAndMagicItems.KNIGHT_STATUE.getDefaultStack());
            }
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    public void updateStatuePosition() {
        while (this.getWorld().getBlockState(this.getStatuePosition()).getBlock().equals(Blocks.AIR)) {
            this.setStatuePosition(this.getStatuePosition().down());
        }
        while (!this.getWorld().getBlockState(this.getStatuePosition().up()).getBlock().equals(Blocks.AIR)) {
            this.setStatuePosition(this.getStatuePosition().up());
        }
    }

    public void snapToStatuePosition() {
        this.updateStatuePosition();
        this.setBodyYaw(this.getStatueYaw());
        this.setHeadYaw(this.getStatueYaw());
        this.setRotation(this.getStatueYaw(), this.getPitch());
        this.setPosition(this.getStatuePosition().toCenterPos().add(0, 0.5, 0));
    }

    @Override
    public Text getName() {
        if (this.isStatue()) {
            return Text.translatable("entity.myth_and_magic.knight.statue_name");
        } else {
            return Text.translatable("entity.myth_and_magic.knight.name");
        }
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return false;
    }

    @Override
    protected boolean canStartRiding(Entity entity) {
        return false;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        this.setStatue(false);
        super.onDeath(damageSource);
    }

    @Override
    public boolean isCollidable() {
        return this.isStatue();
    }

    @Override
    public void takeKnockback(double strength, double x, double z) {
        if (!this.isStatue()) {
            super.takeKnockback(strength, x, z);
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(STATUE, true);
        this.dataTracker.startTracking(TRACKING, false);
        this.dataTracker.startTracking(STATUE_POSITION, this.getBlockPos());
        this.dataTracker.startTracking(STATUE_YAW, 0f);
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("statue", this.isStatue());
        nbt.putBoolean("tracking", this.isTracking());
        nbt.putLong("statuePosition", this.getStatuePosition().asLong());
        nbt.putFloat("statueYaw", this.getStatueYaw());
        nbt.putUuid("ownerUuid", this.getOwnerUuid());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("statue")) {
            this.setStatue(nbt.getBoolean("statue"));
        }
        if (nbt.contains("tracking")) {
            this.setTracking(nbt.getBoolean("tracking"));
        }
        if (nbt.contains("statuePosition")) {
            this.setStatuePosition(BlockPos.fromLong(nbt.getLong("statuePosition")));
        }
        if (nbt.contains("statueYaw")) {
            this.setStatueYaw(nbt.getFloat("statueYaw"));
        }
        if (nbt.contains("ownerUuid")) {
            this.setOwnerUuid(nbt.getUuid("ownerUuid"));
        }
    }

    public boolean isStatue() {
        return this.dataTracker.get(STATUE);
    }

    public void setStatue(boolean statue) {
        this.dataTracker.set(STATUE, statue);
    }

    public boolean isTracking() {
        return this.dataTracker.get(TRACKING);
    }

    public void setTracking(boolean statue) {
        this.dataTracker.set(TRACKING, statue);
    }

    public BlockPos getStatuePosition() {
        return this.dataTracker.get(STATUE_POSITION);
    }

    public void setStatuePosition(BlockPos position) {
        this.dataTracker.set(STATUE_POSITION, position);
    }

    public float getStatueYaw() {
        return this.dataTracker.get(STATUE_YAW);
    }

    public void setStatueYaw(float yaw) {
        this.dataTracker.set(STATUE_YAW, yaw);
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(UUID owner) {
        this.dataTracker.set(OWNER_UUID, Optional.of(owner));
    }

    public static DefaultAttributeContainer.Builder createKnightAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200)
                .add(EntityAttributes.GENERIC_ARMOR, 6)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 1)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 2);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_IRON_GOLEM_REPAIR;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_IRON_GOLEM_DEATH;
    }

    private void setupAnimationStates() {
        if (this.isStatue() && this.statueAnimationTimeout <= 0) {
            this.statueAnimationTimeout = 100;
            this.statueAnimationState.start(this.age);
        } else {
            --this.statueAnimationTimeout;
        }

        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = 20;
            this.attackAnimationState.start(this.age);
        } else {
            --this.attackAnimationTimeout;
        }

        if (!this.isStatue()) {
            this.statueAnimationState.stop();
        }

        if (!this.isAttacking()) {
            this.attackAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorld().isClient()) {
            setupAnimationStates();
        } else if (this.isStatue()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new MeleeAttackGoal(this, 0.5f, true));
        this.goalSelector.add(2, new GoStatuePosGoal(this, 0.2f));
        this.targetSelector.add(0, new ProtectOwnerGoal(this));
        this.targetSelector.add(1, new AttackWithOwnerGoal(this));
    }

    static class ProtectOwnerGoal extends TrackTargetGoal {
        private final KnightEntity knight;
        private LivingEntity attacker;
        private int lastAttackedTime;

        public ProtectOwnerGoal(KnightEntity knight) {
            super(knight, false);
            this.knight = knight;
        }

        @Nullable
        private PlayerEntity getOwner() {
            return this.knight.getOwnerUuid() != null ? this.knight.getWorld().getPlayerByUuid(this.knight.getOwnerUuid()) : null;
        }

        @Override
        public boolean canStart() {
            if (this.getOwner() != null) {
                this.attacker = this.getOwner().getAttacker();
                return this.getOwner().getLastAttackedTime() != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT);
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.knight.setTracking(true);
            this.knight.setStatue(false);
            this.mob.setTarget(this.attacker);
            LivingEntity owner = this.getOwner();
            if (owner != null) {
                this.lastAttackedTime = owner.getLastAttackedTime();
            }
            super.start();
        }

        @Override
        public void stop() {
            this.knight.setTracking(false);
            super.stop();
        }
    }

    static class AttackWithOwnerGoal extends TrackTargetGoal {
        private final KnightEntity knight;
        private LivingEntity attacking;
        private int lastAttackTime;

        AttackWithOwnerGoal(KnightEntity knight) {
            super(knight, false);
            this.knight = knight;
        }

        @Nullable
        private PlayerEntity getOwner() {
            return this.knight.getOwnerUuid() != null ? this.knight.getWorld().getPlayerByUuid(this.knight.getOwnerUuid()) : null;
        }

        @Override
        public boolean canStart() {
            if (this.getOwner() != null) {
                this.attacking = this.getOwner().getAttacking();
                return this.getOwner().getLastAttackTime() != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT)
                        && !(this.attacking instanceof KnightEntity) && !((this.attacking instanceof TameableEntity)
                        && ((TameableEntity) this.attacking).isTamed());
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            this.knight.setTracking(true);
            this.knight.setStatue(false);
            this.mob.setTarget(this.attacking);
            LivingEntity owner = this.getOwner();
            if (owner != null) {
                this.lastAttackTime = owner.getLastAttackTime();
            }
            super.start();
        }

        @Override
        public void stop() {
            this.knight.setTracking(false);
            super.stop();
        }
    }

    static class GoStatuePosGoal extends Goal {
        private final KnightEntity knight;
        private final double speed;

        GoStatuePosGoal(KnightEntity knight, double speed) {
            this.knight = knight;
            this.speed = speed;
        }

        @Override
        public boolean canStart() {
            return !this.knight.isStatue() && !this.knight.isTracking();
        }

        @Nullable
        private PlayerEntity getOwner() {
            return this.knight.getOwnerUuid() != null ? this.knight.getWorld().getPlayerByUuid(this.knight.getOwnerUuid()) : null;
        }

        @Override
        public void start() {
            this.knight.updateStatuePosition();
            if (this.getOwner() != null && !this.knight.getWorld().isClient()) {
                MythAndMagic.KNIGHT_PROTECT.trigger((ServerPlayerEntity) this.getOwner());
            }
        }

        @Override
        public boolean shouldContinue() {
            boolean close = this.knight.getStatuePosition().isWithinDistance(this.knight.getBlockPos(), 2);
            return (!this.knight.isTracking() && !close) || (close && !this.knight.getNavigation().isIdle());
        }

        @Override
        public void stop() {
            if (!this.knight.isTracking()) {
                this.knight.setStatue(true);
                this.knight.setVelocity(0, 0, 0);
                this.knight.snapToStatuePosition();
            }
        }

        @Override
        public void tick() {
            if (this.knight.getNavigation().isIdle()) {
                BlockPos pos = this.knight.getStatuePosition();
                this.knight.getNavigation().startMovingTo(pos.getX(), pos.getY(), pos.getZ(), this.speed);
            }
        }
    }
}