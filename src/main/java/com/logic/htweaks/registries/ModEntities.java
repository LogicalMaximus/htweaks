package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.covenant.*;
import com.logic.htweaks.entity.flood.*;
import com.logic.htweaks.entity.unsc.*;
import com.logic.htweaks.entity.vehicle.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.logic.htweaks.entity.projectile.HeatSeekingMissileEntity;

public class ModEntities {

    public static final RegistryObject<EntityType<UNSCMarineEntity>> UNSC_MARINE;

    public static final RegistryObject<EntityType<CovenantEliteEntity>> COVENANT_ELITE;

    public static final RegistryObject<EntityType<UNSCMarineRadioEntity>> UNSC_MARINE_RADIO_ENTITY;

    public static final RegistryObject<EntityType<CovenantEliteRadioEntity>> COVENANT_ELITE_RADIO_ENTITY;

    public static final RegistryObject<EntityType<CovenantGruntEntity>> COVENANT_GRUNT;

    public static final RegistryObject<EntityType<CovenantMajorEliteEntity>> COVENANT_ELITE_MAJOR;

    public static final RegistryObject<EntityType<CovenantBruteEntity>> COVENANT_BRUTE;

    public static final RegistryObject<EntityType<CovenantMajorBruteEntity>> COVENANT_BRUTE_MAJOR;

    public static final RegistryObject<EntityType<UNSCArmyTrooperEntity>> UNSC_ARMY_TROOPER;

    public static final RegistryObject<EntityType<ODSTEntity>> UNSC_ODST;

    public static final RegistryObject<EntityType<HeatSeekingMissileEntity>> HEAT_SEEKING_MISSILE;

    public static final RegistryObject<EntityType<CovenantJackalEntity>> COVENANT_JACKAL;

    public static final RegistryObject<EntityType<FloodBruteEntity>> FLOOD_BRUTE;

    public static final RegistryObject<EntityType<FloodMarineEntity>> FLOOD_MARINE;

    public static final RegistryObject<EntityType<FloodEliteEntity>> FLOOD_ELITE;

    public static final RegistryObject<EntityType<FloodGruntEntity>> FLOOD_GRUNT;

    public static final RegistryObject<EntityType<FloodJackalEntity>> FLOOD_JACKAL;

    //public static final RegistryObject<EntityType<DelayedProjectileEntity>> DELAYED_PROJECTILE;

    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Htweaks.MODID);

    public static void init(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    static {

        HEAT_SEEKING_MISSILE = ENTITY_TYPES.register("heat_seeking_missile", () -> {
            return EntityType.Builder.of(HeatSeekingMissileEntity::new, MobCategory.MISC).sized(2.5F, 0.5F).clientTrackingRange(10).build("heat_seeking_missile");
        });

        //DELAYED_PROJECTILE = ENTITY_TYPES.register("delay_spawned_projectile", () -> {
        //    return EntityType.Builder.of(DelayedProjectileEntity::new, MobCategory.MISC).noSummon().noSave().fireImmune().sized(0.0625F, 0.0625F).clientTrackingRange(5).updateInterval(5).setShouldReceiveVelocityUpdates(false).build("delay_spawned_projectile");
        //});

        UNSC_MARINE = ENTITY_TYPES.register("unsc_marine", () -> {
            return EntityType.Builder.of(UNSCMarineEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("unsc_marine");
        });

        UNSC_ARMY_TROOPER = ENTITY_TYPES.register("unsc_army_trooper", () -> {
            return EntityType.Builder.of(UNSCArmyTrooperEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("unsc_army_trooper");
        });

        UNSC_ODST = ENTITY_TYPES.register("unsc_odst", () -> {
            return EntityType.Builder.of(ODSTEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("unsc_odst");
        });

        COVENANT_BRUTE = ENTITY_TYPES.register("covenant_brute", () -> {
            return EntityType.Builder.of(CovenantBruteEntity::new, MobCategory.MISC).sized(0.9F, 2.7F).build("covenant_brute");
        });

        COVENANT_BRUTE_MAJOR = ENTITY_TYPES.register("covenant_major_brute", () -> {
            return EntityType.Builder.of(CovenantMajorBruteEntity::new, MobCategory.MISC).sized(0.9F, 2.7F).build("covenant_major_brute");
        });

        COVENANT_ELITE_MAJOR = ENTITY_TYPES.register("covenant_major_elite", () -> {
            return EntityType.Builder.of(CovenantMajorEliteEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("covenant_major_elite");
        });

        COVENANT_ELITE = ENTITY_TYPES.register("covenant_elite", () -> {
            return EntityType.Builder.of(CovenantEliteEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("covenant_elite");
        });

        COVENANT_JACKAL = ENTITY_TYPES.register("covenant_jackal", () -> {
            return EntityType.Builder.of(CovenantJackalEntity::new, MobCategory.MISC).sized(0.54F, 1.62F).build("covenant_jackal");
        });

        COVENANT_GRUNT = ENTITY_TYPES.register("covenant_grunt", () -> {
            return EntityType.Builder.of(CovenantGruntEntity::new, MobCategory.MISC).sized(0.6F, 1.3608F).build("covenant_grunt");
        });

        UNSC_MARINE_RADIO_ENTITY = ENTITY_TYPES.register("unsc_marine_radio", () -> {
            return EntityType.Builder.of(UNSCMarineRadioEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("unsc_marine_radio");
        });

        COVENANT_ELITE_RADIO_ENTITY = ENTITY_TYPES.register("covenant_elite_radio", () -> {
            return EntityType.Builder.of(CovenantEliteRadioEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("covenant_elite_radio");
        });

        FLOOD_MARINE = ENTITY_TYPES.register("flood_marine", () -> {
            return EntityType.Builder.of(FloodMarineEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("flood_marine");
        });

        FLOOD_ELITE = ENTITY_TYPES.register("flood_elite", () -> {
            return EntityType.Builder.of(FloodEliteEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build("flood_elite");
        });

        FLOOD_JACKAL = ENTITY_TYPES.register("flood_jackal", () -> {
            return EntityType.Builder.of(FloodJackalEntity::new, MobCategory.MISC).sized(0.54F, 1.62F).build("flood_jackal");
        });

        FLOOD_BRUTE = ENTITY_TYPES.register("flood_brute", () -> {
            return EntityType.Builder.of(FloodBruteEntity::new, MobCategory.MISC).sized(0.9F, 2.7F).build("flood_brute");
        });

        FLOOD_GRUNT = ENTITY_TYPES.register("flood_grunt", () -> {
            return EntityType.Builder.of(FloodGruntEntity::new, MobCategory.MISC).sized(0.6F, 1.3608F).build("flood_grunt");
        });
    }
}
