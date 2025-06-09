package com.logic.htweaks.mixin;

import com.atsuishio.superbwarfare.entity.vehicle.base.HelicopterEntity;
import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.behavior.TaczShootAttack;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.*;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.entity.ICharge;
import com.logic.htweaks.entity.behavior.TaczSetWalkTargetToChunkPosition;
import com.logic.htweaks.entity.behavior.*;
import com.logic.htweaks.entity.navigation.TaczAirPathNavigation;
import com.logic.htweaks.entity.navigation.TaczMoveControl;
import com.logic.htweaks.entity.navigation.TaczPathNavigation;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModAttributes;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.utils.HtweaksUtils;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.GunTabType;
import com.tacz.guns.init.ModItems;
import net.mcreator.halo_mde.entity.FragmentationGrenadeEntity;
import net.mcreator.halo_mde.entity.PlasmaGrenadeEntity;
import net.mcreator.halo_mde.init.HaloMdeModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.*;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(AbstractScavEntity.class)
public abstract class MixinAbstractScavEntity extends PathfinderMob implements GeoEntity, SmartBrainOwner<AbstractScavEntity>, IGunOperator, InventoryCarrier, HasCustomInventoryScreen, MenuProvider, IScavFaction, INPCSPatrol, IGrenade, IAllyChecker, IPatrolLeader {

    @Shadow(remap=false)
    public boolean panic;

    @Shadow(remap=false)
    public int collectiveShots;

    @Shadow(remap=false)
    public boolean isPatrolLeader;

    @Unique
    private Entity leader;

    @Unique
    private Faction faction;

    @Unique
    private Patrol patrol;

    @Unique
    private Optional<UUID> patrolLeaderUUID;

    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new TaczPathNavigation(this, level);
    }

    @Unique
    protected @NotNull PathNavigation createAirNavigation(@NotNull Level level) {
        return new TaczPathNavigation(this, level);
    }

    public PathNavigation getNavigation() {
        return super.getNavigation();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityType p_21683_, Level p_21684_, CallbackInfo ci) {
        this.navigation = this.createNavigation(p_21684_);

        this.moveControl = new TaczMoveControl(this);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(!(this.navigation instanceof TaczAirPathNavigation) && this.getVehicle() instanceof HelicopterEntity) {
            this.navigation = this.createAirNavigation(level());
        }
        else if (!(this.navigation instanceof TaczPathNavigation)) {
            if(!(this.getVehicle() instanceof HelicopterEntity)) {
                this.navigation = this.createNavigation(level());
            }
        }

    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if(leader != null) {
            pCompound.putUUID("patrolLeaderUUID", this.leader.getUUID());
        }

        if(patrol != null) {
            pCompound.putUUID("patrolID", patrol.getUUID());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if(pCompound.contains("patrolLeaderUUID")) {
            UUID leaderUUID = pCompound.getUUID("patrolLeaderUUID");

            if(this.level() instanceof ServerLevel serverLevel) {
                leader = serverLevel.getEntity(leaderUUID);
            }

            patrolLeaderUUID = Optional.of(leaderUUID);
        }

        if(pCompound.contains("patrolID")) {
            patrol = Htweaks.getPatrolManager().getPatrol(pCompound.getUUID("patrolID"));
        }
    }

    @Inject(method = "die", at = @At("TAIL"))
    public void die(DamageSource pDamageSource, CallbackInfo ci) {
        if(HTServerConfig.DEBUG_MODE.get()) {
            this.sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }
    }


    @Override
    public void htweaks$clearPatrolLeader() {
        this.leader = null;
        this.patrolLeaderUUID = Optional.empty();
    }

    @Override
    public void htweaks$setPatrolLeader(@NotNull Entity patrolLeader) {
        this.leader = patrolLeader;

        patrolLeaderUUID = Optional.of(patrolLeader.getUUID());
    }

    @Override
    public @Nullable Entity htweaks$getPatrolLeader() {
        if(this.leader != null) {
            return leader;
        }
        else if(patrolLeaderUUID != null && patrolLeaderUUID.isPresent() && this.level() instanceof ServerLevel serverLevel) {
            this.leader = serverLevel.getEntity(patrolLeaderUUID.get());
        }

        return null;
    }

    protected MixinAbstractScavEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public Faction htweaks$getFaction() {
        return faction;
    }

    @Override
    public void htweaks$setFaction(Faction faction) {
        this.faction = faction;
    }

    @Override
    public Patrol htweaks$getPatrol() {
        return patrol;
    }

    @Override
    public void htweaks$setPatrol(Patrol patrol) {
        this.patrol = patrol;
    }
    
    /**
     * @author LogicalMaxims
     * @reason RAAAAAHH
     */
    @Overwrite(remap=false)
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if(pPlayer.isCrouching()) {
            if (this.allowInventory(pPlayer)) {
                this.openCustomInventoryScreen(pPlayer);
            }
        }

        return super.mobInteract(pPlayer, pHand);
    }

    @Shadow(remap=false)
    public abstract boolean allowInventory(Player var1);

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    public void hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {

    }

    @Override
    public boolean shouldFollowCommands(Entity entity) {
        if(entity != null && entity == this.leader) {
            return true;
        }

        if(patrol != null && !this.level().isClientSide) {
            Player player = this.level().getPlayerByUUID(patrol.getPlayerID());

            if(player != null) {
                return entity == player;
            }
        }

        return false;
    }

    /**
     * @author LogicalMaximus
     * @reason Change Idle AI Functionality
     */
    @Overwrite(remap=false)
    public BrainActivityGroup<? extends AbstractScavEntity> getIdleTasks() {
        return BrainActivityGroup.idleTasks(new Behavior[]{new FirstApplicableBehaviour(new ExtendedBehaviour[]{new TargetOrRetaliate(), new TaczSetWalkTargetToChunkPosition(), new SetPlayerLookTarget(), new SetRandomLookTarget()}), new OneRandomBehaviour(new ExtendedBehaviour[]{(new SetRandomWalkTarget()).speedModifier(1.0F).startCondition((e) -> !this.htweaks$shouldHoldPosition()), (new Idle()).runFor((entity) -> {
            return RandomSource.create().nextInt(30, 60);
        }).startCondition((e) -> !this.htweaks$shouldHoldPosition())})});
    }

    /**
     * @author LogicalMaximus
     * @reason Adds More Fuctionality To Attack AI
     */
    @Overwrite(remap=false)
    public BrainActivityGroup<? extends AbstractScavEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(new Behavior[]{new InvalidateAttackTarget(), (new SetWalkTargetToAttackTarget()).startCondition((entity) -> {
            return !this.getMainHandItem().is((Item) ModItems.MODERN_KINETIC_GUN.get());
        }), new SetRetaliateTarget(), new FirstApplicableBehaviour(new ExtendedBehaviour[]{(new TaczSwitchRPGWeaponBehavior()), (new TaczShootAttack(256)).stopIf((e) -> {
            LivingEntity patt19146$temp = ((PathfinderMob)e).getTarget();
            boolean var10000;
            if (patt19146$temp instanceof AbstractScavEntity scav) {
                if (scav.deadAsContainer) {
                    var10000 = true;
                    return var10000;
                }
            }

            var10000 = false;
            return var10000;
        }).startCondition((x$0) -> {
            return ((AbstractScavEntity)x$0).getMainHandItem().is((Item)ModItems.MODERN_KINETIC_GUN.get()) && !((AbstractScavEntity)x$0).panic && ((AbstractScavEntity)x$0).collectiveShots <= this.getStateBurst() && ((AbstractScavEntity)x$0).getVehicle() == null;
        }), (new TaczShootVehicle()), (new TaczSetWalkTargetToFlankPos().startCondition((e) -> ((AbstractScavEntity)e).getVehicle() == null)), (new TaczThrowGrenade()).cooldownFor((e) -> 3600).stopIf((e) -> {
            if(((AbstractScavEntity)e).getTarget() == null) {
                return true;
            }
            return false;
        }).startCondition((e) -> {
            if((!((AbstractScavEntity)e).panic && ((AbstractScavEntity)e).getVehicle() == null)) {
                return true;
            }
            return false;
        }),(new TaczSetWalkTargetCover().startCondition((e) -> ((AbstractScavEntity)e).getVehicle() == null)),(new AnimatableMeleeAttack(0)).whenStarting((entity) -> {
            ((AbstractScavEntity)entity).setAggressive(true);
        }).whenStopping((entity) -> {
            ((AbstractScavEntity)entity).setAggressive(false);
        })})});
    }

    @Override
    public boolean hasLineOfSight(Entity p_147185_) {
        if (p_147185_.level() != this.level()) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
            if (vec31.distanceTo(vec3) > 512.0) {
                return false;
            } else {
                return this.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
            }
        }
    }

    @Override
    public void htweaks$throwGrenade(LivingEntity target) {
        if(target == null)
            return;

        if(this.level() instanceof ServerLevel) {
            //this.triggerAnim("throw_grenade", "throw_grenade");
            Projectile grenade;

            if(((IScavFaction)this).htweaks$getFaction() == Factions.COVENANT) {
                grenade = new PlasmaGrenadeEntity(HaloMdeModEntities.PLASMA_GRENADE.get(), this, this.level());
            }
            else {
                grenade = new FragmentationGrenadeEntity(HaloMdeModEntities.FRAGMENTATION_GRENADE.get(), this, this.level());
            }

            double distance = this.distanceToSqr(target.getX(), target.getY(), target.getZ());
            double heightDiff = target.getY() - this.getY();
            double d0 = target.getX() - this.getX();
            double d1 = target.getY() - grenade.getY() + (double)target.getEyeHeight();
            double d2 = target.getZ() - this.getZ();
            double d3 = (double) Mth.sqrt((float)(d0 * d0 + d2 * d2));
            double angle = HtweaksUtils.getAngleDistanceModifier(distance, 47, 4) + HtweaksUtils.getAngleHeightModifier(distance, heightDiff, 1.0) / 100.0;
            float force = 1.9F + HtweaksUtils.getForceDistanceModifier(distance, 1.899999976158142);
            float accuracy = 0.35F;

            grenade.shoot(d0, d1 + d3 * angle, d2, force, accuracy);

            this.level().addFreshEntity(grenade);
        }
    }

    /**
     * @author LogicalMaximus
     * @reason Adds More Fuctionality To Movement AI
     */
    @Overwrite(remap=false)
    public BrainActivityGroup<? extends AbstractScavEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(new Behavior[]{(new AvoidProjectile().startCondition((e) -> ((AbstractScavEntity)e).getVehicle() == null)), (new AvoidEntity()).noCloserThan(64.0F).avoiding((entity) -> {
            return entity == this.getTarget();
        }).startCondition((e) -> !(((AbstractScavEntity)e) instanceof ICharge) && htweaks$isOutnumbered() || !this.htweaks$shouldHoldPosition()), (new FollowEntity()).following((entity) -> {
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
        }),(new TaczSetWalkToWaypoint().startCondition((e) -> ((AbstractScavEntity)e).getVehicle() == null)) , (new TaczSetWalkTargetToMovePos()), (new TaczSetWalkToMountTarget()), (new TaczMountTarget()), (new LookAtTarget()).runFor((entity) -> {
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
            if(((AbstractScavEntity)e).getVehicle() != null && ((AbstractScavEntity)e).getTarget() != null && this.getVehicle().getPassengers().indexOf(this) > 2 || htweaks$shouldDismount()) {
                return true;
            }

            return false;
        }).stopIf((e) -> {
            if(((AbstractScavEntity)e).getVehicle() == null) {
                return true;
            }

            return false;
        }), new OneRandomBehaviour(new ExtendedBehaviour[]{(new StrafeTarget()).speedMod(0.80F).strafeDistance(30.0F).stopStrafingWhen((entity) -> {
            return ((AbstractScavEntity)entity).getTarget() == null || !((AbstractScavEntity)entity).getMainHandItem().is((Item)ModItems.MODERN_KINETIC_GUN.get());
        }).startCondition((e) -> {
            return ((AbstractScavEntity)e).getMainHandItem().is((Item)ModItems.MODERN_KINETIC_GUN.get()) && !this.htweaks$shouldHoldPosition() && ((AbstractScavEntity)e).distanceTo(BrainUtils.getTargetOfEntity(((AbstractScavEntity)e))) < 64F && BrainUtils.getTargetOfEntity(((AbstractScavEntity)e)).hasLineOfSight(((AbstractScavEntity)e))  && ((AbstractScavEntity)e).getVehicle() == null;
        }), (new SetWalkTargetToAttackTarget()).startCondition((e) -> this instanceof ICharge && !this.htweaks$shouldHoldPosition() || ((AbstractScavEntity)e).getVehicle() != null || !this.htweaks$isOutnumbered()), (new TaczCaptureChunk()), new MoveToWalkTarget().startCondition((e) -> !this.htweaks$shouldHoldPosition())})});
    }

    @Shadow(remap=false)
    public GunTabType heldGunType() {
        throw new AssertionError();
    }

    @Overwrite(remap=false)
    int getStateBurst() {
        if (this.heldGunType() != null) {
            byte var10000;
            switch (this.heldGunType()) {
                case RIFLE:
                    var10000 = 2;
                    break;
                case PISTOL:
                    var10000 = 3;
                    break;
                case SNIPER:
                    var10000 = 1;
                    break;
                case SHOTGUN:
                    var10000 = 1;
                    break;
                case SMG:
                    var10000 = 4;
                    break;
                case MG:
                    var10000 = 6;
                    break;
                case RPG:
                    var10000 = 1;
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            return var10000;
        } else {
            return 1;
        }
    }

    @Shadow public abstract float getHealth();

    @Unique
    private boolean htweaks$shouldHoldPosition() {
        return Boolean.TRUE.equals(BrainUtils.getMemory(this, ModMemoryTypes.SHOULD_HOLD_POSITION.get()));
    }

    @Unique
    private boolean htweaks$shouldDismount() {
        return Boolean.TRUE.equals(BrainUtils.getMemory(this, ModMemoryTypes.SHOULD_DISMOUNT.get()));
    }

    @Override
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

    @Overwrite(remap=false)
    public boolean needCheckAmmo() {
        return HTServerConfig.CONSUME_AMMO_SCAV.get();
    }

    @Overwrite(remap=false)
    public static AttributeSupplier.@NotNull Builder createLivingAttributes() {
        AttributeSupplier.Builder builder = PathfinderMob.createMobAttributes();
        builder.add(ModAttributes.SKILL_LEVEL.get());
        builder.add(Attributes.MAX_HEALTH, 20);
        builder.add(Attributes.FOLLOW_RANGE, 160.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0);
        return builder;
    }

    @Unique
    private boolean htweaks$isOutnumbered() {
        int numOfEnemies = 0;
        int numOfAllies = 0;

        List<AbstractScavEntity> scavEntities = this.level().getEntitiesOfClass(AbstractScavEntity.class, this.getBoundingBox().inflate(64));
        List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(64));

        for(AbstractScavEntity scav : scavEntities) {
            if(this.isAlly(scav)) {
                numOfAllies++;
            }
            else {
                numOfEnemies++;
            }
        }

        for(Player player : players) {
            if(this.isAlly(player)) {
                numOfAllies++;
            }
            else {
                numOfEnemies++;
            }
        }

        return numOfEnemies > numOfAllies;
    }

}
