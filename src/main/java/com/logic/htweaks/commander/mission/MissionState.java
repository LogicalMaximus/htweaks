package com.logic.htweaks.commander.mission;

public enum MissionState {
    COMPLETE_SUCCESSFUL("complete_successful"),
    COMPLETE_FAILED("complete_failed"),
    UNDER_PROGRESS("under_progress")
    ;

    private final String name;

    MissionState(String name) {
        this.name = name;
    }

    public static MissionState get(String name) {
        return MissionState.valueOf(name);
    }

    public String getName() {
        return name;
    }
 }
