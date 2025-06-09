package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IAllyChecker;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.Projectile;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class TaczTargetShooter<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        List<Projectile> projectiles = BrainUtils.getMemory(entity, SBLMemoryTypes.INCOMING_PROJECTILES.get());

        for(Projectile projectile : projectiles) {
            if(!((IAllyChecker)entity).isAlly(projectile.getOwner())) {
                Entity target = projectile.getOwner();

                if(target instanceof LivingEntity livingTarget) {
                    BrainUtils.setTargetOfEntity(entity, livingTarget);

                    break;
                }
            }
        }

    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(SBLMemoryTypes.INCOMING_PROJECTILES.get(), MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)});
    }
}
