package com.logic.htweaks.commander.ai.htn.actions.artillery.multi;

import com.logic.htweaks.commander.ai.htn.actions.artillery.single.AbstractFireSupportAction;
import com.logic.htweaks.config.HTServerConfig;
import net.genzyuro.artillerysupport.entity.ArtillerySupportEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public class FireMultiLightMortarAction extends AbstractMultiTargetFireSupportAction {
    private static final int DEFAULT_SHOTS_AMOUNT = 10;

    private boolean isFinished;

    @Override
    public long getCooldown() {
        return 600L;
    }

    @Override
    public double requiredResources() {
        return HTServerConfig.LIGHT_MORTAR_COST.get();
    }

    @Override
    int getMaxDistance() {
        return HTServerConfig.LIGHT_MORTAR_RANGE.get();
    }

    @Override
    int getInterval() {
        return 5;
    }

    @Override
    int getShotAmount() {
        return DEFAULT_SHOTS_AMOUNT;
    }

    @Override
    EntityType<? extends ThrowableItemProjectile> getProjectile() {
        return (EntityType)ArtillerySupportEntities.LIGHT_MORTAR_PROJECTILE.get();
    }


}
