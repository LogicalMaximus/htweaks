package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.BruteModel;
import com.logic.htweaks.entity.covenant.CovenantBruteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class CovenantBruteRenderer extends ScavRenderer<CovenantBruteEntity> {
    public CovenantBruteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BruteModel());
    }
}
