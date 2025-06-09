package com.logic.htweaks.client.renderers;

import com.logic.htweaks.client.models.HeatSeekingMissileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import com.logic.htweaks.entity.projectile.HeatSeekingMissileEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HeatSeekingMissileRenderer extends GeoEntityRenderer<HeatSeekingMissileEntity> {
    public HeatSeekingMissileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HeatSeekingMissileModel());
    }
}
