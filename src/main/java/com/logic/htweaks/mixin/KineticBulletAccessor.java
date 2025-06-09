package com.logic.htweaks.mixin;

import com.tacz.guns.entity.EntityKineticBullet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityKineticBullet.class)
public interface KineticBulletAccessor {

    @Accessor
    public boolean getExplosion();

    @Accessor
    float getArmorIgnore();

    @Accessor
    boolean getIgniteEntity();

    @Accessor
    float getSpeed();
}
