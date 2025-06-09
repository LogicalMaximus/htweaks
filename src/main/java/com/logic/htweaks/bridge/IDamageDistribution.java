package com.logic.htweaks.bridge;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nonnull;

public interface IDamageDistribution {
    float htweaks$distributeDamage(float damage, @Nonnull LivingEntity livingEntity, @Nonnull DamageSource source, boolean addStat);
}
