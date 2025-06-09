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

public class MoveSquadsAction extends AbstractAction {
    private boolean isFinished;

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        return true;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {
        List<Patrol> patrols = commander.getMemory(ModMemoryTypes.CURRENT_CONTROL_SQUADS.get()).get();
        BlockPos targetPos = commander.getMemory(ModMemoryTypes.TARGET_SQUAD_MOVE_POS.get()).get();

        for(Patrol patrol : patrols) {
            patrol.setPos(new ChunkPos(targetPos));
        }

        isFinished = true;
    }

    @Override
    public void stop(MiltaryAiCommander commander, ServerLevel level) {
        isFinished = false;
    }

    @Override
    public void tick(MiltaryAiCommander commander, ServerLevel level) {

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
        return List.of(Pair.of(ModMemoryTypes.CURRENT_CONTROL_SQUADS.get(), MemoryStatus.VALUE_PRESENT), Pair.of(ModMemoryTypes.TARGET_SQUAD_MOVE_POS.get(), MemoryStatus.VALUE_PRESENT));
    }

}
