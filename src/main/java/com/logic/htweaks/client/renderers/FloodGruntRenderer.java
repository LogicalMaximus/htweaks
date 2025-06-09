package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.FloodGruntModel;
import com.logic.htweaks.entity.flood.FloodGruntEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class FloodGruntRenderer extends ScavRenderer<FloodGruntEntity> {
    public FloodGruntRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FloodGruntModel());
    }
}
