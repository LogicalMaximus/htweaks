package com.logic.htweaks.event;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.atsuishio.superbwarfare.init.ModParticleTypes;
import com.atsuishio.superbwarfare.init.ModSounds;
import com.atsuishio.superbwarfare.tools.ParticleTool;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.MiltaryCommanderManager;
import com.logic.htweaks.commands.CaptureChunkCommand;
import com.logic.htweaks.commands.ClearNPCSCommand;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.entity.flood.AbstractFloodTrooper;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.mixin.KineticBulletAccessor;
import com.logic.htweaks.patrols.PatrolSavedData;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.teamabyssalofficial.entity.categories.BaseForm;
import net.teamabyssalofficial.entity.categories.BodyEntity;
import net.teamabyssalofficial.registry.EffectRegistry;

import java.util.*;

@Mod.EventBusSubscriber(modid = Htweaks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void onLivingHurt(LivingHurtEvent event) {
        if(event.getEntity().getLastHurtMob() instanceof BaseForm || event.getEntity().getLastHurtMob() instanceof BodyEntity) {
            if(event.getSource().getDirectEntity() instanceof EntityKineticBullet bullet) {
                if(!((KineticBulletAccessor)bullet).getExplosion()) {
                    event.setAmount(event.getAmount() * HTServerConfig.FLOOD_DAMAGE_RESISTANCE.get());
                }
            }
        }
        else if(event.getSource().getDirectEntity() instanceof EntityKineticBullet bullet && event.getSource().getEntity() instanceof AbstractFloodTrooper entity) {
            LivingEntity victim = entity.getLastHurtMob();

            if(victim != null) {
                victim.addEffect(new MobEffectInstance(EffectRegistry.PLAGUE_OF_THE_FLOOD.get(), 600, 0, true, true, true ));
                victim.addEffect(new MobEffectInstance(MobEffects.POISON, 150, 0, true, true, true));
            }
        }
    }

    @SubscribeEvent
    public static void onTargetChangeEvent(LivingChangeTargetEvent event) {
        if(event.getEntity() instanceof BaseForm form) {

            MiltaryCommanderManager manager = Htweaks.getMiltaryCommanderManager();

            if(manager != null) {
                MiltaryAiCommander commander = manager.getCommander(Factions.FLOOD);

                if(commander != null) {
                    List<BlockPos> enemyPositions = commander.getMemory(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get()).orElse(null);

                    LivingEntity target = form.getTarget();

                    if(target != null) {
                        BlockPos pos = target.blockPosition();

                        if(enemyPositions != null && !enemyPositions.contains(pos)) {
                            enemyPositions.add(pos);

                            commander.setMemoryWithExpiry(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(), enemyPositions, HTServerConfig.COMMANDER_REMEMBER_TIME.get());
                        }
                        else {
                            List<BlockPos> newEnemyPositions = new ArrayList<>();

                            newEnemyPositions.add(pos);

                            commander.setMemoryWithExpiry(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(), newEnemyPositions, HTServerConfig.COMMANDER_REMEMBER_TIME.get());
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBulletHit(EntityHurtByGunEvent.Pre event) {
        if(((KineticBulletAccessor)event.getBullet()).getExplosion()) {
            if(event.getHurtEntity() instanceof VehicleEntity vehicle) {
                vehicle.onHurt(event.getBaseAmount(), event.getAttacker(), true);

                if (vehicle.sendFireStarParticleOnHurt()) {
                    Level var5 = vehicle.level();
                    if (var5 instanceof ServerLevel) {
                        ServerLevel serverLevel = (ServerLevel)var5;
                        ParticleTool.sendParticle(serverLevel, (SimpleParticleType) ModParticleTypes.FIRE_STAR.get(), vehicle.getX(), vehicle.getY() + (double)0.5F * (double)vehicle.getBbHeight(), vehicle.getZ(), 2, 0.4, 0.4, 0.4, 0.2, false);
                    }
                }

                if (vehicle.playHitSoundOnHurt()) {
                    vehicle.level().playSound((Player)null, vehicle.getOnPos(), (SoundEvent) ModSounds.HIT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public static void onEntitySpawn(MobSpawnEvent event) {
        Mob entity = event.getEntity();
        ServerLevel level = event.getLevel().getLevel();

        if(COVENANT_ENTITIES.contains(entity.getType())) {
            CovenantEliteEntity newEntity = ModEntities.COVENANT_ELITE.get().create(level);

            newEntity.setPos(entity.getPosition(1.0F));

            level.addFreshEntity(newEntity);
            entity.discard();
        }
        else if(UNSC_ENTITIES.contains(entity.getType())) {
            UNSCMarineEntity newEntity = ModEntities.UNSC_MARINE.get().create(level);

            newEntity.setPos(entity.getPosition(1.0F));

            level.addFreshEntity(newEntity);
            entity.discard();
        }
    }
     */

    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        PatrolSavedData patrolManager = Htweaks.getPatrolManager();

        if(patrolManager != null) {
            patrolManager.tick();
        }

        MiltaryCommanderManager manager = Htweaks.getMiltaryCommanderManager();

        if(manager != null) {
            manager.tick();
        }
    }

    @SubscribeEvent
    public static void onLevelSave(LevelEvent.Save event) {
        PatrolSavedData patrolManager = Htweaks.getPatrolManager();

        if(patrolManager != null) {
            patrolManager.setDirty();
        }

        MiltaryCommanderManager manager = Htweaks.getMiltaryCommanderManager();

        if(manager != null) {
            manager.setDirty();
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        //SetHeadquartersPosCommand.register(event.getDispatcher());
        //GetHeadquartersPosCommand.register(event.getDispatcher());
        ClearNPCSCommand.register(event.getDispatcher());
    }

    /*
    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        HitResult result = event.getRayTraceResult();
        if (result.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult)result).getEntity();
            if (!entity.level().isClientSide && entity instanceof LivingEntity) {
                hitList.put(entity, Pair.of(event.getEntity(), event.getRayTraceResult()));
            }
        }
    }

    @SubscribeEvent
    public static void registerCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity obj = (Entity)event.getObject();
        if (obj instanceof LivingEntity livingEntity) {
            event.addCapability(CapProvider.IDENTIFIER, new CapProvider());
            //livingEntity.entityData = new SynchedEntityDataWrapper(livingEntity, livingEntity.entityData);
        }

    }
     */

}
