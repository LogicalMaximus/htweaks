package com.logic.htweaks.commander.ai.htn.decisions;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.config.HTServerConfig;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCompoundTask extends AbstractAction implements ICompoundTask {

    private final List<AbstractAction> actionsInQuery = new ArrayList<>();

    abstract void planActions(ServerLevel level, MiltaryAiCommander commander);

    public final boolean tryStart(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        if (!this.doStartCheck(level, commander, gameTime)) {
            return false;
        } else {
            this.endTimestamp = gameTime + this.getRunTime(commander);
            this.start(level, commander, gameTime);
            return true;
        }
    }

    public void start(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        if(HTServerConfig.DEBUG_MODE.get()) {
            Htweaks.LOGGER.info(commander.getFaction().getDisplayName() + " Commander Has Begun " + this.getClass().getName());
        }

        this.planActions(level, commander);

        manageNecessaryMemories(level, commander);

        actionsInQuery.addAll(this.getActions(commander));

        Iterator<AbstractAction> it = actionsInQuery.iterator();

        while(it.hasNext()) {
            AbstractAction action = it.next();

            if(isActionValid(action)) {
                action.tryStart(level, commander, gameTime);
            }
        }
    }

    public void tick(MiltaryAiCommander commander, ServerLevel level, long gameTime) {
        manageNecessaryMemories(level, commander);

        Iterator<AbstractAction> it = actionsInQuery.iterator();

        while(it.hasNext()) {
            AbstractAction action = it.next();

            if(isActionValid(action)) {
                if(action.shouldContinue(gameTime) && !action.timedOut(level.getGameTime())) {
                    action.tick(commander, level);
                }
                else {
                    action.stop(commander, level, gameTime);

                    it.remove();
                }
            }
        }

    }

    private boolean isActionValid(AbstractAction action) {
        return action != null;
    }

    public void stop(ServerLevel level, MiltaryAiCommander commander, long gameTime) {
        if(HTServerConfig.DEBUG_MODE.get()) {
            Htweaks.LOGGER.info(commander.getFaction().getDisplayName() + " Commander Has Stopped " + this.getClass().getName());
        }

        Iterator<AbstractAction> it = actionsInQuery.iterator();

        while(it.hasNext()) {
            AbstractAction action = it.next();

            if(isActionValid(action)) {
                action.stop(commander, level);

                it.remove();
            }
        }

        actionsInQuery.clear();
    }

    public boolean isFinished() {
        return actionsInQuery.isEmpty();
    }

    public abstract void manageNecessaryMemories(ServerLevel level, MiltaryAiCommander commander);

    public boolean checkShouldStop(long gameTime) {
        return this.timedOut(gameTime) || this.isFinished();
    }

    public double requiredResources() {
        return 0;
    }

    public long getCooldown() {
        return 60;
    }

    public boolean shouldContinue(long gameTime) {
        return this.checkShouldStop(gameTime);
    }

    public boolean getIsFinished() {
        return this.isFinished();
    }

    public final boolean hasRequiredMemories(MiltaryAiCommander commander) {
        for(Pair<MemoryModuleType<?>, MemoryStatus> memoryPair : this.getMemoryRequirements()) {
            if (!commander.checkMemory((MemoryModuleType)memoryPair.getFirst(), (MemoryStatus)memoryPair.getSecond())) {
                return false;
            }
        }

        return true;
    }
}
