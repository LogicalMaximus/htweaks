package com.logic.htweaks.bridge;

import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public interface IEntityDamageModel {
    boolean isDead(@Nullable Entity entity);
}
