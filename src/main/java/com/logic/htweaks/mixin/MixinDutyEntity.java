package com.logic.htweaks.mixin;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.BanditEntity;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.bridge.*;
import com.logic.htweaks.entity.ICharge;
import com.logic.htweaks.entity.behavior.*;
import com.logic.htweaks.entity.sensor.ChunkSensor;
import com.logic.htweaks.entity.sensor.CoverSensor;
import com.logic.htweaks.entity.sensor.FlankSensor;
import com.logic.htweaks.entity.vehicle.IVehicleEntity;
import com.logic.htweaks.faction.Factions;
import com.tacz.guns.init.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.mcreator.halo_mde.init.HaloMdeModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.IncomingProjectilesSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(DutyEntity.class)
public abstract class MixinDutyEntity  extends AbstractScavEntity  implements IScavFaction, INPCSPatrol, IGrenade, IAllyChecker, IPatrolLeader {


    @Shadow(remap=false)
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(4, 6);

    protected MixinDutyEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void init(CallbackInfo cir) {
        ((IScavFaction)this).htweaks$setFaction(Factions.INSURRECTIONIST);

        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack((ItemLike) HaloMdeModItems.CHEAP_ARMOR_HELMET.get()));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack((ItemLike)HaloMdeModItems.CHEAP_ARMOR_CHESTPLATE.get()));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack((ItemLike)HaloMdeModItems.CHEAP_ARMOR_LEGGINGS.get()));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack((ItemLike)HaloMdeModItems.CHEAP_ARMOR_BOOTS.get()));
    }

    /**
     * @author LogicalMaximus
     * @reason Adds Faction Functionality
     */
    @Overwrite(remap=false)
    public List<? extends ExtendedSensor<? extends AbstractScavEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<DutyEntity>().setScanRate((e) -> 10),
                new CoverSensor<DutyEntity>().setRadius(16).setScanRate((e) -> 100),
                new ChunkSensor<DutyEntity>().setScanRate((e) -> 100),
                new FlankSensor<DutyEntity>().setScanRate((e) -> 100),
                new IncomingProjectilesSensor<DutyEntity>()
                        .setPredicate((target, entity) -> (!((IAllyChecker)entity).isAlly(target.getOwner())) // Keep track of nearby players
                        ).setScanRate((e) -> 10), // Keep track of nearby players
                new NearbyLivingEntitySensor<DutyEntity>().setRadius(256) // Keep track of nearby entities the Skeleton is interested in
                        .setPredicate((target, entity) ->
                                (target instanceof Player player && !player.isCreative()) ||
                                        target instanceof Monster ||
                                            (target instanceof AbstractScavEntity abstractScavEntity && !abstractScavEntity.deadAsContainer && ((IScavFaction)entity).htweaks$getFaction().getEnemyFactions().contains(((IScavFaction)abstractScavEntity).htweaks$getFaction())) ||
                                        (target instanceof IVehicleEntity<?> vehicle && vehicle.getEntity().getControllingPassenger() instanceof AbstractScavEntity || target instanceof IVehicleEntity<?> vehicle2 && vehicle2.getEntity().getControllingPassenger() instanceof Player))
                        .setScanRate((e) -> 10));

    }

    /**
     * @author LogicalMaximus
     * @reason Adds Faction Functionality
     */
    @Overwrite(remap=false)
    public void setTarget(LivingEntity pLivingEntity) {
        if(((IAllyChecker)this).isAlly(pLivingEntity)) {
            if(BrainUtils.getTargetOfEntity(this) == pLivingEntity) {
                BrainUtils.clearMemory(this, MemoryModuleType.ATTACK_TARGET);
            }

            pLivingEntity = null;
        }

        if (this.getTarget() == null && pLivingEntity != null) {
            ALERT_INTERVAL.sample(this.random);
        }

        if (pLivingEntity instanceof Player) {
            this.setLastHurtByPlayer((Player)pLivingEntity);
        }

        super.setTarget(pLivingEntity);
    }

    /**
     * @author LogicalMaximus
     * @reason Adds More Fuctionality To Movement AI
     */
    @Overwrite(remap=false)
    public BrainActivityGroup<? extends AbstractScavEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new Behavior[]{(new AvoidProjectile()), (new AvoidEntity()).noCloserThan(64.0F).avoiding((entity) -> {
            return entity == this.getTarget();
        }).startCondition((e) -> !(((AbstractScavEntity)e) instanceof ICharge)), (new FollowEntity()).following((entity) -> {
            if(this.htweaks$getPatrolLeader() != null) {
                return this.htweaks$getPatrolLeader();
            }

            return null;
        }).stopFollowingWithin(24.0).speedMod(1.1F), (new TaczTargetShooter()), (new TargetOrRetaliate()).isAllyIf((e, l) -> {
            return this.isAlly((Entity) l) && !(l instanceof Player);
        }).attackablePredicate((l) -> {
            return l != null && this.hasLineOfSight((LivingEntity)l);
        }).alertAlliesWhen((m, e) -> {
            return e != null && ((LivingEntity)m).hasLineOfSight((LivingEntity)e);
        }).runFor((e) -> {
            return 999;
        }).stopIf((e) -> {
            LivingEntity patt13085$temp = ((Mob)e).getTarget();
            boolean var10000;
            if (patt13085$temp instanceof AbstractScavEntity scav) {
                if (scav.deadAsContainer) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }),(new TaczSetWalkToWaypoint()) , (new TaczSetWalkTargetToMovePos()), (new TaczSetWalkToMountTarget()), (new TaczMountTarget()), (new LookAtTarget()).runFor((entity) -> {
            return RandomSource.create().nextInt(40, 300);
        }).stopIf((e) -> {
            LivingEntity patt13702$temp = ((Mob)e).getTarget();
            boolean var10000;
            if (patt13702$temp instanceof AbstractScavEntity scav) {
                if (scav.deadAsContainer) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }), (new TaczDismount()).startCondition((e) -> {
            if(((AbstractScavEntity)e).getVehicle() != null && ((AbstractScavEntity)e).getTarget() != null && this.getVehicle().getPassengers().indexOf(this) > 2) {
                return true;
            }

            return false;
        }).stopIf((e) -> {
            if(((AbstractScavEntity)e).getVehicle() == null) {
                return true;
            }

            return false;
        }), new OneRandomBehaviour(new ExtendedBehaviour[]{(new StrafeTarget()).speedMod(0.80F).strafeDistance(30.0F).stopStrafingWhen((entity) -> {
            return ((AbstractScavEntity)entity).getTarget() == null || !((AbstractScavEntity)entity).getMainHandItem().is((Item) ModItems.MODERN_KINETIC_GUN.get());
        }).startCondition((e) -> {
            return ((AbstractScavEntity)e).getMainHandItem().is((Item)ModItems.MODERN_KINETIC_GUN.get()) && ((AbstractScavEntity)e).distanceTo(BrainUtils.getTargetOfEntity(((AbstractScavEntity)e))) < 64F && BrainUtils.getTargetOfEntity(((AbstractScavEntity)e)).hasLineOfSight(((AbstractScavEntity)e));
        }), (new SetWalkTargetToAttackTarget()).startCondition((e) -> this instanceof ICharge  || ((AbstractScavEntity)e).getVehicle() != null), (new TaczCaptureChunk()), new MoveToWalkTarget()})});
    }

}
