package com.logic.htweaks.mixin.client;

import com.corrinedev.tacznpcs.client.renderer.ScavRenderer;
import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

@Mixin(ScavRenderer.class)
public abstract class MixinScavRenderer<T extends AbstractScavEntity> extends GeoEntityRenderer<T> {
    public MixinScavRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Unique
    public BlockAndItemGeoLayer<T> htweaks$ITEMLAYER = new BlockAndItemGeoLayer<T>(this) {
        protected @Nullable ItemStack getStackForBone(GeoBone bone, T animatable) {
            ItemStack var10000;
            switch (bone.getName()) {
                case "third_person_left_hand" -> var10000 = animatable.getOffhandItem();
                default -> var10000 = null;
            }

            return var10000;
        }

        protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
            String var4 = bone.getName();
            byte var5 = -1;
            var4.hashCode();
            switch (var5) {
                default -> {
                    return ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
                }
            }
        }

        protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
        }
    };

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityRendererProvider.Context renderManager, GeoModel model, CallbackInfo ci) {
        this.addRenderLayer(htweaks$ITEMLAYER);
    }
}
