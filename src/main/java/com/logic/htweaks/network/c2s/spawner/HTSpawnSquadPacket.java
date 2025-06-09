package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.CovenantSquadSpawnerUtils;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnSquadPacket extends AbstractSquadSpawnerPacket {

    public HTSpawnSquadPacket() {

    }

    public HTSpawnSquadPacket(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        if(factionID == 3) {
            CovenantSquadSpawnerUtils.spawnSquad(patrol, pos, level, null);
        }
        else {
            HumanSquadSpawnerUtils.spawnSquad(patrol, factionID, pos, level, null);
        }
    }
}
