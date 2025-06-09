package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.FloodEliteModel;
import com.logic.htweaks.entity.flood.FloodEliteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class FloodEliteRenderer extends ScavRenderer<FloodEliteEntity> {
    public FloodEliteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FloodEliteModel());
    }
}
