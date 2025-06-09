package com.logic.htweaks.commander.ai.htn.actions.artillery.multi;

import com.logic.htweaks.commander.ai.htn.actions.artillery.single.AbstractFireSupportAction;
import com.logic.htweaks.config.HTServerConfig;
import net.genzyuro.artillerysupport.entity.ArtillerySupportEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public class FireMultiLightHowitzerAction extends AbstractMultiTargetFireSupportAction {
    private static final int DEFAULT_SHOTS_AMOUNT = 8;

    @Override
    int getMaxDistance() {
        return HTServerConfig.LIGHT_HOWITZER_RANGE.get();
    }

    @Override
    int getInterval() {
        return 12;
    }

    @Override
    int getShotAmount() {
        return DEFAULT_SHOTS_AMOUNT;
    }

    @Override
    EntityType<? extends ThrowableItemProjectile> getProjectile() {
        return ArtillerySupportEntities.HOWITZER_PROJECTILE.get();
    }

    @Override
    public long getCooldown() {
        return 1000L;
    }

    @Override
    public double requiredResources() {
        return HTServerConfig.LIGHT_MORTAR_COST.get();
    }


}
