package com.logic.htweaks.commander.ai.htn.actions.artillery.single;

import com.logic.htweaks.config.HTServerConfig;
import net.genzyuro.artillerysupport.entity.ArtillerySupportEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public class FireLargeHowitzerAction extends AbstractFireSupportAction {
    private static final int DEFAULT_SHOTS_AMOUNT = 30;

    @Override
    int getMaxDistance() {
        return HTServerConfig.LARGE_HOWITZER_RANGE.get();
    }

    @Override
    int getInterval() {
        return 18;
    }

    @Override
    int getShotAmount() {
        return DEFAULT_SHOTS_AMOUNT;
    }

    @Override
    EntityType<? extends ThrowableItemProjectile> getProjectile() {
        return ArtillerySupportEntities.LARGE_HOWITZER_PROJECTILE.get();
    }

    @Override
    public long getCooldown() {
        return 1500;
    }

    @Override
    public double requiredResources() {
        return HTServerConfig.LARGE_HOWITIZER_COST.get();
    }
}
