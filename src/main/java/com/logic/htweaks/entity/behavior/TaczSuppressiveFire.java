package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.utils.TargetUtils;
import com.mojang.datafixers.util.Pair;
import com.tacz.guns.api.entity.ShootResult;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaczSuppressiveFire <E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    private @Nullable LivingEntity target;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        if(BrainUtils.getTargetOfEntity(entity) != null) {
            target = BrainUtils.getTargetOfEntity(entity);
        }

        return !BrainUtils.canSee(entity, this.target);
    }

    protected void start(E entity) {
        if (this.target != null) {

            entity.lookAt(EntityAnchorArgument.Anchor.EYES, this.target.getPosition(0.75F));

            BehaviorUtils.lookAtEntity(entity, this.target);
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

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)});
    }
}
