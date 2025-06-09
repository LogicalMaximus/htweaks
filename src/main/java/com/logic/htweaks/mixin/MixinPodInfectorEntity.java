package com.logic.htweaks.mixin;

import net.asestefan.utils.SoundUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.teamabyssalofficial.entity.custom.PodInfectorEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoEntity;

@Mixin(PodInfectorEntity.class)
public abstract class MixinPodInfectorEntity extends Monster implements GeoEntity, SoundUtils {

    protected MixinPodInfectorEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "tick", at= @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if(this.getVehicle() instanceof PodInfectorEntity) {
            this.stopRiding();
        }

        if(this.getPassengers().stream().anyMatch((e) -> e instanceof PodInfectorEntity)) {
            this.ejectPassengers();
        }
    }

    @Override
    public boolean startRiding(Entity entity, boolean force) {
        if(entity instanceof PodInfectorEntity) {
            return false;
        }
        else {
            return super.startRiding(entity, force);
        }
    }

    @Inject(method = "grabTarget", at= @At("HEAD"), cancellable = true, remap = false)
    public void grabTarget(LivingEntity entity, CallbackInfo ci) {
        if(entity instanceof PodInfectorEntity) {
            ci.cancel();
        }
    }

}
