package com.logic.htweaks.mixin;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.installations.StructureSaveData;
import mcjty.lostcities.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlacedFeature.class)
public class MixinPlacedFeature {


    @Inject(method = "placeWithContext", at = @At("RETURN"))
    private void placeWithContext(PlacementContext p_226369_, RandomSource p_226370_, BlockPos p_226371_, CallbackInfoReturnable<Boolean> cir) {
        if(cir.getReturnValue()) {
            if(((PlacedFeature)(Object)this).feature().is(new ResourceLocation("lostcities","lostcity"))) {
                StructureSaveData structureSaveData = Htweaks.getStructureSaveData();

                if(structureSaveData != null) {
                    structureSaveData.addStructurePos(new ChunkPos(p_226371_));
                }
            }
        }
    }
}
