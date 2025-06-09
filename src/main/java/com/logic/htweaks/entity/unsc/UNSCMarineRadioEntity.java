package com.logic.htweaks.entity.unsc;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.entity.IArtillerySummoner;
import com.logic.htweaks.entity.behavior.TaczArtilleryStrike;
import net.genzyuro.artillerysupport.entity.smokebomb.MortarConcentratedFireSmokeBombProjectileEntity;
import net.genzyuro.artillerysupport.entity.smokebomb.MortarContinuousFireSmokeBombProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;

public class UNSCMarineRadioEntity extends UNSCMarineEntity implements IArtillerySummoner {
    public UNSCMarineRadioEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    public void summonArtillery() {
        double chance = this.random.nextDouble();

        if(chance < 0.5) {
            MortarConcentratedFireSmokeBombProjectileEntity entity = new MortarConcentratedFireSmokeBombProjectileEntity(level(), this);
            entity.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 2.8F, 1.0F);
            this.level().addFreshEntity(entity);
        }
        else {
            MortarContinuousFireSmokeBombProjectileEntity entity = new MortarContinuousFireSmokeBombProjectileEntity(level(), this);
            entity.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 2.8F, 1.0F);
            this.level().addFreshEntity(entity);
        }
    }
}
