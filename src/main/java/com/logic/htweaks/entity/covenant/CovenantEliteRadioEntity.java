package com.logic.htweaks.entity.covenant;

import com.logic.htweaks.entity.IArtillerySummoner;
import net.genzyuro.artillerysupport.entity.smokebomb.B1MortarConcentratedFireSmokeBombProjectileEntity;
import net.genzyuro.artillerysupport.entity.smokebomb.B1MortarContinuousFireSmokeBombProjectileEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class CovenantEliteRadioEntity extends CovenantEliteEntity implements IArtillerySummoner {
    public CovenantEliteRadioEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
    }

    @Override
    public void summonArtillery() {
        double chance = this.random.nextDouble();

        if(chance < 0.5) {
            B1MortarConcentratedFireSmokeBombProjectileEntity entity = new B1MortarConcentratedFireSmokeBombProjectileEntity(level(), this);
            entity.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 2.8F, 1.0F);
            this.level().addFreshEntity(entity);
        }
        else {
            B1MortarContinuousFireSmokeBombProjectileEntity entity = new B1MortarContinuousFireSmokeBombProjectileEntity(level(), this);
            entity.shootFromRotation(this, this.getXRot(), this.getYRot(), 0.0F, 2.8F, 1.0F);
            this.level().addFreshEntity(entity);
        }
    }
}
