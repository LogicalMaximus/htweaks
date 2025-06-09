package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.unsc.BaseTrooperEntity;
import com.logic.htweaks.entity.unsc.UNSCMarineEntity;
import net.minecraft.resources.ResourceLocation;

public class UNSCMarineModel extends ScavModel<BaseTrooperEntity> {
    public UNSCMarineModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/unsc_marine.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/unsc_marine.png"));
    }
}