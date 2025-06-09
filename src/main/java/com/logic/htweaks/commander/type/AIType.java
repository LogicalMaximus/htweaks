package com.logic.htweaks.commander.type;

public enum AIType {
    AGGRESSIVE("invasion"),
    DEFENSIVE("occuption")
    ;

    private final String name;

    private AIType(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AIType getAIType(String name) {
        return AIType.valueOf(name);
    }
}
