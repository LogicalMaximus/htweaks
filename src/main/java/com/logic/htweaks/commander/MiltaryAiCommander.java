package com.logic.htweaks.commander;

import com.google.common.collect.Maps;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.ai.htn.decisions.AbstractCompoundTask;
import com.logic.htweaks.commander.ai.htn.decisions.CaptureAreaTask;
import com.logic.htweaks.commander.ai.htn.decisions.EngageEnemyTask;
import com.logic.htweaks.commander.logistics.LogisticsManager;
import com.logic.htweaks.commander.type.TaskTypes;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.ExpirableValue;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class MiltaryAiCommander {
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            ModMemoryTypes.ARTILLERY_STRIKE_POS.get(),
            ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(),
            ModMemoryTypes.SPAWN_SQUAD_POS.get(),
            ModMemoryTypes.MISSILE_STRIKE_POS.get(),
            ModMemoryTypes.CURRENT_CONTROL_SQUAD.get(),
            ModMemoryTypes.CURRENT_CONTROL_SQUADS.get(),
            ModMemoryTypes.TARGET_SQUAD_MOVE_POS.get(),
            ModMemoryTypes.TARGET_CHUNK_POSITION.get(),
            ModMemoryTypes.ARTILLERY_STRIKE_POSITIONS.get(),
            ModMemoryTypes.TARGET_FLANK_POS.get(),
            ModMemoryTypes.TARGET_CHUNKS.get(),
            ModMemoryTypes.TARGET_SQUAD_WAYPOINT_POS.get());

    private final ServerLevel level;

    private final LogisticsManager logisticsManager;

    private final List<@NotNull ChunkPos> ownedObjectives = new ArrayList<>();

    private final Map<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> memories = Maps.newHashMap();

    private AbstractCompoundTask activeDecision;

    private final Faction faction;

    private int tick = 0;

    private WorldState worldState;

    private int numOfSquadCreationRequest;

    public MiltaryAiCommander(ServerLevel level, Faction faction) {
        this.level = level;

        this.faction = faction;
        this.logisticsManager = new LogisticsManager();

        this.worldState = new WorldState(this);

        this.init();
    }

    public MiltaryAiCommander(ServerLevel level, CompoundTag tag) {
        this.level = level;

        this.faction = Factions.getFactionByString(tag.getString("factionID"));

        this.logisticsManager = new LogisticsManager(tag.getCompound("logisticsManagerTag"));

        this.worldState = new WorldState(this);

        this.init();
    }

    public ServerLevel level() {
        return level;
    }

    public void init() {
        for(MemoryModuleType<?> memorymoduletype : MEMORY_TYPES) {
            this.memories.put(memorymoduletype, Optional.empty());
        }

    }

    public void tick() {
        forgetOutdatedMemories();

        if(tick % 100 == 0) {
            update();
        }

        tickActiveDecisions();
        stopActiveDecision(false);

        tick++;
    }

    private boolean isActiveDecisionValid() {
        return activeDecision != null && !activeDecision.isFinished();
    }

    private void tickActiveDecisions() {
        if(this.isActiveDecisionValid()) {
            activeDecision.tick(this, this.level, this.level.getGameTime());
        }
    }

    private void stopActiveDecision(boolean forceStop) {
        if(this.isActiveDecisionValid()) {
            if(activeDecision.checkShouldStop(this.level.getGameTime()) || forceStop) {
                activeDecision.stop(level, this, this.level.getGameTime());
            }
        }
    }

    public void update() {
        updateOwnedTerritory();

        createNewSquads();

        provideLogisticsToSquads();

        if(!isActiveDecisionValid()) {
            worldState.chooseNextDecision();
        }
    }

    private void createPatrol() {
        Patrol patrol = Htweaks.getPatrolManager().createPatrol(this.faction);
    }

    private boolean canCreateSquad() {
        return Htweaks.getPatrolManager().getPatrolsByFaction(this.faction).size() <= HTServerConfig.MAX_NUM_OF_SQUADS.get() && this.logisticsManager.getResources() > HTServerConfig.SQUAD_COST.get();
    }

    private void createNewSquads() {
        if(!this.getOwnedObjectives().isEmpty()) {
            for(int i = 0; i < numOfSquadCreationRequest; i++) {
                if(this.canCreateSquad()) {
                    this.createPatrol();
                }
                else {
                    break;
                }
            }

            numOfSquadCreationRequest = 0;
        }
    }

    public List<ChunkPos> getOwnedObjectives() {
        return ownedObjectives;
    }

    public LogisticsManager getLogisticsManager() {
        return logisticsManager;
    }

    private void provideLogisticsToSquads() {
        List<Patrol> patrolsInNeed = Htweaks.getPatrolManager().getPatrolsByFaction(this.faction);

        if(!patrolsInNeed.isEmpty()) {
            patrolsInNeed.removeIf(Patrol::canClaimTerritory);

            for(Patrol patrol : patrolsInNeed) {
                if(this.getResources() > HTServerConfig.SQUAD_COST.get()) {
                    patrol.addSquad();
                    this.getLogisticsManager().removeResources(HTServerConfig.SQUAD_COST.get());
                }
                else {
                    break;
                }
            }
        }
    }

    //checks Relative Troop Strength and available assets
    public void updateOwnedTerritory() {
        ownedObjectives.clear();

        List<ChunkPos> ownedChunks = Htweaks.getMiltaryCommanderManager().getFactionTerritory(this.faction);

        if(ownedChunks != null && !ownedChunks.isEmpty()) {
            ownedObjectives.addAll(ownedChunks);
        }
    }

    private void forgetOutdatedMemories() {
        for(Map.Entry<MemoryModuleType<?>, Optional<? extends ExpirableValue<?>>> entry : this.memories.entrySet()) {
            if (((Optional)entry.getValue()).isPresent()) {
                ExpirableValue<?> expirablevalue = (ExpirableValue)((Optional)entry.getValue()).get();
                if (expirablevalue.hasExpired()) {
                    this.eraseMemory((MemoryModuleType)entry.getKey());
                }

                expirablevalue.tick();
            }
        }

    }

    public boolean hasMemoryValue(MemoryModuleType<?> p_21875_) {
        return this.checkMemory(p_21875_, MemoryStatus.VALUE_PRESENT);
    }

    public void clearMemories() {
        this.memories.keySet().forEach((p_276103_) -> this.memories.put(p_276103_, Optional.empty()));
    }

    public <U> void eraseMemory(MemoryModuleType<U> p_21937_) {
        this.setMemory(p_21937_, Optional.empty());
    }

    public <U> void setMemory(MemoryModuleType<U> p_21880_, @Nullable U p_21881_) {
        this.setMemory(p_21880_, Optional.ofNullable(p_21881_));
    }

    public <U> void setMemory(MemoryModuleType<U> p_21887_, Optional<? extends U> p_21888_) {
        this.setMemoryInternal(p_21887_, p_21888_.map(ExpirableValue::of));
    }

    public <U> void setMemoryWithExpiry(MemoryModuleType<U> p_21883_, U p_21884_, long p_21885_) {
        this.setMemoryInternal(p_21883_, Optional.of(ExpirableValue.of(p_21884_, p_21885_)));
    }

    <U> void setMemoryInternal(MemoryModuleType<U> p_21942_, Optional<? extends ExpirableValue<?>> p_21943_) {
        if (this.memories.containsKey(p_21942_)) {
            if (p_21943_.isPresent() && this.isEmptyCollection(((ExpirableValue)p_21943_.get()).getValue())) {
                this.eraseMemory(p_21942_);
            } else {
                this.memories.put(p_21942_, p_21943_);
            }
        }

    }

    public <U> Optional<U> getMemory(MemoryModuleType<U> p_21953_) {
        Optional<? extends ExpirableValue<?>> optional = (Optional)this.memories.get(p_21953_);
        if (optional == null) {
            throw new IllegalStateException("Unregistered memory fetched: " + String.valueOf(p_21953_));
        } else {
            return (Optional<U>) optional.map(ExpirableValue::getValue);
        }
    }

    @Nullable
    public <U> Optional<U> getMemoryInternal(MemoryModuleType<U> p_259344_) {
        Optional<? extends ExpirableValue<?>> optional = (Optional)this.memories.get(p_259344_);
        return optional == null ? null : (Optional<U>) optional.map(ExpirableValue::getValue);
    }

    public boolean checkMemory(MemoryModuleType<?> p_21877_, MemoryStatus p_21878_) {
        Optional<? extends ExpirableValue<?>> optional = (Optional)this.memories.get(p_21877_);
        if (optional == null) {
            return false;
        } else {
            return p_21878_ == MemoryStatus.REGISTERED || p_21878_ == MemoryStatus.VALUE_PRESENT && optional.isPresent() || p_21878_ == MemoryStatus.VALUE_ABSENT && !optional.isPresent();
        }
    }

    private boolean isEmptyCollection(Object p_21919_) {
        return p_21919_ instanceof Collection && ((Collection)p_21919_).isEmpty();
    }

    public void save(CompoundTag tag) {
        tag.putString("factionID", this.faction.getName());

        CompoundTag logisticsManagerTag = new CompoundTag();

        this.logisticsManager.save(logisticsManagerTag);

        tag.put("logisticsManagerTag", logisticsManagerTag);

    }

    public Faction getFaction() {
        return faction;
    }

    public double getResources() {
        return this.logisticsManager.getResources();
    }

    public void requestSquadCreation(int addition) {
        numOfSquadCreationRequest += addition;
    }

    public void setActiveDecision(TaskTypes taskTypes) {
        if(!isActiveDecisionValid()) {
            if(taskTypes == TaskTypes.ENGAGE_ENEMY) {
                if(this.activeDecision != null) {
                    this.activeDecision.stop(this.level, this, this.level.getGameTime());
                }

                this.activeDecision = new EngageEnemyTask();
            }
            else if(taskTypes == TaskTypes.CAPTURE_AREA) {
                if(this.activeDecision != null) {
                    this.activeDecision.stop(this.level, this, this.level.getGameTime());
                }

                this.activeDecision = new CaptureAreaTask();
            }

            this.activeDecision.tryStart(this.level, this, this.level.getGameTime());
        }
    }
}
