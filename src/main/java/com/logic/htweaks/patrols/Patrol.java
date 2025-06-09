package com.logic.htweaks.patrols;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.utils.*;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;

public class Patrol {

    private static final Random rand = new Random();

    @Deprecated
    private Waypoint waypoint;

    private final UUID uuid;

    private final Faction faction;

    private final ServerLevel level;

    private UUID playerID;

    private ChunkPos pos;

    private int numOfSquads;

    public Patrol(UUID uuid, Faction faction, ServerLevel level) {
        this.level = level;

        this.uuid = uuid;
        this.faction = faction;

        numOfSquads = 1;
    }

    public Patrol(CompoundTag compoundTag, ServerLevel level) {
        this.level = level;

        uuid = compoundTag.getUUID("uuid");
        faction = Factions.getFactionByString(compoundTag.getString("factionID"));

        if(compoundTag.contains("waypointTag")) {
            waypoint = new Waypoint(compoundTag.getCompound("waypointTag"));
        }

        if(compoundTag.contains("playerID")) {
            playerID = compoundTag.getUUID("playerID");
        }

        if(compoundTag.contains("posTag")) {
            CompoundTag posTag = (CompoundTag) compoundTag.get("posTag");

            pos = new ChunkPos(posTag.getInt("x"), posTag.getInt("z"));
        }
    }

    public Faction getFaction() {
        return faction;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    public void setPlayerID(UUID playerID) {
        this.playerID = playerID;
    }

    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putUUID("uuid", this.getUUID());
        compoundTag.putString("factionID", this.faction.getName());
        compoundTag.putInt("numOfSquads", numOfSquads);

        if(playerID != null) {
            compoundTag.putUUID("playerID", this.playerID);
        }

        if(waypoint != null) {
            CompoundTag waypointTag = new CompoundTag();

            waypoint.save(waypointTag);

            compoundTag.put("waypointTag", waypointTag);
        }

        if(pos != null) {
            CompoundTag posTag = new CompoundTag();

            posTag.putInt("x", pos.x);
            posTag.putInt("z", pos.z);

            compoundTag.put("posTag", posTag);
        }

        return compoundTag;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setWaypoint(Waypoint waypoint) {
        this.waypoint = waypoint;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public ChunkPos getPos() {
        return pos;
    }

    public void setPos(ChunkPos pos) {
        this.pos = pos;
    }

    public void setPosAndClaimArea(ChunkPos pos) {
        this.setPos(pos);

        claimSurroundingChunks();
    }

    public boolean attemptSpawnFireteam() {
        if(this.canSpawnSquad()) {
            Htweaks.LOGGER.info("Attempting To Spawn Fireteam");

            BlockPos pos = null;

            ChunkPos chunkPos = this.getPos();

            pos = HtweaksUtils.func_221244_a(chunkPos.getWorldPosition(), 16, level);

            if(pos != null) {
                this.spawnFireteam(pos);
                this.subFromSquadCount();

                return true;
            }
        }

        return false;
    }

    private void spawnFireteam(BlockPos pos) {
        Htweaks.LOGGER.info("Spawning Fireteam at: " + pos.toString());

        double chance = rand.nextDouble();

        if(chance < HTServerConfig.TANK_SPAWN_CHANCE.get()) {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                AbstractScavEntity tankCommander = HumanSquadSpawnerUtils.spawnTank(this, faction.getID(), pos, level, null);
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, true);
            }
            else if(this.faction == Factions.COVENANT) {
                AbstractScavEntity tankCommander = CovenantSquadSpawnerUtils.spawnTank(this, pos, level, null);
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
            else if(this.faction == Factions.FLOOD) {
                AbstractScavEntity tankCommander = FloodSquadSpawnerUtils.spawnTank(this, pos, level, null);
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
        }
        else if(chance < HTServerConfig.APC_SPAWN_CHANCE.get()) {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                AbstractScavEntity tankCommander = HumanSquadSpawnerUtils.spawnAPC(this, faction.getID(), pos, level, null);

                Entity vehicle = tankCommander.getVehicle();

                if(vehicle != null) {
                    HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, vehicle, false);
                }
                else {
                    HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, false);
                }

            }
            else if(this.faction == Factions.COVENANT) {
                AbstractScavEntity tankCommander = CovenantSquadSpawnerUtils.spawnAPC(this, pos, level, null);

                Entity vehicle = tankCommander.getVehicle();

                if(vehicle != null) {
                    CovenantSquadSpawnerUtils.spawnFireteam(this,  pos, level, tankCommander, vehicle);
                }
                else {
                    CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                }
            }
            else if(this.faction == Factions.FLOOD) {
                AbstractScavEntity tankCommander = FloodSquadSpawnerUtils.spawnAPC(this, pos, level, null);

                Entity vehicle = tankCommander.getVehicle();

                if(vehicle != null) {
                    FloodSquadSpawnerUtils.spawnFireteam(this,  pos, level, tankCommander, vehicle);
                }
                else {
                    FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                }
            }
        }
        else {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, null,null, false);
            }
            else if(this.faction == Factions.COVENANT) {
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, null, null);
            }
            else if(this.faction == Factions.FLOOD) {
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, null, null);
            }
        }
    }

    public boolean attemptSpawnSquad() {
        int numOfScavEntities = level.getEntities(EntityTypeTest.forClass(AbstractScavEntity.class), (e) -> !((AbstractScavEntity)e).isDeadOrDying()).size();

        if(numOfScavEntities <= HTServerConfig.MAX_SCAV_CAP.get()) {
            if(this.canSpawnSquad()) {
                Htweaks.LOGGER.info("Attempting To Spawn Squad");

                BlockPos pos = null;

                ChunkPos chunkPos = this.getPos();

                pos = HtweaksUtils.func_221244_a(chunkPos.getWorldPosition(), 128, level);

                if(pos != null) {
                    this.spawnSquad(pos);
                    this.subFromSquadCount();

                    return true;
                }
            }
        }

        return false;
    }

    public boolean canClaimTerritory() {
        return numOfSquads > 0;
    }

    public boolean canSpawnSquad() {
        return numOfSquads > 0 && this.pos != null;
    }

    private void spawnSquad(BlockPos pos) {
        Htweaks.LOGGER.info("Spawning Squad at: " + pos.toString());

        double chance = rand.nextDouble();

        if(chance < HTServerConfig.TANK_SPAWN_CHANCE.get()) {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                AbstractScavEntity tankCommander = HumanSquadSpawnerUtils.spawnTank(this, faction.getID(), pos, level, null);
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, false);
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, true);
            }
            else if(this.faction == Factions.COVENANT) {
                AbstractScavEntity tankCommander = CovenantSquadSpawnerUtils.spawnTank(this, pos, level, null);
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
            else if(this.faction == Factions.FLOOD) {
                AbstractScavEntity tankCommander = FloodSquadSpawnerUtils.spawnTank(this, pos, level, null);
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
        }
        else if(chance < HTServerConfig.APC_SPAWN_CHANCE.get()) {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                AbstractScavEntity tankCommander = HumanSquadSpawnerUtils.spawnAPC(this, faction.getID(), pos, level, null);
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, false);
                HumanSquadSpawnerUtils.spawnFireteam(this, faction.getID(), pos, level, tankCommander, null, true);
            }
            else if(this.faction == Factions.COVENANT) {
                AbstractScavEntity tankCommander = CovenantSquadSpawnerUtils.spawnAPC(this, pos, level, null);
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                CovenantSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
            else if(this.faction == Factions.FLOOD) {
                AbstractScavEntity tankCommander = FloodSquadSpawnerUtils.spawnAPC(this, pos, level, null);
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
                FloodSquadSpawnerUtils.spawnFireteam(this, pos, level, tankCommander, null);
            }
        }
        else {
            if(this.faction == Factions.UNSC || this.faction == Factions.INSURRECTIONIST) {
                HumanSquadSpawnerUtils.spawnSquad(this, faction.getID(), pos, level, null);
            }
            else if(this.faction == Factions.COVENANT) {
                CovenantSquadSpawnerUtils.spawnSquad(this, pos, level, null);
            }
            else if(this.faction == Factions.FLOOD) {
                FloodSquadSpawnerUtils.spawnSquad(this, pos, level, null);
            }
        }
    }

    public void addSquad() {
        this.numOfSquads++;
    }

    public void subFromSquadCount() {
        this.numOfSquads--;
    }

    public void tick() {
        if(pos != null && this.level.isAreaLoaded(pos.getWorldPosition(), 16)) {
            this.attemptSpawnSquad();
        }

    }

    public void claimSurroundingChunks() {
        if(this.canClaimTerritory()) {
            List<ChunkPos> chunkPositions = new ArrayList<>(ChunkPos.rangeClosed(pos, 3).filter((e) -> {
                Pair<String, ClaimType> claim = ClaimHandler.getClaim(level, pos);

                if (claim == null) {
                    return true;
                }

                if (!claim.getFirst().equals("t:" + this.faction.getName())) {
                    return true;
                }

                return false;
            }).toList());

            for(ChunkPos cPos : chunkPositions) {
                CaptureUtils.captureChunk(this.faction, level, cPos);
            }
        }
    }
}
