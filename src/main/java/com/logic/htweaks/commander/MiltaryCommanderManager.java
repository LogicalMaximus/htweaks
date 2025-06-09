package com.logic.htweaks.commander;

import com.logic.htweaks.commander.logistics.LogisticsManager;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiltaryCommanderManager extends SavedData {
    public List<MiltaryAiCommander> commanders = new ArrayList<>();

    private boolean intialized = false;

    private final ServerLevel level;

    private MiltaryCommanderChunkManager miltaryCommanderChunkManager;

    private int ticks;

    public MiltaryCommanderManager() {
        this.level = ServerLifecycleHooks.getCurrentServer().getLevel(ServerLevel.OVERWORLD);
        this.miltaryCommanderChunkManager = new MiltaryCommanderChunkManager(level);
    }

    public void tick() {
        checkForMissingCommanders();

        if(ticks % HTServerConfig.FACTION_SUPPLY_INTERVAL.get() == 0) {
            for(MiltaryAiCommander commander : commanders) {
                LogisticsManager logisticsManager = commander.getLogisticsManager();

                if(logisticsManager.getResources() <= HTServerConfig.MAX_RESOURCES.get()) {
                    logisticsManager.addResources(HTServerConfig.CHUNK_RESOURCE_MULTIPLIER.get() * commander.getOwnedObjectives().size());
                }
            }
        }

        miltaryCommanderChunkManager.update();

        for(MiltaryAiCommander commander : commanders) {
            commander.tick();
        }

        ticks++;
    }

    public static MiltaryCommanderManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(MiltaryCommanderManager::load, MiltaryCommanderManager::create, "miltarycommanders");
    }

    public static MiltaryCommanderManager create() {
        MiltaryCommanderManager manager = new MiltaryCommanderManager();

        return manager;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for(MiltaryAiCommander commander : commanders) {
            CompoundTag tag = new CompoundTag();

            commander.save(tag);

            listTag.add(tag);
        }

        compoundTag.put("commandersListTag", listTag);

        return compoundTag;
    }

    public static MiltaryCommanderManager load(CompoundTag tag) {
        MiltaryCommanderManager miltaryCommanderManager = MiltaryCommanderManager.create();

        ListTag listTag = tag.getList("commanderListTag", ListTag.TAG_COMPOUND);

        for(Tag commanderTag : listTag) {
            CompoundTag compoundTag = (CompoundTag) commanderTag;

            miltaryCommanderManager.commanders.add(new MiltaryAiCommander(miltaryCommanderManager.level, compoundTag));
        }

        miltaryCommanderManager.checkForMissingCommanders();

        return miltaryCommanderManager;
    }

    private void checkForMissingCommanders() {
        for(Faction faction : Factions.getFactions()) {
            if(!faction.isHasAICommander())continue;

            if(this.commanders.stream().anyMatch((e) -> e.getFaction() == faction))continue;

            MiltaryAiCommander commander = new MiltaryAiCommander(this.level, faction);

            this.commanders.add(commander);
        }

    }

    public List<ChunkPos> getFactionTerritory(Faction faction) {
        return miltaryCommanderChunkManager.getTerritory(faction);
    }

    public HashMap<Faction, List<ChunkPos>> getAllFactionTerritories() {
        return miltaryCommanderChunkManager.getFactionTerritories();
    }

    public MiltaryAiCommander getCommander(Faction faction) {
        for(MiltaryAiCommander commander : commanders) {
            if(commander.getFaction() == faction) {
                return commander;
            }
        }

        return null;
    }
}
