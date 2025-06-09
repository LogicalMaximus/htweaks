package com.logic.htweaks.commander;

import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.*;
import java.util.stream.Collectors;

public class MiltaryCommanderChunkManager {

    private final ServerLevel level;

    private HashMap<Faction, List<ChunkPos>> factionTerritories = new HashMap<>();

    public MiltaryCommanderChunkManager(ServerLevel level) {
        this.level = level;
    }

    public void update() {
        for(Faction faction : Factions.getFactions()) {
            Map<ChunkPos, ClaimType> chunkPosSet = ClaimHandler.getTeamClaims(level, "t:" + faction.getName());

            if(chunkPosSet != null) {
                List<ChunkPos> chunkPositions = new ArrayList<>(chunkPosSet.keySet());

                factionTerritories.put(faction, chunkPositions);
            }
        }
    }

    public List<ChunkPos> getTerritory(Faction faction) {
        return factionTerritories.get(faction);
    }

    public HashMap<Faction, List<ChunkPos>> getFactionTerritories() {
        return factionTerritories;
    }
}
