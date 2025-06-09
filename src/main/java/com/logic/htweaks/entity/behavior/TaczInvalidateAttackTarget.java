package com.logic.htweaks.entity.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.apache.commons.lang3.function.ToBooleanBiFunction;

public class TaczInvalidateAttackTarget<E extends LivingEntity> extends InvalidateAttackTarget<E> {

    protected ToBooleanBiFunction<E, LivingEntity> predicate = (entity, target) -> {
        boolean var10000;
        label21: {
            if (target instanceof Player player) {
                if (player.getAbilities().invulnerable) {
                    break label21;
                }
            }
        }

        var10000 = true;
        return var10000;
    };

    @Override
    protected void start(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if (target != null) {
            if (this.isTargetInvalid(entity, target) || !this.canAttack(entity, target) || this.predicate.applyAsBoolean(entity, target)) {
                BrainUtils.clearMemory(entity, MemoryModuleType.ATTACK_TARGET);
            }

        }
    }

}
