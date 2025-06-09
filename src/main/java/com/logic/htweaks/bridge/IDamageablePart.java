package com.logic.htweaks.bridge;

import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface IDamageablePart {
    float htweaks$damage(float amount, @Nullable LivingEntity livingEntity, boolean applyDebuff, float minHealth);
}
