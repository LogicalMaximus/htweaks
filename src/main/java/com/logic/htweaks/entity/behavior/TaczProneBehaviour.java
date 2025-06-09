package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;

import java.util.List;

public class TaczProneBehaviour <E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        entity.crawl(true);
    }

    protected void stop(E entity) {
        entity.crawl(false);
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of();
    }
}
