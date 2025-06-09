package com.logic.htweaks.network.c2s.spawner;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.CovenantSquadSpawnerUtils;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnAPCSquadPacket extends AbstractSquadSpawnerPacket {
    public HTSpawnAPCSquadPacket() {

    }

    public HTSpawnAPCSquadPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        if(factionID == 1 || factionID == 2) {
            AbstractScavEntity apcCommander = HumanSquadSpawnerUtils.spawnAPC(patrol, factionID, pos, level, null);
            HumanSquadSpawnerUtils.spawnSquad(patrol, factionID, pos, level, apcCommander.getVehicle());
        }
        else if(factionID == 3) {
            AbstractScavEntity apcCommander = CovenantSquadSpawnerUtils.spawnAPC(patrol, pos, level, null);
            CovenantSquadSpawnerUtils.spawnSquad(patrol, pos, level, apcCommander.getVehicle());
        }
    }
}
