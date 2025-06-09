package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.CovenantSquadSpawnerUtils;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnFireTeamPacket extends AbstractSquadSpawnerPacket {
    public HTSpawnFireTeamPacket() {

    }

    public HTSpawnFireTeamPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        if(factionID == 3) {
            CovenantSquadSpawnerUtils.spawnFireteam(patrol, pos, level, null, null);
        }
        else {
            HumanSquadSpawnerUtils.spawnFireteam(patrol, factionID, pos, level, null, null, false);
        }
    }
}
