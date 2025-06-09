package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class TaczMountTarget<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private Entity mountTarget;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        mountTarget = BrainUtils.getMemory(entity, ModMemoryTypes.MOUNT_TARGET.get());

        if(entity.distanceTo(mountTarget) < 7) {
            return true;
        }

        return false;
    }

    @Override
    protected void start(E entity) {
        entity.setPos(mountTarget.getPosition(1.0F));
        entity.startRiding(mountTarget, true);

        BrainUtils.clearMemory(entity, ModMemoryTypes.MOUNT_TARGET.get());

        super.start(entity);
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(ModMemoryTypes.MOUNT_TARGET.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
