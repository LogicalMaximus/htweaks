package com.logic.htweaks.mixin;

import net.mcreator.halo_mde.HaloMdeMod;
import net.mcreator.halo_mde.procedures.ALLTeamProcedure;
import net.mcreator.halo_mde.procedures.ChangeCreaturesProcedureProcedure;
import net.mcreator.halo_mde.procedures.ResizeCreaturesTagProcedure;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(ChangeCreaturesProcedureProcedure.class)
public class MixinChangeCreatureProcedureProcedure {


    public MixinChangeCreatureProcedureProcedure() {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap=false)
    public static void onEntityJoin(EntityJoinLevelEvent event) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap=false)
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap=false)
    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {

    }

}
