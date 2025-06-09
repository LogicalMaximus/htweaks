package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.flood.FloodEliteEntity;
import net.minecraft.resources.ResourceLocation;

public class FloodEliteModel extends ScavModel<FloodEliteEntity> {
    public FloodEliteModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/flood_sangheli.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/flood_sangheili.png"));
    }
}
