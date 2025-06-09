package com.logic.htweaks.commander.tactical;

import com.logic.htweaks.commander.MiltaryAiCommander;
import net.minecraft.nbt.CompoundTag;

@Deprecated
public class TacticalAICommander {
    private final MiltaryAiCommander commander;

    public TacticalAICommander(MiltaryAiCommander commander) {
        this.commander = commander;
    }


    public void save(CompoundTag tag) {
    }
}
