package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.covenant.CovenantJackalEntity;
import net.minecraft.resources.ResourceLocation;

public class JackalModel extends ScavModel<CovenantJackalEntity> {
    public JackalModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/jackal.geo.json"),  new ResourceLocation(Htweaks.MODID, "textures/entity/jackal.png"));
    }
}
