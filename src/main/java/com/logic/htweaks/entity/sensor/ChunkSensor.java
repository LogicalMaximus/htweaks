package com.logic.htweaks.entity.sensor;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.registries.ModSensors;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.ChunkPos;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChunkSensor<E extends AbstractScavEntity> extends PredicateSensor<ChunkPos, E> {
    private static final List<MemoryModuleType<?>> MEMORIES;
    protected int radius = 3;

    public ChunkSensor() {
        super((pos, entity) -> {
            return true;
        });
    }

    public ChunkSensor<E> setRadius(int x) {
        this.radius = x;
        return this;
    }

    protected void doTick(ServerLevel level, E entity) {
        ChunkPos origin = entity.chunkPosition();

        List<ChunkPos> chunkPositions = new ArrayList<>(ChunkPos.rangeClosed(origin, this.radius).filter((e) -> {
            Pair<String, ClaimType> claim = ClaimHandler.getClaim(level, origin);

            if (claim == null) {
                return true;
            }

            if (!claim.getFirst().equals("t:" + ((IScavFaction) entity).htweaks$getFaction().getName())) {
                return true;
            }

            return false;
        }).toList());

        chunkPositions.sort(Comparator.comparing((e) -> e.getChessboardDistance(origin)));

        BrainUtils.setMemory(entity, ModMemoryTypes.UNCLAIMED_CHUNKS.get(), chunkPositions);
    }

    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    public SensorType<? extends ExtendedSensor<?>> type() {
        return (SensorType) ModSensors.UNCLAIMED_CHUNKS.get();
    }

    static {
        MEMORIES = ObjectArrayList.of(new MemoryModuleType[]{ModMemoryTypes.UNCLAIMED_CHUNKS.get()});
    }
}
