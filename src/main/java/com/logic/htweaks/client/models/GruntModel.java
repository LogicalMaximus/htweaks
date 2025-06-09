package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.covenant.CovenantGruntEntity;
import net.minecraft.resources.ResourceLocation;

public class GruntModel extends ScavModel<CovenantGruntEntity> {
    public GruntModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/covenant_grunt.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/covenant_grunt.png"));
    }

}
