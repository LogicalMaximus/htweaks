package com.logic.htweaks.client.renderers;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.corrinedev.tacznpcs.common.entity.DutyEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.client.models.UNSCMarineModel;
import com.logic.htweaks.entity.unsc.BaseTrooperEntity;
import com.logic.htweaks.entity.unsc.UNSCMarineEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class UNSCMarineRenderer extends ScavRenderer<BaseTrooperEntity> {
    public UNSCMarineRenderer(EntityRendererProvider.Context context) {
        super(context, new UNSCMarineModel());
    }
}
