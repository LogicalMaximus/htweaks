package com.logic.htweaks.commander.ai.htn.decisions;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.commander.ai.htn.actions.squad.MoveSquadAction;
import com.logic.htweaks.commander.ai.htn.actions.squad.WaypointSquadAction;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.utils.HtweaksUtils;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;

import java.util.*;
import java.util.stream.Collectors;

public class CaptureAreaTask extends AbstractCompoundTask {
    private final ObjectArrayList<AbstractAction> actions = new ObjectArrayList<>();

    private final List<ChunkPos> primitiveChunkPositions = new ArrayList<>();

    @Override
    void planActions(ServerLevel level, MiltaryAiCommander commander) {
        Optional<List<ChunkPos>> optionalChunkPosList = commander.getMemory(ModMemoryTypes.TARGET_CHUNKS.get());

        if(!primitiveChunkPositions.isEmpty()) {
            createActions(primitiveChunkPositions, level, commander);
        }
        else if(optionalChunkPosList.isPresent()) {
            createActions(optionalChunkPosList.get(), level, commander);
        }
    }

    @Override
    public void manageNecessaryMemories(ServerLevel level, MiltaryAiCommander commander) {

    }

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        return true;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {

    }

    @Override
    public void stop(MiltaryAiCommander commander, ServerLevel level) {
        actions.clear();
    }

    @Override
    public void tick(MiltaryAiCommander commander, ServerLevel level) {

    }

    public void setPrimitiveChunkPositions(List<ChunkPos> primitiveChunkPositions) {
        this.primitiveChunkPositions.clear();

        this.primitiveChunkPositions.addAll(primitiveChunkPositions);
    }

    private void createActions(List<ChunkPos> chunkPosList, ServerLevel level, MiltaryAiCommander commander) {
        List<Patrol> factionPatrols = Htweaks.getPatrolManager().getPatrolsByFaction(commander.getFaction());

        List<Patrol> patrolsInUse = new ArrayList<>(factionPatrols.stream().filter(Patrol::canClaimTerritory).toList());

        if(!patrolsInUse.isEmpty()) {
            for(int i = 0; i < patrolsInUse.size() && i < chunkPosList.size(); i++) {
                MoveSquadAction action = new MoveSquadAction();

                action.setPatrolInUse(patrolsInUse.get(i));

                List<ChunkPos> ownedObjectives = new ArrayList<>(commander.getOwnedObjectives());

                int finalI = i;

                ownedObjectives.sort(Comparator.comparingInt((e) -> e.getChessboardDistance(chunkPosList.get(finalI))));

                Optional<ChunkPos> optionalChunkPos = ownedObjectives.stream().findFirst();

                if(optionalChunkPos.isPresent()) {
                    ChunkPos closestChunkPos = optionalChunkPos.get();

                    BlockPos targetMovePos = chunkPosList.get(i).getWorldPosition();

                    action.setMovePos(closestChunkPos.getWorldPosition());

                    if(targetMovePos != null) {
                        action.setPatrolWaypoint(new Waypoint(targetMovePos, 8));
                    } else {
                        action.setPatrolWaypoint(new Waypoint(chunkPosList.get(i).getWorldPosition(), 16));
                    }

                    action.tryStart(level, commander, level.getGameTime());
                }
                else {
                    action = null;
                }
            }
        } else {
            commander.requestSquadCreation(chunkPosList.size());
        }
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    public ObjectArrayList<AbstractAction> getActions(MiltaryAiCommander commander) {
        return actions;
    }
}
