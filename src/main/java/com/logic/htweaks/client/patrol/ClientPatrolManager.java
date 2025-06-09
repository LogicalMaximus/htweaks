package com.logic.htweaks.client.patrol;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientPatrolManager {

    private final List<ClientPatrol> patrols = new ArrayList<>();


    public void createPatrol(BlockPos pos, UUID uuid) {
        ClientPatrol patrol = new ClientPatrol(pos, uuid);

        patrols.add(patrol);
    }

    public void removePatrol(UUID uuid) {
        for(ClientPatrol patrol : patrols) {
            if(patrol.getUuid() == uuid) {
                patrols.remove(patrol);
                break;
            }
        }
    }

    public void clearPatrols() {
        patrols.clear();
    }

    public List<ClientPatrol> getPatrols() {
        return patrols;
    }
}
