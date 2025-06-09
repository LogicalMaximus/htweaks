package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.FloodMarineModel;
import com.logic.htweaks.entity.flood.FloodMarineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class FloodMarineRenderer extends ScavRenderer<FloodMarineEntity> {

    public FloodMarineRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FloodMarineModel());
    }
}
