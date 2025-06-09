package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnSniperTeamPacket extends AbstractSquadSpawnerPacket {
    public HTSpawnSniperTeamPacket() {

    }

    public HTSpawnSniperTeamPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        HumanSquadSpawnerUtils.spawnSniperTeam(patrol, factionID, pos, level, null, null);
    }
}
