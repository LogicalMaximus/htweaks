package com.logic.htweaks.mixin;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Predicate;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {

    @Inject(method = "applyBiomeDecoration", at = @At("RETURN"), locals = LocalCapture.PRINT)
    public void applyBiomeDecoration(WorldGenLevel p_223087_, ChunkAccess p_223088_, StructureManager p_223089_, CallbackInfo ci, ChunkPos $$3) {

    }
}
