package com.logic.htweaks.utils;

import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.faction.Faction;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.cadmus.api.claims.ClaimApi;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class CaptureUtils {
    public static void captureChunk(Faction faction, ServerLevel level, ChunkPos pos) {
        boolean anyNearbyEnemies = false;

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos.getMinBlockX(), -64, pos.getMinBlockZ(), pos.getMaxBlockX(), 64, pos.getMaxBlockZ()));

        for(LivingEntity entity : entities) {
            if(entity instanceof IScavFaction scavFaction) {
                if(faction.getEnemyFactions().contains(scavFaction)) {
                    anyNearbyEnemies = true;
                    break;
                }
            }
        }

        if(!anyNearbyEnemies) {
            Pair<String, ClaimType> claimData = ClaimHandler.getClaim(level, pos);
            if (claimData != null) {
                ClaimHandler.unclaim(level, claimData.getFirst(), pos);
                ClaimApi.API.claim(level, pos, "t:" + faction.getName(), false);
                //Htweaks.LOGGER.info("{} Successfully Captured Chunk", "t:" + faction.getName());
                return;
            } else {
                ClaimApi.API.claim(level, pos, "t:" + faction.getName(), false);
                //Htweaks.LOGGER.info("{} Successfully Captured Chunk", "t:" + faction.getName());
            }
        }
        else {
            //Htweaks.LOGGER.info("{} Failed To Capture Chunk Due To Nearby Enemies", "t:" + faction.getName());
        }
    }

    public static boolean isChunksOwned(Faction faction, ServerLevel level, List<ChunkPos> chunks) {
        String teamFactionID = "t:" + faction.getName();

        for(ChunkPos pos : chunks) {
            Pair<String, ClaimType> chunkPair = ClaimHandler.getClaim(level, pos);

            if(chunkPair != null) {
                if(!chunkPair.getFirst().equals(teamFactionID)) {
                    return false;
                }
            }
            else {
                return false;
            }
        }


        return true;
    }
}
