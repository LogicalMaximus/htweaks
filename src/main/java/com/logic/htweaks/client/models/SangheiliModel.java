package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.covenant.CovenantEliteEntity;
import net.minecraft.resources.ResourceLocation;

public class SangheiliModel extends ScavModel<CovenantEliteEntity> {
    public SangheiliModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/sangheili.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/sangheili.png"));
    }
}
