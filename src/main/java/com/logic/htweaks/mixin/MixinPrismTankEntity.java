package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.PrismTankEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.logic.htweaks.entity.IShootableVehicle;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Comparator;

@Mixin(PrismTankEntity.class)
public abstract class MixinPrismTankEntity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity, IShootableVehicle {

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> CANNON_FIRE_TIME;

    @Shadow(remap=false)
    public static EntityDataAccessor<Float> LASER_LENGTH;

    @Shadow(remap=false)
    public static EntityDataAccessor<Float> LASER_SCALE;

    @Shadow(remap=false)
    public static EntityDataAccessor<Float> LASER_SCALE_O;

    public MixinPrismTankEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void htweaks$entityShoot(LivingEntity shooter) {
        Level level = this.level();

        Matrix4f transform = this.getBarrelTransform(1.0F);
        Vector4f worldPosition = this.transformPosition(transform, 0.0F, 0.5F, 0.0F);
        Vec3 root = new Vec3((double)worldPosition.x, (double)worldPosition.y, (double)worldPosition.z);

        if (this.getWeaponIndex(0) == 0) {
            if (this.cannotFire) {
                return;
            }

            if (level instanceof ServerLevel) {
                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent) ModSounds.PRISM_FIRE_3P.get(), SoundSource.NEUTRAL, 5.0F, 1.0F);
                }

                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 55);
                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)5.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)target;
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)8.0F, (double)4.0F, (double)7.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }
            }

            float dis = this.laserLengthEntity(root);
            if (dis < this.laserLength(root)) {
                this.entityData.set(LASER_LENGTH, dis);
            } else {
                this.entityData.set(LASER_LENGTH, this.laserLength(root));
                this.hitBlock(root);
            }

            this.entityData.set(LASER_SCALE, 3.0F);

        } else if (this.getWeaponIndex(0) == 1) {
            if (this.cannotFire) {
                return;
            }

            if (!this.canConsume((Integer)VehicleConfig.PRISM_TANK_SHOOT_COST_MODE_2.get())) {
                return;
            }

            if (level instanceof ServerLevel) {
                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.PRISM_FIRE_3P_2.get(), SoundSource.NEUTRAL, 4.0F, 1.0F);
                }

                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 2);
                this.consumeEnergy((Integer)VehicleConfig.PRISM_TANK_SHOOT_COST_MODE_2.get());
            }

            Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

            for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)2.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                if (target instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)target;
                    Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)5.0F, (double)3.0F, (double)3.0F, this.getX(), this.getEyeY(), this.getZ()));
                }
            }

            float dis = this.laserLengthEntity(root);
            if (dis < this.laserLength(root)) {
                this.entityData.set(LASER_LENGTH, dis);
            } else {
                this.entityData.set(LASER_LENGTH, this.laserLength(root));
                this.hitBlock(root);
            }

            this.entityData.set(LASER_SCALE, 1.0F);
        }
    }

    @Shadow(remap=false)
    private Matrix4f getBarrelTransform(float v) {
        throw new AssertionError();
    }

    private void shoot(ProjectileEntity projectile, double vecX, double vecY, double vecZ, float velocity, float spread) {
        Vec3 vec3 = (new Vec3(vecX, vecY, vecZ)).normalize().add(this.random.triangle((double)0.0F, 0.0172275 * (double)spread), this.random.triangle((double)0.0F, 0.0172275 * (double)spread), this.random.triangle((double)0.0F, 0.0172275 * (double)spread)).scale((double)velocity);
        projectile.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        projectile.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float) java.lang.Math.PI)));
        projectile.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float) java.lang.Math.PI)));

        projectile.yRotO = projectile.getYRot();
        projectile.xRotO = projectile.getXRot();
    }

    @Shadow(remap=false)
    private float laserLengthEntity(Vec3 pos) {
        throw new AssertionError();
    }

    @Shadow(remap=false)
    private float laserLength(Vec3 pos) {
        throw new AssertionError();
    }

    @Shadow(remap=false)
    private void hitBlock(Vec3 pos) {
        throw new AssertionError();
    }
}
