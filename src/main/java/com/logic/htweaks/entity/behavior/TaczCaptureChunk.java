package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.utils.CaptureUtils;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class TaczCaptureChunk<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        Faction faction = ((IScavFaction)entity).htweaks$getFaction();

        List<ChunkPos> chunkPositions = BrainUtils.getMemory(entity, ModMemoryTypes.UNCLAIMED_CHUNKS.get());

        for(ChunkPos pos : chunkPositions) {
            CaptureUtils.captureChunk(faction, (ServerLevel) entity.level(), pos);
        }
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT), Pair.of(ModMemoryTypes.UNCLAIMED_CHUNKS.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
