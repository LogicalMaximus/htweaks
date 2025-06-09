package com.logic.htweaks.client.models;

import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.entity.covenant.CovenantBruteEntity;
import net.minecraft.resources.ResourceLocation;

public class BruteModel extends ScavModel<CovenantBruteEntity> {

    public BruteModel() {
        super(new ResourceLocation(Htweaks.MODID, "geo/covenant_brute.geo.json"), new ResourceLocation(Htweaks.MODID, "textures/entity/brute.png"));
    }

}
