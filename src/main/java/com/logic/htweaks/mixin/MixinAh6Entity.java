package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.entity.projectile.HeliRocketEntity;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.HelicopterEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.HeliRocketWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import com.atsuishio.superbwarfare.tools.InventoryTool;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.logic.htweaks.entity.IShootableVehicle;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Comparator;

@Mixin(Ah6Entity.class)
public abstract class MixinAh6Entity extends ContainerMobileVehicleEntity implements GeoEntity, HelicopterEntity, WeaponVehicleEntity, IShootableVehicle {

    public MixinAh6Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void htweaks$entityShoot(LivingEntity shooter) {
        Matrix4f transform = this.getVehicleTransform(1.0F);
        Level level = this.level();

        for(int i = 0; i <= 1; i++) {
            if (i == 0) {
                if (this.cannotFire) {
                    continue;
                }

                float x = 1.15F;
                float y = -0.83000004F;
                float z = 0.8F;

                Vector4f worldPositionRight = this.transformPosition(transform, -x, y, z);
                Vector4f worldPositionLeft = this.transformPosition(transform, x, y, z);

                ProjectileEntity projectileRight = new ProjectileWeapon().create(shooter).setGunItemId(this.getType().getDescriptionId());
                projectileRight.setPos((double)worldPositionRight.x, (double)worldPositionRight.y, (double)worldPositionRight.z);
                this.shoot(projectileRight, this.getLookAngle().x, this.getLookAngle().y + 0.018, this.getLookAngle().z, 20.0F, 0.2F);
                this.level().addFreshEntity(projectileRight);

                ParticleTool.sendParticle((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, (double)worldPositionRight.x, (double)worldPositionRight.y, (double)worldPositionRight.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, false);


                ProjectileEntity projectileLeft = new ProjectileWeapon().create(shooter).setGunItemId(this.getType().getDescriptionId());
                projectileLeft.setPos((double)worldPositionLeft.x, (double)worldPositionLeft.y, (double)worldPositionLeft.z);
                this.shoot(projectileLeft, this.getLookAngle().x, this.getLookAngle().y + 0.018, this.getLookAngle().z, 20.0F, 0.2F);
                this.level().addFreshEntity(projectileLeft);
                ParticleTool.sendParticle((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, (double)worldPositionLeft.x, (double)worldPositionLeft.y, (double)worldPositionLeft.z, 1, (double)0.0F, (double)0.0F, (double)0.0F, (double)0.0F, false);

                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 5);
                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent) ModSounds.HELICOPTER_CANNON_FIRE_3P.get(), SoundSource.NEUTRAL, 4.0F, 1.0F);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.HELICOPTER_CANNON_FAR.get(), SoundSource.NEUTRAL, 12.0F, 1.0F);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.HELICOPTER_CANNON_VERYFAR.get(), SoundSource.NEUTRAL, 24.0F, 1.0F);
                }

                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)6.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)target;
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)6.0F, (double)5.0F, (double)7.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }
            } else if (i == 1) {
                float x = 1.7F;
                float y = -0.83000004F;
                float z = 0.8F;

                Vector4f worldPositionRight = this.transformPosition(transform, -x, y, z);
                Vector4f worldPositionLeft = this.transformPosition(transform, x, y, z);

                HeliRocketEntity heliRocketEntity = new HeliRocketWeapon().create(shooter);
                heliRocketEntity.setPos((double)worldPositionRight.x, (double)worldPositionRight.y, (double)worldPositionRight.z);
                heliRocketEntity.shoot(this.getLookAngle().x, this.getLookAngle().y + 0.008, this.getLookAngle().z, 7.0F, 0.25F);
                level.addFreshEntity(heliRocketEntity);

                heliRocketEntity.setPos((double)worldPositionLeft.x, (double)worldPositionLeft.y, (double)worldPositionLeft.z);
                heliRocketEntity.shoot(this.getLookAngle().x, this.getLookAngle().y + 0.008, this.getLookAngle().z, 7.0F, 0.25F);
                level.addFreshEntity(heliRocketEntity);

                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.HELICOPTER_ROCKET_FIRE_3P.get(), SoundSource.NEUTRAL, 6.0F, 1.0F);
                }

                this.reloadCoolDown = 30;

                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)6.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)target;
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)6.0F, (double)5.0F, (double)7.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }
            }
        }
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
