package com.logic.htweaks.commander.mission;

import com.logic.htweaks.commander.objective.Objective;
import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.nbt.CompoundTag;

public class Mission {

    private final Waypoint waypoint;

    private final MissionType missionType;

    private final Objective objective;

    private MissionState missionState;

    public Mission(Objective objective, Waypoint waypoint, MissionType missionType) {
        this.objective = objective;
        this.waypoint = waypoint;
        this.missionType = missionType;
    }

    public Mission(Objective objective, CompoundTag tag) {
        this.objective = objective;

        this.waypoint = new Waypoint((CompoundTag) tag.get("waypoint"));

        this.missionType = MissionType.get(tag.getString("missionType"));

        if(tag.contains("missionState")) {
            this.missionState = MissionState.get(tag.getString("missionState"));
        }
    }

    //returns The Status Of The Mission
    public MissionState getMissionState() {
        return missionState;
    }

    public void setMissionState(MissionState missionState) {
        this.missionState = missionState;
    }

    public Waypoint getWaypoint() {
        return waypoint;
    }

    public void save(CompoundTag tag) {
        CompoundTag waypointTag = new CompoundTag();
        this.waypoint.save(waypointTag);

        tag.put("waypoint", waypointTag);

        tag.putString("missionType", this.missionType.getName());

        if(this.missionState != null) {
            tag.putString("missionState", this.missionState.getName());
        }
    }

    public Objective getObjective() {
        return objective;
    }
}
