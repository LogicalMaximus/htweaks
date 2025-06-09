package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.INPCSPatrol;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class TaczSetWalkToWaypoint<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private Waypoint waypointTarget;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;
    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    protected float speedModifier = 1.0F;
    protected BiFunction<E, Waypoint, Float> speedMod = (owner, target) -> {
        return 1.0F;
    };
    protected BiFunction<E, Waypoint, Integer> closeEnoughWhen = (owner, target) -> {
        return waypointTarget.getRadius();
    };

    public TaczSetWalkToWaypoint() {
    }

    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    public TaczSetWalkToWaypoint<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> {
            return speedModifier;
        });
    }

    public TaczSetWalkToWaypoint<E> speedMod(BiFunction<E, Waypoint, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    public TaczSetWalkToWaypoint<E> closeEnoughDist(BiFunction<E, Waypoint, Integer> closeEnoughMod) {
        this.closeEnoughWhen = closeEnoughMod;
        return this;
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        Patrol patrol = ((INPCSPatrol)entity).htweaks$getPatrol();

        if(patrol != null) {
            waypointTarget = patrol.getWaypoint();

            if(waypointTarget != null) {
                BlockPos waypointCenter = waypointTarget.getPos();

                if(entity.distanceToSqr(waypointCenter.getCenter()) > this.closeEnoughWhen.apply(entity, waypointTarget)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();

        BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(waypointTarget.getPos()), (Float)this.speedMod.apply(entity, waypointTarget), 1));
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)});
    }
}
