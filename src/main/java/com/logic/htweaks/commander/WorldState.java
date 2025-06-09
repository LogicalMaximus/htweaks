package com.logic.htweaks.commander;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.type.TaskTypes;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class WorldState {
    private long timestamp;

    private final MiltaryAiCommander commander;

    public WorldState(MiltaryAiCommander commander) {
        this.commander = commander;
    }

    public void update() {

    }

    public void chooseNextDecision() {
        Optional<List<BlockPos>> optionalKnownEnemyLocations = commander.getMemory(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get());

        if(optionalKnownEnemyLocations.isPresent()) {
            commander.setActiveDecision(TaskTypes.ENGAGE_ENEMY);
        }
        else {
            HashMap<Faction, List<ChunkPos>> factionTerritory = Htweaks.getMiltaryCommanderManager().getAllFactionTerritories();

            Optional<List<ChunkPos>> optionalChunkPos = factionTerritory.values().stream().filter((e) -> e != factionTerritory.get(commander.getFaction())).findAny();

            if(optionalChunkPos.isPresent()) {
                List<ChunkPos> targetChunks = optionalChunkPos.get();

                commander.setMemory(ModMemoryTypes.TARGET_CHUNKS.get(), targetChunks);

                commander.setActiveDecision(TaskTypes.CAPTURE_AREA);
            }
        }
    }
}
