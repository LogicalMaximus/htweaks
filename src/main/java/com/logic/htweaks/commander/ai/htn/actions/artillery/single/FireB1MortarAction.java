package com.logic.htweaks.commander.ai.htn.actions.artillery.single;

import com.logic.htweaks.config.HTServerConfig;
import net.genzyuro.artillerysupport.entity.ArtillerySupportEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;

public class FireB1MortarAction extends AbstractFireSupportAction {
    private static final int DEFAULT_SHOTS_AMOUNT = 16;

    @Override
    int getMaxDistance() {
        return HTServerConfig.B1_MORTAR_RANGE.get();
    }

    @Override
    int getInterval() {
        return 8;
    }

    @Override
    int getShotAmount() {
        return DEFAULT_SHOTS_AMOUNT;
    }

    @Override
    EntityType<? extends ThrowableItemProjectile> getProjectile() {
        return ArtillerySupportEntities.B1_MORTAR_PROJECTILE.get();
    }

    @Override
    public long getCooldown() {
        return 800L;
    }

    @Override
    public double requiredResources() {
        return HTServerConfig.B1_MORTAR_COST.get();
    }


}
