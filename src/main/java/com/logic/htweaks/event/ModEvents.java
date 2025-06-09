package com.logic.htweaks.event;

import com.logic.htweaks.entity.covenant.*;
import com.logic.htweaks.entity.flood.*;
import com.logic.htweaks.entity.unsc.*;
import com.logic.htweaks.registries.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModEvents {

    @SubscribeEvent
    public static void onAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.UNSC_MARINE.get(), UNSCMarineEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_ELITE.get(), CovenantEliteEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_JACKAL.get(), CovenantJackalEntity.createAttributes().build());
        event.put(ModEntities.UNSC_MARINE_RADIO_ENTITY.get(), UNSCMarineRadioEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_ELITE_RADIO_ENTITY.get(), CovenantEliteEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_GRUNT.get(), CovenantGruntEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_ELITE_MAJOR.get(), CovenantMajorEliteEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_BRUTE.get(), CovenantBruteEntity.createAttributes().build());
        event.put(ModEntities.COVENANT_BRUTE_MAJOR.get(), CovenantMajorBruteEntity.createAttributes().build());
        event.put(ModEntities.UNSC_ARMY_TROOPER.get(), UNSCArmyTrooperEntity.createAttributes().build());
        event.put(ModEntities.UNSC_ODST.get(), ODSTEntity.createAttributes().build());

        event.put(ModEntities.FLOOD_MARINE.get(), FloodMarineEntity.createAttributes().build());
        event.put(ModEntities.FLOOD_ELITE.get(), FloodEliteEntity.createAttributes().build());
        event.put(ModEntities.FLOOD_JACKAL.get(), FloodJackalEntity.createAttributes().build());
        event.put(ModEntities.FLOOD_GRUNT.get(), FloodGruntEntity.createAttributes().build());
        event.put(ModEntities.FLOOD_BRUTE.get(), FloodBruteEntity.createAttributes().build());
    }
}
