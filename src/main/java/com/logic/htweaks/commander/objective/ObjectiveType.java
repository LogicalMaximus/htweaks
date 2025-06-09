package com.logic.htweaks.commander.objective;

public enum ObjectiveType {
    CIVILIAN("civilian"),
    MILITARY("military")
    ;

    private final String name;

    private ObjectiveType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
