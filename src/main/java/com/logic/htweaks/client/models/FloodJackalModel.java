package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.flood.FloodJackalEntity;
import net.minecraft.resources.ResourceLocation;

public class FloodJackalModel extends ScavModel<FloodJackalEntity> {
    public FloodJackalModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/flood_jackal.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/flood_jackal.png"));
    }
}
