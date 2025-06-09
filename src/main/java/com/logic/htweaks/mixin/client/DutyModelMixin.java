package com.logic.htweaks.mixin.client;

import com.corrinedev.tacznpcs.client.models.DutyModel;
import com.corrinedev.tacznpcs.client.models.ScavModel;
import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.Htweaks;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DutyModel.class)
public abstract class DutyModelMixin extends ScavModel<DutyEntity> {
    public DutyModelMixin() {
        super(new ResourceLocation(Htweaks.MODID, "geo/unsc_marine.geo.json"), new ResourceLocation("tacz_npc", "textures/entity/duty.png"));
    }

    @Override
    public ResourceLocation getModelResource(AbstractScavEntity object) {
        return new ResourceLocation(Htweaks.MODID, "geo/unsc_marine.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractScavEntity object) {
        return new ResourceLocation("tacz_npc", "textures/entity/duty.png");
    }
}
