package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.projectile.SmallCannonShellEntity;
import com.atsuishio.superbwarfare.entity.projectile.WgMissileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.Bmp2Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.WgMissileWeapon;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.logic.htweaks.entity.IShootableVehicle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Comparator;

@Mixin(Bmp2Entity.class)
public abstract class MixinBmp2Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity, IShootableVehicle {

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> CANNON_FIRE_TIME;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> LOADED_MISSILE;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> MISSILE_COUNT;

    public MixinBmp2Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void htweaks$entityShoot(LivingEntity shooter) {
        Level level = this.level();

        Matrix4f transform = this.getBarrelTransform(1.0F);

        for(int i = 0; i <= 2; i++) {
            if (i == 0) {
                if (this.cannotFire) {
                    continue;
                }

                float x = -0.45F;
                float y = 0.4F;
                float z = 4.2F;
                Vector4f worldPosition = this.transformPosition(transform, x, y, z);
                SmallCannonShellEntity smallCannonShell = new SmallCannonShellEntity(shooter, level, VehicleConfig.BMP_2_CANNON_DAMAGE.get(), VehicleConfig.BMP_2_CANNON_EXPLOSION_DAMAGE.get(), 5, false);
                smallCannonShell.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                smallCannonShell.shoot(this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.005F, this.getBarrelVector(1.0F).z, 20.0F, 0.25F);
                this.level().addFreshEntity(smallCannonShell);
                ParticleTool.sendParticle((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, (double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z, 1, 0.02, 0.02, 0.02, (double)0.0F, false);
                float pitch = (Integer)this.entityData.get(HEAT) <= 60 ? 1.0F : (float)((double)1.0F - 0.011 * (double) Math.abs(60 - (Integer)this.entityData.get(HEAT)));

                if (!level.isClientSide) {
                    BlockPos pos = this.blockPosition();

                    level.playSound(null, pos, (SoundEvent) ModSounds.BMP_CANNON_FIRE_3P.get(), SoundSource.NEUTRAL, 4.0F, pitch);
                    level.playSound(null, pos, (SoundEvent)ModSounds.LAV_CANNON_FAR.get(), SoundSource.NEUTRAL, 12.0F, pitch);
                    level.playSound(null, pos, (SoundEvent)ModSounds.LAV_CANNON_VERYFAR.get(), SoundSource.NEUTRAL, 24.0F, pitch);
                }

                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)4.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer serverPlayer) {
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)6.0F, (double)5.0F, (double)9.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }

                this.entityData.set(CANNON_RECOIL_TIME, 40);
                this.entityData.set(YAW, this.getTurretYRot());
                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 7);
                this.entityData.set(FIRE_ANIM, 3);

            } else if (i == 1) {
                if (this.cannotFireCoax) {
                    continue;
                }

                float x = -0.2F;
                float y = 0.3F;
                float z = 1.2F;
                Vector4f worldPosition = this.transformPosition(transform, x, y, z);

                ProjectileEntity projectileRight = new ProjectileWeapon().create(shooter).setGunItemId(this.getType().getDescriptionId());
                projectileRight.bypassArmorRate(0.2F);
                projectileRight.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                this.shoot(projectileRight, this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.002F, this.getBarrelVector(1.0F).z, 36.0F, 0.25F);

                this.level().addFreshEntity(projectileRight);

                this.entityData.set(COAX_HEAT, (Integer)this.entityData.get(COAX_HEAT) + 3);
                this.entityData.set(FIRE_ANIM, 2);
                if (!level.isClientSide) {
                    BlockPos pos = this.blockPosition();

                    level.playSound(null, pos,(SoundEvent)ModSounds.M_60_FIRE_3P.get(), SoundSource.NEUTRAL, 3.0F, 1.0F);
                    level.playSound(null, pos,(SoundEvent)ModSounds.M_60_FAR.get(), SoundSource.NEUTRAL, 6.0F, 1.0F);
                    level.playSound(null, pos,(SoundEvent)ModSounds.M_60_VERYFAR.get(), SoundSource.NEUTRAL, 12.0F, 1.0F);
                }
            } else if (i == 2) {
                Matrix4f transformT = this.getBarrelTransform(1.0F);
                Vector4f worldPosition = this.transformPosition(transformT, 0.0F, 1.0F, 0.0F);
                WgMissileEntity wgMissileEntity = new WgMissileWeapon().create(shooter);
                wgMissileEntity.setPos((double)worldPosition.x, (double)worldPosition.y, (double)worldPosition.z);
                wgMissileEntity.shoot(this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y, this.getBarrelVector(1.0F).z, 2.0F, 0.0F);
                level.addFreshEntity(wgMissileEntity);
                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(),(SoundEvent)ModSounds.BMP_MISSILE_FIRE_3P.get(), SoundSource.NEUTRAL, 6.0F, 1.0F);
                }

                this.reloadCoolDown = 160;
            }
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
}
