package com.logic.htweaks.mixin;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.BanditEntity;
import com.logic.htweaks.bridge.IAllyChecker;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.entity.sensor.ChunkSensor;
import com.logic.htweaks.entity.sensor.CoverSensor;
import com.logic.htweaks.entity.sensor.FlankSensor;
import com.logic.htweaks.entity.vehicle.IVehicleEntity;
import com.logic.htweaks.faction.Factions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.IncomingProjectilesSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BanditEntity.class)
public abstract class MixinBanditEntity extends AbstractScavEntity {
    protected MixinBanditEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    protected void init(CallbackInfo cir) {
        ((IScavFaction)this).htweaks$setFaction(Factions.PIRATE);
    }

    public void setTarget(LivingEntity pLivingEntity) {
        if(((IAllyChecker)this).isAlly(pLivingEntity)) {
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

    /**
     * @author LogicalMaximus
     * @reason Adds Faction Functionality
     */
    public List<? extends ExtendedSensor<? extends AbstractScavEntity>> getSensors() {
        // Keep track of nearby players
        // Keep track of nearby entities the Skeleton is interested in
        return ObjectArrayList.of(
                new NearbyPlayersSensor<BanditEntity>().setScanRate((e) -> 10),
                new CoverSensor<BanditEntity>().setRadius(16).setScanRate((e) -> 100),
                new ChunkSensor<BanditEntity>().setScanRate((e) -> 100),
                new FlankSensor<BanditEntity>().setScanRate((e) -> 100),
                new IncomingProjectilesSensor<BanditEntity>()
                        .setPredicate((target, entity) -> (!((IAllyChecker)entity).isAlly(target.getOwner())) // Keep track of nearby players
                        ).setScanRate((e) -> 10), new NearbyLivingEntitySensor<BanditEntity>().setRadius(256) // Keep track of nearby entities the Skeleton is interested in
                        .setPredicate((target, entity) ->
                                (target instanceof Player player && !player.isCreative()) ||
                                        target instanceof Monster ||
                                            (target instanceof AbstractScavEntity abstractScavEntity && !abstractScavEntity.deadAsContainer && ((IScavFaction)entity).htweaks$getFaction().getEnemyFactions().contains(((IScavFaction)abstractScavEntity).htweaks$getFaction())) ||
                                                (target instanceof IVehicleEntity<?> vehicle && vehicle.getEntity().getControllingPassenger() instanceof AbstractScavEntity || target instanceof IVehicleEntity<?> vehicle2 && vehicle2.getEntity().getControllingPassenger() instanceof Player))
                        .setScanRate((e) -> 10));
    }
}
