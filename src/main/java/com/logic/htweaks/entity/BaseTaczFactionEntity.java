package com.logic.htweaks.entity;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.IAllyChecker;
import com.logic.htweaks.bridge.INPCSPatrol;
import com.logic.htweaks.bridge.IPatrolLeader;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.entity.sensor.ChunkSensor;
import com.logic.htweaks.entity.sensor.CoverSensor;
import com.logic.htweaks.entity.sensor.FlankSensor;
import com.logic.htweaks.entity.vehicle.IVehicleEntity;
import com.logic.htweaks.registries.ModAttributes;
import com.tacz.guns.item.ModernKineticGunItem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.phys.AABB;
import net.teamabyssalofficial.entity.categories.BodyEntity;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.IncomingProjectilesSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class BaseTaczFactionEntity extends AbstractScavEntity {

    private boolean angry = false;


    protected BaseTaczFactionEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);

        this.applyArmor();
    }

    public abstract String getLootTableName();

    protected abstract void applyArmor();

    public boolean allowInventory(Player player) {
        if (this.deadAsContainer) {
            return true;
        } else {
            return this.lastHurtByPlayer != player;
        }
    }



    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);

        controllers.add(new AnimationController<GeoAnimatable>(this, "throw_grenade", 5, (event) -> {
            return event.setAndContinue(!((AbstractScavEntity)event.getAnimatable()).isDeadOrDying() && !((AbstractScavEntity)event.getAnimatable()).deadAsContainer ? (((AbstractScavEntity)event.getAnimatable()).isSprinting() ? RawAnimation.begin().thenLoop("run") : (event.isMoving() ? RawAnimation.begin().thenLoop("walk") : (this.tacz$data.isAiming ? RawAnimation.begin().thenLoop("aim_upper") : RawAnimation.begin().thenLoop("idle")))) : RawAnimation.begin().thenPlayAndHold("death" + this.randomDeathNumber));
        }));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @org.jetbrains.annotations.Nullable SpawnGroupData p_21437_, @org.jetbrains.annotations.Nullable CompoundTag p_21438_) {
        double randomSkillLevel = this.getRandom().nextDouble();
        AttributeInstance attribute = this.getAttribute(ModAttributes.SKILL_LEVEL.get());

        if(attribute != null) {
            attribute.setBaseValue(HTServerConfig.AI_ACCURACY.get());
        }

        if (this.getServer() != null && !(this.getMainHandItem().getItem() instanceof ModernKineticGunItem)) {
            ObjectArrayList<ItemStack> stacks = this.getServer().getLootData().getLootTable(new ResourceLocation(Htweaks.MODID, this.getLootTableName())).getRandomItems((new LootParams.Builder(this.getServer().getLevel(this.level().dimension()))).create(LootContextParamSet.builder().build()));
            stacks.forEach((stack) -> {
                if (stack.getMaxDamage() != 0) {
                    stack.setDamageValue(RandomSource.create().nextInt(stack.getMaxDamage() / 2, stack.getMaxDamage()));
                }

                this.inventory.addItem(stack);
            });
        }

        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isAlly(pSource.getEntity())) {
            return false;
        } else {
            this.panic = true;
            this.paniccooldown = 60;
            Entity target = pSource.getEntity();

            return super.hurt(pSource, pAmount);
        }
    }

    public void tick() {
        if (this.getTarget() == null && this.angry) {
            this.angry = false;
        }

        super.tick();
    }

    public void setTarget(@Nullable LivingEntity pLivingEntity) {
        if(this.isAlly(pLivingEntity)) {
            if(BrainUtils.getTargetOfEntity(this) == pLivingEntity) {
                BrainUtils.clearMemory(this, MemoryModuleType.ATTACK_TARGET);
            }

            pLivingEntity = null;
        }

        if (pLivingEntity instanceof Player) {
            this.setLastHurtByPlayer((Player)pLivingEntity);
        }

        super.setTarget(pLivingEntity);
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        if(this.isAlly(entity)) {
            return false;
        }

        return super.canAttack(entity);
    }

    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if(!pPlayer.isCrouching() && !pPlayer.level().isClientSide) {
            if(this.isAlly(pPlayer)) {
                Entity patrolLeader = ((IPatrolLeader)this).htweaks$getPatrolLeader();

                if(patrolLeader != null) {
                    if(patrolLeader instanceof AbstractScavEntity) {
                        Entity superior = ((IPatrolLeader)patrolLeader).htweaks$getPatrolLeader();

                        while (superior != null) {
                            Entity manager = ((IPatrolLeader)superior).htweaks$getPatrolLeader();

                            if(manager instanceof AbstractScavEntity) {
                                superior = manager;
                            }
                            else if(manager instanceof Player) {
                                break;
                            }
                            else {
                                ((IPatrolLeader)superior).htweaks$setPatrolLeader(pPlayer);
                                ((INPCSPatrol)superior).htweaks$getPatrol().setPlayerID(pPlayer.getUUID());
                                pPlayer.sendSystemMessage(Component.literal("You Are Now The Leader Of " + superior.getName()));
                            }
                        }
                    }
                }
                else {
                    ((INPCSPatrol)this).htweaks$getPatrol().setPlayerID(pPlayer.getUUID());
                    ((IPatrolLeader)this).htweaks$setPatrolLeader(pPlayer);
                    pPlayer.sendSystemMessage(Component.literal("You Are Now The Leader Of " + this.getName()));
                }
            }
        }

        return super.mobInteract(pPlayer, pHand);
    }

    public List<? extends ExtendedSensor<? extends AbstractScavEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<BaseTaczFactionEntity>().setScanRate((e) -> 10),
                new IncomingProjectilesSensor<BaseTaczFactionEntity>()
                        .setPredicate((target, entity) -> (!((IAllyChecker)entity).isAlly(target.getOwner()))).setScanRate((e) -> 10),
                new CoverSensor<BaseTaczFactionEntity>().setRadius(16).setScanRate((e) -> 100),
                new ChunkSensor<BaseTaczFactionEntity>().setScanRate((e) -> 100),
                new FlankSensor<BaseTaczFactionEntity>().setScanRate((e) -> 100),
                new NearbyLivingEntitySensor<BaseTaczFactionEntity>().setRadius(256) // Keep track of nearby entities the Skeleton is interested in
                        .setPredicate((target, entity) ->
                                (target instanceof Player player && !player.isCreative()) ||
                                        target instanceof Monster && (!(target instanceof BodyEntity)) ||
                                            (target instanceof AbstractScavEntity abstractScavEntity && !abstractScavEntity.deadAsContainer && ((IScavFaction)entity).htweaks$getFaction().getEnemyFactions().contains(((IScavFaction)abstractScavEntity).htweaks$getFaction())))
                        .setScanRate((e) -> 10));
    }

    public boolean isAlly(Entity entity) {
        if(entity instanceof BaseTaczFactionEntity baseTaczFactionEntity) {
            if(((IScavFaction)baseTaczFactionEntity).htweaks$getFaction() == ((IScavFaction)this).htweaks$getFaction()) {
                return true;
            }
        }

        if(entity instanceof Player player) {
            if(((IScavFaction)player).htweaks$getFaction() == ((IScavFaction)this).htweaks$getFaction()) {
                return true;
            }
        }

        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = PathfinderMob.createMobAttributes();
        builder.add(ModAttributes.SKILL_LEVEL.get());
        builder.add(Attributes.MAX_HEALTH, 30);
        builder.add(Attributes.FOLLOW_RANGE, 160.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
        return builder;
    }
}
