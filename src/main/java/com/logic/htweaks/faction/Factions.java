package com.logic.htweaks.faction;

import com.corrinedev.tacznpcs.common.entity.BanditEntity;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.registries.ModEntities;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class Factions {

    public static Faction DEFAULT = new Faction(0, "default", "Default", ChatFormatting.WHITE, false);

    public static Faction UNSC = new Faction(1, "unsc", "The United Nation's Space Command", ChatFormatting.DARK_GREEN, true);

    public static Faction INSURRECTIONIST = new Faction(2, "insurrectionist", "United Rebel Front", ChatFormatting.DARK_RED, true);

    public static Faction COVENANT = new Faction(3, "covenant", "The Covenant Hegemony", ChatFormatting.DARK_PURPLE, true);

    public static Faction PIRATE = new Faction(4, "pirate", "The Bandit Alliance", ChatFormatting.BLACK, false);

    public static Faction FLOOD = new Faction(5, "flood", "Flood", ChatFormatting.YELLOW, true);

    public static List<Faction> FACTIONS = List.of(DEFAULT, UNSC, INSURRECTIONIST, COVENANT, PIRATE, FLOOD);

    public static void init() {
        addEnemyFactions();
        addInfantryTypes();
        addVehicleTypes();
    }

    public static void addEnemyFactions() {
        PIRATE.addEnemyFaction(INSURRECTIONIST);
        PIRATE.addEnemyFaction(UNSC);
        PIRATE.addEnemyFaction(COVENANT);
        PIRATE.addEnemyFaction(FLOOD);

        INSURRECTIONIST.addEnemyFaction(UNSC);
        INSURRECTIONIST.addEnemyFaction(COVENANT);
        INSURRECTIONIST.addEnemyFaction(PIRATE);
        INSURRECTIONIST.addEnemyFaction(FLOOD);

        UNSC.addEnemyFaction(PIRATE);
        UNSC.addEnemyFaction(INSURRECTIONIST);
        UNSC.addEnemyFaction(COVENANT);
        UNSC.addEnemyFaction(FLOOD);

        COVENANT.addEnemyFaction(PIRATE);
        COVENANT.addEnemyFaction(INSURRECTIONIST);
        COVENANT.addEnemyFaction(UNSC);
        COVENANT.addEnemyFaction(FLOOD);


        FLOOD.addEnemyFaction(PIRATE);
        FLOOD.addEnemyFaction(INSURRECTIONIST);
        FLOOD.addEnemyFaction(UNSC);
        FLOOD.addEnemyFaction(COVENANT);
    }

    @Deprecated
    public static void addInfantryTypes() {
        UNSC.addInfantryType(ModEntities.UNSC_MARINE.get());
        //UNSC.addInfantryType(ModEntities.UNSC_MARINE_RADIO_ENTITY.get());

        PIRATE.addInfantryType(BanditEntity.BANDIT);

        INSURRECTIONIST.addInfantryType(DutyEntity.DUTY);

        COVENANT.addInfantryType(ModEntities.COVENANT_ELITE.get());
    }

    @Deprecated
    public static void addVehicleTypes() {

    }

    public static List<Faction> getFactions() {
        return FACTIONS;
    }

    public static Faction getFactionByString(String id) {
        for(Faction faction : FACTIONS) {
            if(Objects.equals(faction.getName(), id)) {
                return faction;
            }
        }

        return null;
    }

    @Deprecated
    @Nullable
    public static Faction getFactionByID(int id) {
        return switch (id) {
            case 0 -> DEFAULT;
            case 1 -> UNSC;
            case 2 -> INSURRECTIONIST;
            case 3 -> COVENANT;
            case 4 -> PIRATE;
            case 5 -> FLOOD;
            default -> null;
        };
    }
}
