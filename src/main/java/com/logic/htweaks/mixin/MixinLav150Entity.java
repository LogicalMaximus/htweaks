package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.entity.projectile.ProjectileEntity;
import com.atsuishio.superbwarfare.entity.projectile.SmallCannonShellEntity;
import com.atsuishio.superbwarfare.entity.vehicle.Lav150Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.SmallCannonShellWeapon;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
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
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Comparator;

@Mixin(Lav150Entity.class)
public abstract class MixinLav150Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity, IShootableVehicle {

    public MixinLav150Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void htweaks$entityShoot(LivingEntity shooter) {
        Level level = this.level();

        for(int i = 0; i <= 1; i++) {
            Matrix4f transform = this.getBarrelTransform(1.0F);
            if (i == 0) {
                if (this.cannotFire) {
                    return;
                }

                float x = 0.0609375F;
                float y = 0.0517F;
                float z = 3.0927625F;
                Vector4f worldPosition = this.transformPosition(transform, x, y, z);
                SmallCannonShellEntity smallCannonShell = new SmallCannonShellWeapon().create(shooter);
                smallCannonShell.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                smallCannonShell.shoot(this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.005F, this.getBarrelVector(1.0F).z, 20.0F, 0.25F);
                this.level().addFreshEntity(smallCannonShell);
                ParticleTool.sendParticle((ServerLevel)this.level(), ParticleTypes.LARGE_SMOKE, (double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z, 1, 0.02, 0.02, 0.02, (double)0.0F, false);
                float pitch = (Integer)this.entityData.get(HEAT) <= 60 ? 1.0F : (float)((double)1.0F - 0.011 * (double) Math.abs(60 - (Integer)this.entityData.get(HEAT)));
                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(),(SoundEvent) ModSounds.LAV_CANNON_FIRE_3P.get(), SoundSource.NEUTRAL, 4.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.LAV_CANNON_FAR.get(), SoundSource.NEUTRAL, 12.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.LAV_CANNON_VERYFAR.get(), SoundSource.NEUTRAL, 24.0F, pitch);
                }

                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)4.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)target;
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)6.0F, (double)5.0F, (double)9.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }

                this.entityData.set(CANNON_RECOIL_TIME, 40);
                this.entityData.set(YAW, this.getTurretYRot());
                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 7);
                this.entityData.set(FIRE_ANIM, 3);

                this.getItemStacks().stream().filter((stack) -> stack.is((Item) ModItems.SMALL_SHELL.get())).findFirst().ifPresent((stack) -> stack.shrink(1));
            } else if (i == 1) {
                if (this.cannotFireCoax) {
                    return;
                }

                float x = 0.3F;
                float y = 0.08F;
                float z = 0.7F;
                Vector4f worldPosition = this.transformPosition(transform, x, y, z);

                ProjectileEntity projectile = new ProjectileWeapon().create(shooter).setGunItemId(this.getType().getDescriptionId());
                projectile.bypassArmorRate(0.2F);
                projectile.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);

                this.shoot(projectile, this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.002F, this.getBarrelVector(1.0F).z, 36.0F, 0.25F);

                this.level().addFreshEntity(projectile);

                this.entityData.set(COAX_HEAT, (Integer)this.entityData.get(COAX_HEAT) + 3);
                this.entityData.set(FIRE_ANIM, 2);

                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(),(SoundEvent)ModSounds.RPK_FIRE_3P.get(), SoundSource.NEUTRAL, 3.0F, 1.0F);
                    level.playSound(null, this.blockPosition(),(SoundEvent)ModSounds.RPK_FAR.get(), SoundSource.NEUTRAL, 6.0F, 1.0F);
                    level.playSound(null, this.blockPosition(),(SoundEvent)ModSounds.RPK_VERYFAR.get(), SoundSource.NEUTRAL, 12.0F, 1.0F);
                }
            }
        }
    }

    @Shadow(remap=false)
    public Matrix4f getBarrelTransform(float ticks) {
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
