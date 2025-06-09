package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.entity.IArtillerySummoner;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;
import java.util.Random;

public class TaczArtilleryStrike <E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    Random random = new Random();

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        double chance = random.nextDouble();

        if(!(entity instanceof IArtillerySummoner)) {
            return false;
        }

        if(chance < 0.05F) {
            return true;
        }

        return false;
    }

    protected void start(E entity) {
        if(entity.getTarget() != null) {
            ((IArtillerySummoner)entity).summonArtillery();
        }
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)});
    }
}
