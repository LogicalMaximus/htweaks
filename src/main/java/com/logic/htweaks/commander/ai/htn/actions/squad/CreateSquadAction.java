package com.logic.htweaks.commander.ai.htn.actions.squad;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.utils.CovenantSquadSpawnerUtils;
import com.logic.htweaks.utils.HtweaksUtils;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public class CreateSquadAction extends AbstractAction {
    private boolean isFinished = false;

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        int maxNumOfSquads = HTServerConfig.MAX_NUM_OF_SQUADS.get();
        int numOfSquads = Htweaks.getPatrolManager().getPatrolsByFaction(commander.getFaction()).size();

        if(numOfSquads < maxNumOfSquads) {
            BlockPos pos = commander.getMemory(ModMemoryTypes.SPAWN_SQUAD_POS.get()).orElse(null);

            if(pos != null) {
                ChunkPos chunkPos = new ChunkPos(pos);

                Pair<String, ClaimType> claimPair = ClaimHandler.getClaim(commander.level(), chunkPos);

                if(claimPair != null) {
                    if(claimPair.getFirst().equals("t:" + commander.getFaction().getName())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {
        BlockPos pos = null;
        BlockPos targetPos = commander.getMemory(ModMemoryTypes.SPAWN_SQUAD_POS.get()).orElse(null);

        for(int i = 0; i < 10; i++) {
            pos = HtweaksUtils.func_221244_a(targetPos, 16, level);

            if(pos != null) {
                break;
            }
        }

        if(pos != null) {
            Faction faction = commander.getFaction();
            Patrol patrol = Htweaks.getPatrolManager().createPatrol(faction);

            patrol.setPosAndClaimArea(new ChunkPos(pos));
        }
        else {
            commander.getLogisticsManager().addResources(this.requiredResources());
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
        return 600;
    }

    @Override
    public double requiredResources() {
        return HTServerConfig.SQUAD_COST.get();
    }

    @Override
    public boolean getIsFinished() {
        return isFinished;
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(ModMemoryTypes.SPAWN_SQUAD_POS.get(), MemoryStatus.VALUE_PRESENT));
    }
}
