package com.logic.htweaks.client.patrol;

import net.minecraft.core.BlockPos;

import java.util.UUID;

public class ClientPatrol {
    private final BlockPos pos;

    private final UUID uuid;

    public ClientPatrol(BlockPos pos, UUID uuid) {
        this.pos = pos;
        this.uuid = uuid;
    }

    public BlockPos getPos() {
        return pos;
    }

    public UUID getUuid() {
        return uuid;
    }
}
