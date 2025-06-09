package com.logic.htweaks.client.models;

import com.logic.htweaks.Htweaks;
import net.minecraft.resources.ResourceLocation;
import com.logic.htweaks.entity.projectile.HeatSeekingMissileEntity;
import software.bernie.geckolib.model.GeoModel;

public class HeatSeekingMissileModel extends GeoModel<HeatSeekingMissileEntity> {
    @Override
    public ResourceLocation getModelResource(HeatSeekingMissileEntity heatSeekingMissileModel) {
        return new ResourceLocation(Htweaks.MODID, "geo/heatseeking_missile.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HeatSeekingMissileEntity heatSeekingMissileModel) {
        return new ResourceLocation(Htweaks.MODID, "textures/entity/heatseeking_missile.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HeatSeekingMissileEntity heatSeekingMissileModel) {
        return null;
    }


}
