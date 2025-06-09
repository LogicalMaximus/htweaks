package com.logic.htweaks.client;

import com.logic.htweaks.client.patrol.ClientPatrolManager;
import com.logic.htweaks.client.screen.FactionCommandScreen;
import com.logic.htweaks.client.screen.SquadCommandScreen;
import com.logic.htweaks.client.screen.SquadSpawnerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.UUID;

public class ClientHooks {
    public static void openSquadSpawnerScreen() {
        Minecraft.getInstance().setScreen(new SquadSpawnerScreen());
    }

    public static void openSquadCommandScreen(){
        Minecraft.getInstance().setScreen(new SquadCommandScreen());
    }

    public static void openFactionCommandScreen(){
        Minecraft.getInstance().setScreen(new FactionCommandScreen());
    }

    public static ClientPatrolManager getPatrolManager() {
        return ClientEvents.patrolManager;
    }

    public static void updateClientPatrolManager(HashMap<UUID, BlockPos> patrolData) {
        ClientPatrolManager patrolManager = ClientHooks.getPatrolManager();

        if(patrolManager != null) {
            patrolManager.clearPatrols();

            for(UUID uuid : patrolData.keySet()) {
                patrolManager.createPatrol(patrolData.get(uuid), uuid);
            }
        }
    }
}
