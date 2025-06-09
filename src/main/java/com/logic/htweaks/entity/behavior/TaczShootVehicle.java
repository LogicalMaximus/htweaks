package com.logic.htweaks.entity.behavior;

import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.entity.IShootableVehicle;
import com.mojang.datafixers.util.Pair;
import com.tacz.guns.api.entity.ShootResult;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TaczShootVehicle <E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    private @Nullable LivingEntity target;

    private IShootableVehicle vehicle;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        target = BrainUtils.getTargetOfEntity(entity);

        if(target != null && entity.getVehicle() instanceof IShootableVehicle vehicle) {
            this.vehicle = vehicle;

            if(entity.getVehicle().getControllingPassenger() == entity || entity.getVehicle() instanceof VehicleEntity vehicleEntity && vehicleEntity.getFirstPassenger() == entity) {
                return true;
            }
        }

        return false;
    }

    protected void tick(E entity) {
        if (this.target != null && vehicle != null) {
            entity.setDeltaMovement(0,0,0);

            entity.getNavigation().stop();

            Entity targetVehicle = this.target.getVehicle();

            if(targetVehicle == null) {
                BehaviorUtils.lookAtEntity(entity, target);
            }
            else {
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, targetVehicle.getEyePosition(1.0F));
            }

            entity.getNavigation().stop();

            vehicle.htweaks$entityShoot(entity);
        }
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)});
    }
}
