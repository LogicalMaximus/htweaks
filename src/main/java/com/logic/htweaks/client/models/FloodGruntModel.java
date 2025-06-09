package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.flood.FloodGruntEntity;
import net.minecraft.resources.ResourceLocation;

public class FloodGruntModel extends ScavModel<FloodGruntEntity> {
    public FloodGruntModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/flood_grunt.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/flood_grunt.png"));
    }
}
