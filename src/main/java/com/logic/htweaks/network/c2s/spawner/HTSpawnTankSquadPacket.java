package com.logic.htweaks.network.c2s.spawner;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.CovenantSquadSpawnerUtils;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnTankSquadPacket extends AbstractSquadSpawnerPacket {

    public HTSpawnTankSquadPacket() {

    }

    public HTSpawnTankSquadPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        if(factionID == 3) {
            AbstractScavEntity tankCommander = CovenantSquadSpawnerUtils.spawnTank(patrol, pos, level, null);
            CovenantSquadSpawnerUtils.spawnFireteam(patrol, pos, level, tankCommander, null);
            CovenantSquadSpawnerUtils.spawnFireteam(patrol, pos, level, tankCommander, null);
            CovenantSquadSpawnerUtils.spawnFireteam(patrol, pos, level, tankCommander, null);
        } else {
            AbstractScavEntity tankCommander = HumanSquadSpawnerUtils.spawnTank(patrol, factionID, pos, level, null);
            HumanSquadSpawnerUtils.spawnFireteam(patrol, factionID, pos, level, tankCommander, null, false);
            HumanSquadSpawnerUtils.spawnFireteam(patrol, factionID, pos, level, tankCommander, null, false);
            HumanSquadSpawnerUtils.spawnFireteam(patrol, factionID, pos, level, tankCommander, null, true);
        }
    }
}
