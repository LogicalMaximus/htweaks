package com.logic.htweaks.client;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.client.patrol.ClientPatrolManager;
import com.logic.htweaks.client.screen.SquadSpawnerScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Htweaks.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    public static ClientPatrolManager patrolManager;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            while(ModKeyMappings.SQUAD_COMMAND_KEY.get().consumeClick()) {
                ClientHooks.openSquadCommandScreen();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        patrolManager = new ClientPatrolManager();
    }

    @SubscribeEvent
    public static void onPlayerLoggedOutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        patrolManager = new ClientPatrolManager();
    }
}
