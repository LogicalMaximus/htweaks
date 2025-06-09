package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.JackalModel;
import com.logic.htweaks.entity.covenant.CovenantJackalEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class CovenantJackalRenderer extends ScavRenderer<CovenantJackalEntity> {
    public CovenantJackalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new JackalModel());
    }
}
