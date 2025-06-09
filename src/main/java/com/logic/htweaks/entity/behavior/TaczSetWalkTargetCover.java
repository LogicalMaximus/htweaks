package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;

public class TaczSetWalkTargetCover<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {

    private LivingEntity target;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;
    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    protected float speedModifier = 1.45F;
    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> {
        return 1.45F;
    };
    protected BiFunction<E, LivingEntity, Integer> closeEnoughWhen = (owner, target) -> {
        return 0;
    };

    public TaczSetWalkTargetCover() {
    }

    /** @deprecated */
    @Deprecated(
            forRemoval = true
    )
    public TaczSetWalkTargetCover<E> speedMod(float speedModifier) {
        return this.speedMod((owner, target) -> {
            return speedModifier;
        });
    }

    public TaczSetWalkTargetCover<E> speedMod(BiFunction<E, LivingEntity, Float> speedModifier) {
        this.speedMod = speedModifier;
        return this;
    }

    public TaczSetWalkTargetCover<E> closeEnoughDist(BiFunction<E, LivingEntity, Integer> closeEnoughMod) {
        this.closeEnoughWhen = closeEnoughMod;
        return this;
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        target = BrainUtils.getTargetOfEntity(entity);

        if(target != null) {
            if(target.hasLineOfSight(entity)) {
                return true;
            }
        }

        if(BrainUtils.hasMemory(entity, SBLMemoryTypes.INCOMING_PROJECTILES.get())) {
            return true;
        }

        return false;
    }

    protected void start(E entity) {
        Brain<?> brain = entity.getBrain();

        BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new BlockPosTracker(BrainUtils.getMemory(entity, ModMemoryTypes.NEARBY_COVER_POSITIONS.get()).get(0)), (Float)this.speedMod.apply(entity, target), 1));
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(ModMemoryTypes.NEARBY_COVER_POSITIONS.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
