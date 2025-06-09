package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.FloodJackalModel;
import com.logic.htweaks.entity.flood.FloodJackalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class FloodJackalRenderer extends ScavRenderer<FloodJackalEntity> {
    public FloodJackalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FloodJackalModel());
    }
}
