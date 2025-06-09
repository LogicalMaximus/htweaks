package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.flood.FloodMarineEntity;
import net.minecraft.resources.ResourceLocation;

public class FloodMarineModel extends ScavModel<FloodMarineEntity> {
    public FloodMarineModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/flood_marine.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/flood_marine.png"));
    }
}
