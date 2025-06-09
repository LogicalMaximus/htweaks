package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.flood.FloodBruteEntity;
import net.minecraft.resources.ResourceLocation;

public class FloodBruteModel extends ScavModel<FloodBruteEntity> {
    public FloodBruteModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/flood_brute.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/flood_brute.png"));
    }
}
