package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.logic.htweaks.client.models.SangheiliModel;
import com.logic.htweaks.entity.covenant.CovenantEliteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CovenantEliteRenderer extends ScavRenderer<CovenantEliteEntity> {
    public CovenantEliteRenderer(EntityRendererProvider.Context context) {
        super(context, new SangheiliModel());
    }

}
