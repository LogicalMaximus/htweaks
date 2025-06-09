package com.logic.htweaks.faction;

import com.logic.htweaks.commander.objective.Objective;
import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.UUID;

public class PlayerFactionSaveData extends SavedData {
    private static final String SAVE_DATA = "playerFaction";

    private ServerLevel level;

    public PlayerFactionSaveData() {
        this.level = ServerLifecycleHooks.getCurrentServer().overworld();
    }

    private int resources;

    private int manpower;

    private Objective headquarters;

    public Objective getHeadquarters() {
        return headquarters;
    }

    public void createHeadquarters(BlockPos pos, int radius) {
        this.headquarters = new Objective(this.level, UUID.randomUUID(), new Waypoint(pos, radius));
        this.headquarters.htweaks$setFaction(Factions.UNSC);

        this.setDirty();
    }

    private void setHeadquarters(Objective headquarters) {
        this.headquarters = headquarters;
    }

    public int getResources() {
        return resources;
    }

    public int getManpower() {
        return manpower;
    }

    public void increaseResources(int resources) {
        this.resources += resources;

        this.setDirty();
    }

    public void decreaseResources(int resources) {
        this.resources -= resources;

        this.setDirty();
    }

    private void setResources(int resources) {
        this.resources = resources;

        this.setDirty();
    }

    public void increaseManpower(int manpower) {
        this.manpower += manpower;

        this.setDirty();
    }

    public void decreaseManpower(int manpower) {
        this.manpower -= manpower;

        this.setDirty();
    }

    private void setManpower(int manpower) {
        this.manpower = manpower;

        this.setDirty();
    }

    public void tick() {
        if(this.headquarters != null) {
            this.headquarters.tick();
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putInt("manpower", this.manpower);
        compoundTag.putInt("resources", this.resources);

        if(this.headquarters != null) {
            CompoundTag headquartersTag = new CompoundTag();

            this.headquarters.save(headquartersTag);

            compoundTag.put("headquartersTag", headquartersTag);
        }

        return compoundTag;
    }

    public static PlayerFactionSaveData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(PlayerFactionSaveData::load, PlayerFactionSaveData::create, SAVE_DATA);
    }

    private static PlayerFactionSaveData create() {
        return new PlayerFactionSaveData();
    }

    private static PlayerFactionSaveData load(CompoundTag compoundTag) {
        PlayerFactionSaveData saveData = PlayerFactionSaveData.create();

        saveData.setResources(compoundTag.getInt("resources"));
        saveData.setManpower(compoundTag.getInt("manpower"));

        if(compoundTag.contains("headquartersTag")) {
            saveData.setHeadquarters(new Objective(saveData.level, compoundTag.getCompound("headquartersTag")));
        }

        return saveData;
    }
}
