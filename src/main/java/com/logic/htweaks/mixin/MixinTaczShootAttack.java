package com.logic.htweaks.mixin;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.corrinedev.tacznpcs.common.entity.behavior.TaczShootAttack;
import com.logic.htweaks.registries.ModAttributes;
import com.logic.htweaks.utils.TargetUtils;
import com.tacz.guns.api.entity.ShootResult;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Random;

@Mixin(TaczShootAttack.class)
public abstract class MixinTaczShootAttack <E extends AbstractScavEntity> extends ExtendedBehaviour<E> {

    @Shadow(remap = false)
    protected @Nullable LivingEntity target;

    /**
     * @author LogicalMaximus
     * @reason Adds Accuracy System
     */
    @Overwrite(remap=false)
    protected void start(E entity) {
        if (this.target != null && entity.getTarget() != null && BehaviorUtils.entityIsVisible(entity.getBrain(), entity.getTarget())) {
            Entity targetVehicle = this.target.getVehicle();

            if(targetVehicle == null) {
                BehaviorUtils.lookAtEntity(entity, target);
            }
            else {
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, targetVehicle.getEyePosition(1.0F));
            }

            entity.aim(true);
            ShootResult result = entity.shoot(() -> {
                    return entity.getViewXRot((float) Math.random());
                }, () -> {
                    return entity.getViewYRot((float) Math.random());
                });
                if (result == ShootResult.SUCCESS) {
                    entity.firing = true;
                    ++entity.collectiveShots;
                    entity.rangedCooldown = entity.getStateRangedCooldown();
                } else if (result == ShootResult.NEED_BOLT) {
                    entity.bolt();
                }
        }

    }

}
