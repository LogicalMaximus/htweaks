package com.logic.htweaks.commander.ai.htn.actions.squad;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;

import java.util.List;
import java.util.Optional;

public class MoveSquadAction extends AbstractAction {
    private boolean isFinished;

    private BlockPos movePos;

    private Patrol patrolInUse;

    private Waypoint patrolWaypoint;

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        return true;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {
        Optional<Patrol> patrolOptional = commander.getMemory(ModMemoryTypes.CURRENT_CONTROL_SQUAD.get());
        Optional<BlockPos> targetPosOptional = commander.getMemory(ModMemoryTypes.TARGET_SQUAD_MOVE_POS.get());

        if(movePos != null && patrolInUse != null) {
            patrolInUse.setPosAndClaimArea(new ChunkPos(movePos));

            if(patrolWaypoint != null) {
                patrolInUse.setWaypoint(patrolWaypoint);
            }
        } else if (patrolOptional.isPresent() && targetPosOptional.isPresent()) {
            Patrol patrol = commander.getMemory(ModMemoryTypes.CURRENT_CONTROL_SQUAD.get()).get();
            BlockPos targetPos = commander.getMemory(ModMemoryTypes.TARGET_SQUAD_MOVE_POS.get()).get();

            patrol.setPosAndClaimArea(new ChunkPos(targetPos));
        }

        this.isFinished = true;
    }

    @Override
    public void stop(MiltaryAiCommander commander, ServerLevel level) {
        isFinished = false;
        patrolInUse = null;
        movePos = null;
        patrolWaypoint = null;
    }

    @Override
    public void tick(MiltaryAiCommander commander, ServerLevel level) {

    }

    public void setPatrolWaypoint(Waypoint patrolWaypoint) {
        this.patrolWaypoint = patrolWaypoint;
    }

    public void setMovePos(BlockPos movePos) {
        this.movePos = movePos;
    }

    public void setPatrolInUse(Patrol patrolInUse) {
        this.patrolInUse = patrolInUse;
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public double requiredResources() {
        return 0;
    }

    @Override
    public boolean getIsFinished() {
        return isFinished;
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }
}
