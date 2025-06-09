package com.logic.htweaks.entity.flood;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IAllyChecker;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.entity.BaseTaczFactionEntity;
import com.logic.htweaks.entity.sensor.ChunkSensor;
import com.logic.htweaks.entity.sensor.CoverSensor;
import com.logic.htweaks.entity.sensor.FlankSensor;
import com.logic.htweaks.entity.vehicle.IVehicleEntity;
import com.logic.htweaks.faction.Factions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.IncomingProjectilesSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;

import java.util.List;

public abstract class AbstractFloodTrooper extends BaseTaczFactionEntity {
    protected AbstractFloodTrooper(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);

        ((IScavFaction)this).htweaks$setFaction(Factions.FLOOD);
    }

    @Override
    public List<? extends ExtendedSensor<? extends AbstractScavEntity>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<AbstractFloodTrooper>().setScanRate((e) -> 10),
                new CoverSensor<AbstractFloodTrooper>().setRadius(16).setScanRate((e) -> 100),
                new ChunkSensor<AbstractFloodTrooper>().setScanRate((e) -> 100),
                new FlankSensor<AbstractFloodTrooper>().setScanRate((e) -> 100),
                new IncomingProjectilesSensor<AbstractFloodTrooper>()
                        .setPredicate((target, entity) -> (!((IAllyChecker)entity).isAlly(target.getOwner())) // Keep track of nearby players
                        ).setScanRate((e) -> 10), // Keep track of nearby players
                new NearbyLivingEntitySensor<AbstractFloodTrooper>().setRadius(256) // Keep track of nearby entities the Skeleton is interested in
                        .setPredicate((target, entity) ->
                                (target instanceof Player player && !player.isCreative()) ||
                                        target instanceof LivingEntity ||
                                        (target instanceof AbstractScavEntity abstractScavEntity && !abstractScavEntity.deadAsContainer && ((IScavFaction)entity).htweaks$getFaction().getEnemyFactions().contains(((IScavFaction)abstractScavEntity).htweaks$getFaction())))
                        .setScanRate((e) -> 10));

    }

    @Override
    public boolean isAlly(Entity entity) {
        if(entity instanceof BaseTaczFactionEntity baseTaczFactionEntity) {
            if(((IScavFaction)baseTaczFactionEntity).htweaks$getFaction() == ((IScavFaction)this).htweaks$getFaction()) {
                return true;
            }
        }

        if(entity instanceof Monster) {
            return true;
        }

        return false;
    }
}
