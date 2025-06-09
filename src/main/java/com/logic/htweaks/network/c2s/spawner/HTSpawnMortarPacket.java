package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.utils.HumanSquadSpawnerUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;

public class HTSpawnMortarPacket extends AbstractSquadSpawnerPacket {
    public HTSpawnMortarPacket() {

    }

    public HTSpawnMortarPacket(FriendlyByteBuf friendlyByteBuf) {
    }

    @Override
    void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level) {
        HumanSquadSpawnerUtils.spawnMortar(patrol, factionID, pos, level, null);
    }
}
