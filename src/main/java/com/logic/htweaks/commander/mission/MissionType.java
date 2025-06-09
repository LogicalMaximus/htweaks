package com.logic.htweaks.commander.mission;

public enum MissionType {
    REINFORCE("reinforce"),
    OCCUPY("occupy"),
    ASSAULT("assault");

    private final String name;

    MissionType(String name) {
        this.name = name;
    }

    public static MissionType get(String name) {
        return MissionType.valueOf(name);
    }

    public String getName() {
        return name;
    }

}
