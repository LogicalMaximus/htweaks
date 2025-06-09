package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
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

public class TaczSetWalkTargetToMovePos<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;
    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    protected float speedModifier = 1.0F;
    protected BiFunction<E, BlockPos, Float> speedMod = (owner, target) -> {
        return 1.0F;
    };
    protected BiFunction<E, BlockPos, Integer> closeEnoughWhen = (owner, target) -> {
        return 5;
    };

    public TaczSetWalkTargetToMovePos() {
    }

    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    public TaczSetWalkTargetToMovePos<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> {
            return speedModifier;
        });
    }

    public TaczSetWalkTargetToMovePos<E> speedMod(BiFunction<E, BlockPos, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    public TaczSetWalkTargetToMovePos<E> closeEnoughDist(BiFunction<E, BlockPos, Integer> closeEnoughMod) {
        this.closeEnoughWhen = closeEnoughMod;
        return this;
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();

        BlockPos targetPos = BrainUtils.getMemory(brain, ModMemoryTypes.MOVE_POS.get());

        BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(targetPos), (Float)this.speedMod.apply(entity, targetPos), this.closeEnoughWhen.apply(entity, targetPos)));
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(ModMemoryTypes.MOVE_POS.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
