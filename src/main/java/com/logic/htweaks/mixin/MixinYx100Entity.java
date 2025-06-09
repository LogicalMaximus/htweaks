package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.config.server.VehicleConfig;
import com.atsuishio.superbwarfare.entity.C4Entity;
import com.atsuishio.superbwarfare.entity.projectile.*;
import com.atsuishio.superbwarfare.entity.vehicle.Yx100Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.ContainerMobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.LandArmorEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.WeaponVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.damage.DamageModifier;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.CannonShellWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.ProjectileWeapon;
import com.atsuishio.superbwarfare.entity.vehicle.weapon.SwarmDroneWeapon;
import com.atsuishio.superbwarfare.init.ModDamageTypes;
import com.atsuishio.superbwarfare.init.ModItems;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.network.message.receive.ShakeClientMessage;
import com.atsuishio.superbwarfare.tools.Ammo;
import com.atsuishio.superbwarfare.tools.SeekTool;
import com.logic.htweaks.entity.IShootableVehicle;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.Comparator;

@Mixin(Yx100Entity.class)
public abstract class MixinYx100Entity extends ContainerMobileVehicleEntity implements GeoEntity, LandArmorEntity, WeaponVehicleEntity, IShootableVehicle {

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> MG_AMMO;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> LOADED_AP;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> LOADED_HE;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> LOADED_AMMO_TYPE;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> GUN_FIRE_TIME;

    @Shadow(remap=false)
    public static EntityDataAccessor<Integer> LOADED_DRONE;

    @Shadow(remap=false)
    private int droneReloadCoolDown;

    public MixinYx100Entity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void htweaks$entityShoot(LivingEntity shooter) {
        Level level = shooter.level();

        for(int j = 0; j <= 2; j++) {
            if (j == 0) {
                if (this.reloadCoolDown == 0) {
                    Matrix4f transform = this.getBarrelTransform(1.0F);
                    Vector4f worldPosition = this.transformPosition(transform, 0.0F, 0.0F, 0.0F);

                    CannonShellWeapon cannonShell = new CannonShellWeapon();
                    CannonShellEntity entityToSpawn = (new CannonShellEntity(shooter, level, cannonShell.hitDamage, cannonShell.explosionRadius, cannonShell.explosionDamage, cannonShell.fireProbability, cannonShell.fireTime, cannonShell.gravity)).durability(cannonShell.durability);;
                    entityToSpawn.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                    entityToSpawn.shoot(this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.005F, this.getBarrelVector(1.0F).z, cannonShell.velocity, 0.02F);
                    this.level().addFreshEntity(entityToSpawn);

                    if (!level.isClientSide) {
                        level.playSound(null, this.blockPosition(), (SoundEvent) ModSounds.YX_100_FIRE_3P.get(), SoundSource.NEUTRAL, 8.0F, 1.0F);
                        level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.YX_100_FAR.get(), SoundSource.NEUTRAL,16.0F, 1.0F);
                        level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.YX_100_VERYFAR.get(), SoundSource.NEUTRAL, 32.0F, 1.0F);
                    }

                    this.entityData.set(CANNON_RECOIL_TIME, 40);

                    this.consumeEnergy(10000);
                    this.entityData.set(YAW, this.getTurretYRot());
                    this.reloadCoolDown = 80;
                    Level swarmDroneEntity = this.level();
                    if (swarmDroneEntity instanceof ServerLevel) {
                        ServerLevel server = (ServerLevel)swarmDroneEntity;
                        server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX() + (double)5.0F * this.getBarrelVector(1.0F).x, this.getY() + 0.1, this.getZ() + (double)5.0F * this.getBarrelVector(1.0F).z, 300, (double)6.0F, 0.02, (double)6.0F, 0.005);
                        double x = (double)worldPosition.x + (double)9.0F * this.getBarrelVector(1.0F).x;
                        double y = (double)worldPosition.y + (double)9.0F * this.getBarrelVector(1.0F).y;
                        double z = (double)worldPosition.z + (double)9.0F * this.getBarrelVector(1.0F).z;
                        server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);
                        server.sendParticles(ParticleTypes.CLOUD, x, y, z, 10, 0.4, 0.4, 0.4, 0.0075);
                        int count = 6;

                        for(float i = 9.5F; i < 23.0F; i += 0.5F) {
                            server.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)worldPosition.x + (double)i * this.getBarrelVector(1.0F).x, (double)worldPosition.y + (double)i * this.getBarrelVector(1.0F).y, (double)worldPosition.z + (double)i * this.getBarrelVector(1.0F).z, Mth.clamp(count--, 1, 5), 0.15, 0.15, 0.15, 0.0025);
                        }

                        Vector4f worldPositionL = this.transformPosition(transform, -0.35F, 0.0F, 0.0F);
                        Vector4f worldPositionR = this.transformPosition(transform, 0.35F, 0.0F, 0.0F);

                        for(float i = 3.0F; i < 6.0F; i += 0.5F) {
                            server.sendParticles(ParticleTypes.CLOUD, (double)worldPositionL.x + (double)i * this.getBarrelVector(1.0F).x, (double)worldPositionL.y + (double)i * this.getBarrelVector(1.0F).y, (double)worldPositionL.z + (double)i * this.getBarrelVector(1.0F).z, 1, 0.025, 0.025, 0.025, 0.0015);
                            server.sendParticles(ParticleTypes.CLOUD, (double)worldPositionR.x + (double)i * this.getBarrelVector(1.0F).x, (double)worldPositionR.y + (double)i * this.getBarrelVector(1.0F).y, (double)worldPositionR.z + (double)i * this.getBarrelVector(1.0F).z, 1, 0.025, 0.025, 0.025, 0.0015);
                        }
                    }

                    Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                    for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)8.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                        if (target instanceof ServerPlayer) {
                            ServerPlayer serverPlayer = (ServerPlayer)target;
                            Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)10.0F, (double)8.0F, (double)60.0F, this.getX(), this.getEyeY(), this.getZ()));
                        }
                    }
                }

                if (this.cannotFireCoax) {
                    continue;
                }

                Matrix4f transform = this.getBarrelTransform(1.0F);
                Vector4f worldPosition = this.transformPosition(transform, -0.12F, 0.15F, 2.0F);

                ProjectileEntity projectileRight = new ProjectileWeapon().create(shooter).setGunItemId(this.getType().getDescriptionId() + ".1");
                projectileRight.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                this.shoot(projectileRight, this.getBarrelVector(1.0F).x, this.getBarrelVector(1.0F).y + (double)0.005F, this.getBarrelVector(1.0F).z, 36.0F, 0.25F);

                this.level().addFreshEntity(projectileRight);

                this.entityData.set(COAX_HEAT, (Integer)this.entityData.get(COAX_HEAT) + 4);
                this.entityData.set(FIRE_ANIM, 2);

                float pitch = (Integer)this.entityData.get(COAX_HEAT) <= 60 ? 1.0F : (float)((double)1.0F - 0.011 * (double) org.joml.Math.abs(60 - (Integer)this.entityData.get(COAX_HEAT)));

                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_FIRE_3P.get(), SoundSource.NEUTRAL, 4.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_FAR.get(), SoundSource.NEUTRAL, 12.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_VERYFAR.get(), SoundSource.NEUTRAL, 24.0F, pitch);
                }
            }

            if (j == 1) {
                if (this.cannotFire) {
                    continue;
                }

                Matrix4f transform = this.getGunTransform(1.0F);
                Vector4f worldPosition = this.transformPosition(transform, 0.0F, -0.25F, 0.0F);

                ProjectileWeapon projectile = new ProjectileWeapon();

                ProjectileEntity projectileEntity = projectile.create(shooter).setGunItemId(this.getType().getDescriptionId() + ".2");
                projectileEntity.setPos((double)worldPosition.x - 1.1 * this.getDeltaMovement().x, (double)worldPosition.y, (double)worldPosition.z - 1.1 * this.getDeltaMovement().z);
                projectileEntity.shoot(this.getGunnerVector(1.0F).x, this.getGunnerVector(1.0F).y + (double)0.01F, this.getGunnerVector(1.0F).z, 20.0F, 0.3F);

                this.level().addFreshEntity(projectileEntity);

                float pitch = (Integer)this.entityData.get(HEAT) <= 60 ? 1.0F : (float)((double)1.0F - 0.011 * (double) Math.abs(60 - (Integer)this.entityData.get(HEAT)));

                if (!level.isClientSide) {
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_FIRE_3P.get(), SoundSource.NEUTRAL, 4.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_FAR.get(), SoundSource.NEUTRAL, 12.0F, pitch);
                    level.playSound(null, this.blockPosition(), (SoundEvent)ModSounds.M_2_VERYFAR.get(), SoundSource.NEUTRAL, 24.0F, pitch);
                }

                this.entityData.set(GUN_FIRE_TIME, 2);
                this.entityData.set(HEAT, (Integer)this.entityData.get(HEAT) + 4);

                Vec3 center = new Vec3(this.getX(), this.getEyeY(), this.getZ());

                for(Entity target : level.getEntitiesOfClass(Entity.class, (new AABB(center, center)).inflate((double)4.0F), (e) -> true).stream().sorted(Comparator.comparingDouble((e) -> e.distanceToSqr(center))).toList()) {
                    if (target instanceof ServerPlayer) {
                        ServerPlayer serverPlayer = (ServerPlayer)target;
                        Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ShakeClientMessage((double)6.0F, (double)4.0F, (double)6.0F, this.getX(), this.getEyeY(), this.getZ()));
                    }
                }
            }

            if (j == 2) {
                Matrix4f transformT = this.getTurretTransform(1.0F);
                Vector4f worldPosition = this.transformPosition(transformT, -1.6290874F, 0.75536877F, -1.7661687F);

                SwarmDroneEntity swarmDroneEntity;
                label146: {
                    Entity lookingEntity;
                    label145: {
                        Vec3 lookVec = shooter.getViewVector(1.0F);
                        lookingEntity = SeekTool.seekLivingEntity(shooter, this.level(), (double)384.0F, (double)6.0F);
                        swarmDroneEntity = new SwarmDroneWeapon().create(shooter);
                        Vector4f shootPosition1 = this.transformPosition(transformT, 0.0F, 0.0F, 0.0F);
                        Vector4f shootPosition2 = this.transformPosition(transformT, 0.0F, 1.0F, 0.0F);
                        Vec3 direct = (new Vec3((double)shootPosition1.x, (double)shootPosition1.y, (double)shootPosition1.z)).vectorTo(new Vec3((double)shootPosition2.x, (double)shootPosition2.y, (double)shootPosition2.z));
                        swarmDroneEntity.setPos((double)worldPosition.x, (double)worldPosition.y, (double)worldPosition.z);
                        swarmDroneEntity.shoot(direct.x, direct.y, direct.z, 1.2F, 10.0F);
                        if (lookingEntity != null) {
                            if (!(lookingEntity instanceof SwarmDroneEntity)) {
                                break label145;
                            }

                            SwarmDroneEntity swarmDrone = (SwarmDroneEntity)lookingEntity;
                            if (swarmDrone.getOwner() != shooter) {
                                break label145;
                            }
                        }

                        swarmDroneEntity.setGuideType(1);
                        BlockHitResult result = this.level().clip(new ClipContext(shooter.getEyePosition(), shooter.getEyePosition().add(lookVec.scale((double)384.0F)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                        Vec3 hitPos = result.getLocation();
                        swarmDroneEntity.setTargetVec(hitPos);
                        break label146;
                    }

                    swarmDroneEntity.setGuideType(0);
                    swarmDroneEntity.setTargetUuid(lookingEntity.getStringUUID());
                    swarmDroneEntity.setTargetVec(lookingEntity.getEyePosition());
                }

                shooter.level().addFreshEntity(swarmDroneEntity);

                this.level().playSound((Player)null, BlockPos.containing(new Vec3((double)worldPosition.x, (double)worldPosition.y, (double)worldPosition.z)), (SoundEvent)ModSounds.DECOY_FIRE.get(), SoundSource.PLAYERS, 1.0F, this.random.nextFloat() * 0.05F + 1.0F);

                this.entityData.set(LOADED_DRONE, (Integer)this.getEntityData().get(LOADED_DRONE) - 1);

                this.droneReloadCoolDown = 100;
            }
        }
    }

    @Shadow(remap=false)
    private Matrix4f getTurretTransform(float v) {
        throw new AssertionError();
    }

    @Shadow(remap=false)
    private Vec3 getGunnerVector(float v) {
        throw new AssertionError();
    }

    @Shadow(remap=false)
    private Matrix4f getGunTransform(float v) {
        throw new AssertionError();
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

    public DamageModifier getDamageModifier() {
        return super.getDamageModifier().custom((source, damage) -> this.getSourceAngle(source, 1.0F) * damage).custom((source, damage) -> {
            if (source.getDirectEntity() instanceof EntityKineticBullet bullet) {
                if(((KineticBulletAccessor)bullet).getExplosion()) {
                    return 2.5F * damage;
                }
            }

            return damage;
        });
    }
}
