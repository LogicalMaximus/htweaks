package com.logic.htweaks.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class HTServerConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Float> FLOOD_DAMAGE_RESISTANCE;

    public static final ForgeConfigSpec.ConfigValue<Integer> OBJECTIVE_CAPTURE_TIME;

    public static final ForgeConfigSpec.ConfigValue<Integer> HELICOPTER_FUEL_AMOUNT;

    public static final ForgeConfigSpec.ConfigValue<Double> AI_ACCURACY;

    public static final ForgeConfigSpec.ConfigValue<Double> GRUNT_EXPLODE_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Float> GRUNT_EXPLOSION_RADIUS;

    public static final ForgeConfigSpec.ConfigValue<Integer> HEADQUARTERS_RADIUS;

    public static final ForgeConfigSpec.ConfigValue<Boolean> CONSUME_AMMO_SCAV;

    public static final ForgeConfigSpec.ConfigValue<Boolean> DEBUG_MODE;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_PATROL;

    public static final ForgeConfigSpec.ConfigValue<Float> PIERCE_NEEDED_TO_IGNORE_ARMOR;

    public static final ForgeConfigSpec.ConfigValue<Integer> FACTION_SUPPLY_INTERVAL;

    public static final ForgeConfigSpec.ConfigValue<Double> LONG_RANGE_MISSILE_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> MEDIUM_RANGE_MISSILE_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> SHORT_RANGE_MISSILE_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> LIGHT_MORTAR_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> B1_MORTAR_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> LARGE_HOWITIZER_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> LIGHT_HOWITIZER_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> LANDMINE_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> SQUAD_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> FIRETEAM_COST;

    public static final ForgeConfigSpec.ConfigValue<Double> CHUNK_RESOURCE_MULTIPLIER;

    public static final ForgeConfigSpec.ConfigValue<Integer> COMMANDER_REMEMBER_TIME;

    public static final ForgeConfigSpec.ConfigValue<Integer> LARGE_HOWITZER_RANGE;

    public static final ForgeConfigSpec.ConfigValue<Integer> LIGHT_HOWITZER_RANGE;

    public static final ForgeConfigSpec.ConfigValue<Integer> B1_MORTAR_RANGE;

    public static final ForgeConfigSpec.ConfigValue<Integer> LIGHT_MORTAR_RANGE;

    public static final ForgeConfigSpec.ConfigValue<Integer> WORLD_STATE_INTERVAL;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_NUM_OF_SQUADS;

    public static final ForgeConfigSpec.ConfigValue<Double> TANK_SPAWN_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Double> APC_SPAWN_CHANCE;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_SCAV_CAP;

    public static final ForgeConfigSpec.ConfigValue<Integer> MAX_RESOURCES;

    //public static final ForgeConfigSpec.ConfigValue<Integer> SQUADS_PER_OUTPOST;

    //public static final ForgeConfigSpec.ConfigValue<Integer> SQUADS_PER_CITY;

    static {
        BUILDER.push("Halo Annihilation");

        FLOOD_DAMAGE_RESISTANCE = BUILDER.comment("\nBase Flood Damage Resistance To Bullets \n\t(takes effect after restart)").define("FloodDamageResistance", 0.50F);
        OBJECTIVE_CAPTURE_TIME = BUILDER.comment("\nThe Amount Of Ticks It Takes To Capture An Objective \n\t(takes effect after restart)").define("ObjectiveCaptureTime", 1200);
        HELICOPTER_FUEL_AMOUNT = BUILDER.comment("\nThe Amount Of Fuel Added To Helicopter Per Coal \n\t(takes effect after restart)").define("HelicopterFueldAmount", 75);
        AI_ACCURACY = BUILDER.comment("\nHow Accurate The AI Is \n\t(takes effect after restart)").define("AIAccuracy", 0.80D);
        GRUNT_EXPLODE_CHANCE = BUILDER.comment("\nHow Likely Are Grunts To Explode \n\t(takes effect after restart)").define("GruntExplodeChance", 0.30D);
        GRUNT_EXPLOSION_RADIUS = BUILDER.comment("\nHow Big Is The Grunt Explosion Radius \n\t(takes effect after restart)").define("GruntExplosionRadius", 5F);
        HEADQUARTERS_RADIUS = BUILDER.comment("\nThe Size Of The Headquarters \n\t(takes effect after restart)").define("HeadquartersRadius", 30);
        CONSUME_AMMO_SCAV = BUILDER.comment("\nDo The NPCs use Ammo \n\t(takes effect after restart)").define("CunsomeAmmoScav", false);
        MAX_PATROL = BUILDER.comment("\nThe Max Number Of Patrols That Can Exist At Once \n\t(takes effect after restart)").define("MaaxPatrol", 50);
        PIERCE_NEEDED_TO_IGNORE_ARMOR = BUILDER.comment("\nHow High Does A Weapon's Pierce need to be to ignore vehicle armor \n\t(takes effect after restart)").define("PieceNeededToIgnoreArmor", 3F);
        FACTION_SUPPLY_INTERVAL = BUILDER.comment("\nHow Often AI Factions Get More Supply \n\t(takes effect after restart)").define("FactionSupplyInterval", 1200);
        CHUNK_RESOURCE_MULTIPLIER = BUILDER.comment("\nHow Much The AI Commander Gets From One Chunk \n\t(takes effect after restart)").define("ChunkResourceMultiplier", 0.75);

        BUILDER.pop();

        BUILDER.push("AI Cost");

        LONG_RANGE_MISSILE_COST = BUILDER.comment("\nLong Range Missile Cost \n\t(takes effect after restart)").define("LongRangeMissileCost", 300D);
        MEDIUM_RANGE_MISSILE_COST = BUILDER.comment("\nMedium Range Missile Cost \n\t(takes effect after restart)").define("MediumRangeMissileCost", 200D);
        SHORT_RANGE_MISSILE_COST = BUILDER.comment("\nShort Range Missile Cost \n\t(takes effect after restart)").define("ShortRangeMissileCost", 100D);
        LIGHT_MORTAR_COST = BUILDER.comment("\nLight Mortar Cost \n\t(takes effect after restart)").define("LightMortarCost", 50D);
        B1_MORTAR_COST = BUILDER.comment("\nB1 Mortar Cost \n\t(takes effect after restart)").define("B1MortarCost", 80D);
        LARGE_HOWITIZER_COST = BUILDER.comment("\nLarge Howitzer Cost \n\t(takes effect after restart)").define("LargeHowitzerCost", 90D);
        LIGHT_HOWITIZER_COST = BUILDER.comment("\nLight Howitzer Cost \n\t(takes effect after restart)").define("LightHowitzerCost", 70D);
        LANDMINE_COST = BUILDER.comment("\nLandmine Cost \n\t(takes effect after restart)").define("LandmineCost", 20D);

        SQUAD_COST = BUILDER.comment("\nThe Cost Of A Squad \n\t(takes effect after restart)").define("SquadCost", 200D);
        FIRETEAM_COST = BUILDER.comment("\nThe Cost Of A Fireteam \n\t(takes effect after restart)").define("FireTeamCost",  66D);

        COMMANDER_REMEMBER_TIME = BUILDER.comment("\nHow Long Will A Commander Remember Enemy Locations \n\t(takes effect after restart)").define("CommanderRememberTime", 6000);
        WORLD_STATE_INTERVAL = BUILDER.comment("\nHow Often Does The AI Commander's World State Update \n\t(takes effect after restart)").define("CommanderWorldStateInterval", 1200);


        LARGE_HOWITZER_RANGE = BUILDER.comment("\nThe Range In Chunks A Large Howitzer Should Have \n\t(takes effect after restart)").define("LargeHowizterRange", 1000);
        LIGHT_HOWITZER_RANGE = BUILDER.comment("\nThe Range In Chunks A Light Howitzer Should Have \n\t(takes effect after restart)").define("LightHowizterRange", 700);
        B1_MORTAR_RANGE = BUILDER.comment("\nThe Range In Chunks A B1 Mortar Should Have \n\t(takes effect after restart)").define("B1MortarRange", 400);
        LIGHT_MORTAR_RANGE = BUILDER.comment("\nThe Range In Chunks A Light Mortar Should Have \n\t(takes effect after restart)").define("LightMortarRange", 200);

        MAX_NUM_OF_SQUADS = BUILDER.comment("\nThe Max Number Of Squads A Commander AI Can Have \n\t(takes effect after restart)").define("MaxNumOfSquads", 6);

        TANK_SPAWN_CHANCE = BUILDER.comment("\nThe Chance Of An Ai Commander Using A Tank \n\t(takes effect after restart)").define("TankSpawnChance", 0.05);
        APC_SPAWN_CHANCE = BUILDER.comment("\nThe Chance Of An Ai Commander Using An APC \n\t(takes effect after restart)").define("ApcSpawnChance", 0.08);

        DEBUG_MODE = BUILDER.comment("\nDebug Mode \n\t(takes effect after restart)").define("DebugMode", false);

        MAX_SCAV_CAP = BUILDER.comment("\nThe Max Amount Of TACZ NPCS That Can Spawn \n\t(takes effect after restart)").define("MaxScavCap", 80);

        MAX_RESOURCES = BUILDER.comment("\nThe Max Amount Of RESOURCES an ai commander can have \n\t(takes effect after restart)").define("MaxResourcesLimit", 10000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
