package com.logic.htweaks.entity.projectile;

import com.logic.htweaks.bridge.IScavFaction;
import com.tacz.guns.util.ExplodeUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class HeatSeekingMissileEntity extends Projectile implements GeoAnimatable {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int explosionDelayCount = 180;
    private int life = 200;
    private double gravity = 0.015;
    private double friction = 0.005;

    private Entity target;

    public HeatSeekingMissileEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        this.onBulletTick();

        Vec3 movement = this.getDeltaMovement();
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        double distance = movement.horizontalDistance();
        this.setYRot((float)Math.toDegrees(Mth.atan2(x, z)));
        this.setXRot((float)Math.toDegrees(Mth.atan2(y, distance)));
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
        this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
        double nextPosX = this.getX() + x;
        double nextPosY = this.getY() + y;
        double nextPosZ = this.getZ() + z;
        this.setPos(nextPosX, nextPosY, nextPosZ);
        float friction = (float) this.friction;
        float gravity = (float) this.gravity;
        if (this.isInWater()) {
            for(int i = 0; i < 4; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, nextPosX - x * 0.25, nextPosY - y * 0.25, nextPosZ - z * 0.25, x, y, z);
            }

            friction = 0.4F;
            gravity *= 0.6F;
        }

        this.setDeltaMovement(this.getDeltaMovement().scale((double)(1.0F - friction)));
        this.setDeltaMovement(this.getDeltaMovement().add(0.0, (double)(-gravity), 0.0));
        if (this.tickCount >= this.life - 1) {
            this.discard();
        }
    }

    protected void onBulletTick() {
        if (!this.level().isClientSide()) {
            if(this.getTarget() == null) {
                AABB aabb = this.getBoundingBox().inflate(60);

                List<Entity> entities = level().getEntitiesOfClass(Entity.class, aabb);

                for(Entity entity : entities) {
                    if(entity == this.getOwner())continue;

                    if(this.isAllied(entity) || this.isAllied(entity.getControllingPassenger())) {
                        continue;
                    }

                    if(entity instanceof LivingEntity || entity.isVehicle()) {
                        this.setTarget(entity);
                        break;
                    }
                }
            }

            if(this.getTarget() != null) {
                Vec3 direction = this.getTargetDirection();

                float targetYaw = (float) (Mth.atan2(-direction.x, direction.z) * (180.0 / Math.PI));
                float targetPitch = (float) (Mth.atan2(-direction.y, direction.horizontalDistance()) * (180.0 / Math.PI));

                this.yRotO = this.getYRot();
                this.xRotO = this.getXRot();

                this.setYRot(lerpRotation(this.getYRot(), targetYaw, 0.1f));
                this.setXRot(lerpRotation(this.getXRot(), targetPitch, 0.1f));

            }

            if (this.explosionDelayCount <= 0) {
                ExplodeUtil.createExplosion(this.getOwner(), this, 25.0F, 5, true, true, this.position());
                this.discard();
                return;
            }

            --this.explosionDelayCount;
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if(hitResult.getEntity() != this.getOwner()) {
            ExplodeUtil.createExplosion(this.getOwner(), this, 25.0F, 5, true, true, this.position());
        }

        super.onHitEntity(hitResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        ExplodeUtil.createExplosion(this.getOwner(), this, 25.0F, 5, true, true, this.position());

        super.onHitBlock(p_37258_);
    }

    private boolean isAllied(Entity entity) {
        if(entity instanceof IScavFaction entityScav && this.getOwner() instanceof IScavFaction ownerScav) {
            if(entityScav.htweaks$getFaction() == ownerScav.htweaks$getFaction()) {
                return true;
            }
        }

        return false;
    }

    private Vec3 getTargetDirection() {
        Entity target = this.getTarget();

        if(target == null)return Vec3.ZERO;

        Vec3 targetPos = target.position();
        Vec3 missilePos = this.position();

        return targetPos.subtract(missilePos);
    }

    private Entity getTarget() {
        return target;
    }

    private void setTarget(Entity target) {
        this.target = target;
    }

    private float lerpRotation(float baseRotation, float targetRotation, float speed) {
        return Mth.rotLerp(speed, baseRotation, targetRotation);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
