package com.logic.htweaks.commander.operation;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.mission.Mission;
import com.logic.htweaks.commander.mission.MissionType;
import com.logic.htweaks.commander.objective.Objective;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Deprecated
public class OperationAICommander {
    private final MiltaryAiCommander commander;

    private List<Patrol> patrols = new ArrayList<>();

    private List<Patrol> patrolsOnMission = new ArrayList<>();

    private List<Mission> missions = new ArrayList<>();

    private List<Mission> plannedMissions = new ArrayList<>();

    public OperationAICommander(MiltaryAiCommander commander) {
        this.commander = commander;
    }

    public void tick() {

    }

    public void analyseMissions() {

    }

    public void planObjectiveMissions(Objective objective, MissionType missionType) {
        for(Waypoint waypoint : objective.getInstallations()) {
            createMission(objective, waypoint, missionType);
        }
    }

    public void planWaypointMission(Objective objective, Waypoint waypoint, MissionType missionType) {
        createMission(objective, waypoint, missionType);
    }

    public void requestSquadsForMission() {
        int numOfSquads = this.plannedMissions.size();

        if(numOfSquads > 0) {
            //TO DO: Send request To AI Commander For More Squads
        }
    }

    public void executeMissions(Objective objective) {

    }

    //Creates Missions For Patrols Based On Situation
    public void createMission(Objective objective, Waypoint waypoint, MissionType missionType) {
        this.plannedMissions.add(new Mission(objective, waypoint, missionType));
    }

    public void save(CompoundTag tag) {


    }

    public void load(CompoundTag tag) {

    }


}
