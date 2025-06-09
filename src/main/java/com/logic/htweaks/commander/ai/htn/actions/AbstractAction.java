package com.logic.htweaks.commander.ai.htn.actions;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.config.HTServerConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.List;

public abstract class AbstractAction {
    protected long endTimestamp;
    private long cooldownFinishedAt;

    public abstract boolean canExecute(MiltaryAiCommander commander);

    public abstract void start(MiltaryAiCommander commander, ServerLevel level);

    public abstract void stop(MiltaryAiCommander commander, ServerLevel level);

    public abstract void tick(MiltaryAiCommander commander, ServerLevel level);

    public abstract long getCooldown();

    public abstract double requiredResources();

    public boolean tryStart(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        if (!this.doStartCheck(level, commander, gameTime)) {
            return false;
        } else {
            this.endTimestamp = gameTime + this.getRunTime(commander);
            this.start(level, commander, gameTime);
            return true;
        }
    }

    public void stop(MiltaryAiCommander commander, ServerLevel level, long gameTime) {
        this.cooldownFinishedAt = gameTime + this.getCooldown();

        if(HTServerConfig.DEBUG_MODE.get()) {
            Htweaks.LOGGER.info(commander.getFaction().getDisplayName() + " Has Stopped Action In " + this.getClass().getName());
        }

        this.stop(commander, level);
    }

    protected int getRunTime(MiltaryAiCommander commander) {
        return 600;
    }


    protected boolean doStartCheck(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        return this.cooldownFinishedAt <= gameTime && this.hasRequiredMemories(commander) && this.canExecute(commander) && commander.getResources() > this.requiredResources();
    }

    public boolean shouldContinue(long gameTime) {
        return !this.getIsFinished();
    }

    public abstract boolean getIsFinished();

    public void start(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        if(HTServerConfig.DEBUG_MODE.get()) {
            Htweaks.LOGGER.info(commander.getFaction().getDisplayName() + " Has Begun Action In " + this.getClass().getName());
        }

        commander.getLogisticsManager().removeResources(this.requiredResources());

        this.start(commander, level);
    }

    public boolean timedOut(long p_22537_) {
        return p_22537_ > this.endTimestamp;
    }

    public boolean hasRequiredMemories(MiltaryAiCommander commander) {
        for(Pair<MemoryModuleType<?>, MemoryStatus> memoryPair : this.getMemoryRequirements()) {
            if (!commander.checkMemory((MemoryModuleType)memoryPair.getFirst(), (MemoryStatus)memoryPair.getSecond())) {
                return false;
            }
        }

        return true;
    }

    public abstract List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements();
}
