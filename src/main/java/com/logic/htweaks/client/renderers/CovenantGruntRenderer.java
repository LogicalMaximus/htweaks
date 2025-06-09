package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.GruntModel;
import com.logic.htweaks.entity.covenant.CovenantGruntEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CovenantGruntRenderer extends ScavRenderer<CovenantGruntEntity> {
    public CovenantGruntRenderer(EntityRendererProvider.Context context) {
        super(context, new GruntModel());
    }
}
