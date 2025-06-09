package com.logic.htweaks.faction;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.entity.vehicle.IVehicleEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class Faction {
    private final List<Faction> enemyFactions = new ArrayList<>();

    private final int id;

    private final String name;

    private final String displayName;

    private final ChatFormatting color;

    private final boolean hasAICommander;

    private List<EntityType<?>> vehicleTypes = new ArrayList<>();

    private List<EntityType<? extends AbstractScavEntity>> infantryTypes = new ArrayList<>();

    public Faction(int id, String name, String displayName, ChatFormatting color, boolean hasAICommander) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.hasAICommander = hasAICommander;
    }

    public List<Faction> getEnemyFactions() {
        return enemyFactions;
    }

    public void addEnemyFaction(Faction enemyFaction) {
        enemyFactions.add(enemyFaction);
    }

    public List<EntityType<?>> getVehicleTypes() {
        return vehicleTypes;
    }

    public List<EntityType<? extends AbstractScavEntity>> getInfantryTypes() {
        return infantryTypes;
    }

    public void addInfantryType(EntityType<? extends AbstractScavEntity> type) {
        infantryTypes.add(type);
    }

    public void addVehicleTypes(EntityType type) {
        vehicleTypes.add(type);
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public boolean isHasAICommander() {
        return hasAICommander;
    }
}
