package com.logic.htweaks.commander.ai.htn.decisions;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.commander.ai.htn.actions.artillery.single.*;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class EngageEnemyTask extends AbstractCompoundTask {
    private final Random random = new Random();

    private final ObjectArrayList<AbstractAction> actions = new ObjectArrayList<>();

    @Override
    void planActions(ServerLevel level, MiltaryAiCommander commander) {
        Optional<List<BlockPos>> optionalEnemyLocations = commander.getMemory(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get());

        if(optionalEnemyLocations.isPresent()) {
            List<BlockPos> enemyLocations = optionalEnemyLocations.get();

            for(BlockPos pos : enemyLocations) {
                AbstractFireSupportAction action;

                int rand = random.nextInt(0, 3);

                switch (rand) {
                    case 0 -> action = new FireB1MortarAction();
                    case 1 -> action = new FireLargeHowitzerAction();
                    case 2 -> action = new FireLightHowitzerAction();
                    default -> action = new FireLightMortarAction();
                }

                action.setHitBlockPos(pos);

                actions.add(action);
            }


            List<ChunkPos> chunkPos = new ArrayList<>();

            for(BlockPos pos : enemyLocations) {
                chunkPos.add(new ChunkPos(pos));
            }

            CaptureAreaTask task = new CaptureAreaTask();

            task.setPrimitiveChunkPositions(chunkPos);

            actions.add(task);

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

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(ModMemoryTypes.KNOWN_ENEMY_LOCATIONS.get(), MemoryStatus.VALUE_PRESENT));
    }

    @Override
    public ObjectArrayList<AbstractAction> getActions(MiltaryAiCommander commander) {
        return actions;
    }
}
