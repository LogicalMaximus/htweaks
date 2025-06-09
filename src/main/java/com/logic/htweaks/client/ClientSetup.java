package com.logic.htweaks.client;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.client.patrol.ClientPatrolManager;
import com.logic.htweaks.client.renderers.*;
import com.logic.htweaks.registries.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Htweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {



    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //MenuScreens.register(ModMenuTypes.SQUAD_SPAWNER_MENU.get(), SquadSpawnerScreen::new);
        });
    }



    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(ModEntities.UNSC_MARINE.get(), UNSCMarineRenderer::new);
        EntityRenderers.register(ModEntities.UNSC_ARMY_TROOPER.get(), UNSCMarineRenderer::new);
        EntityRenderers.register(ModEntities.UNSC_ODST.get(), UNSCMarineRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_ELITE.get(), CovenantEliteRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_JACKAL.get(), CovenantJackalRenderer::new);
        EntityRenderers.register(ModEntities.UNSC_MARINE_RADIO_ENTITY.get(), UNSCMarineRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_ELITE_RADIO_ENTITY.get(), CovenantEliteRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_GRUNT.get(), CovenantGruntRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_ELITE_MAJOR.get(), CovenantEliteRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_BRUTE.get(), CovenantBruteRenderer::new);
        EntityRenderers.register(ModEntities.COVENANT_BRUTE_MAJOR.get(), CovenantBruteRenderer::new);
        EntityRenderers.register(ModEntities.HEAT_SEEKING_MISSILE.get(), HeatSeekingMissileRenderer::new);

        EntityRenderers.register(ModEntities.FLOOD_MARINE.get(), FloodMarineRenderer::new);
        EntityRenderers.register(ModEntities.FLOOD_ELITE.get(), FloodEliteRenderer::new);
        EntityRenderers.register(ModEntities.FLOOD_GRUNT.get(), FloodGruntRenderer::new);
        EntityRenderers.register(ModEntities.FLOOD_BRUTE.get(), FloodBruteRenderer::new);
        EntityRenderers.register(ModEntities.FLOOD_JACKAL.get(), FloodJackalRenderer::new);
    }

    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ModKeyMappings.SQUAD_COMMAND_KEY.get());
    }
}
