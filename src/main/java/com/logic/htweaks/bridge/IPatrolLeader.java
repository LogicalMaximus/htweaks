package com.logic.htweaks.bridge;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPatrolLeader {
    void htweaks$clearPatrolLeader();

    void htweaks$setPatrolLeader(@NotNull Entity patrolLeader);

    public abstract @Nullable Entity htweaks$getPatrolLeader();

    boolean shouldFollowCommands(Entity entity);
}
