package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.FloodBruteModel;
import com.logic.htweaks.entity.flood.FloodBruteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class FloodBruteRenderer extends ScavRenderer<FloodBruteEntity> {
    public FloodBruteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FloodBruteModel());
    }
}
