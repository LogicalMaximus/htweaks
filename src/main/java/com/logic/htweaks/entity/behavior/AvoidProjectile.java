package com.logic.htweaks.entity.behavior;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class AvoidProjectile<E extends AbstractScavEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;
    protected Predicate<Entity> avoidingPredicate = (target) -> target instanceof Projectile;
    protected float noCloserThanSqr = 6.0F;
    protected float stopAvoidingAfterSqr = 20.0F;
    protected float speedModifier = 1.33F;
    private Path runPath = null;

    public AvoidProjectile() {
        this.noTimeout();
    }

    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public AvoidProjectile<E> noCloserThan(float blocks) {
        this.noCloserThanSqr = blocks * blocks;
        return this;
    }

    public AvoidProjectile<E> stopCaringAfter(float blocks) {
        this.stopAvoidingAfterSqr = blocks * blocks;
        return this;
    }

    public AvoidProjectile<E> avoiding(Predicate<Entity> predicate) {
        this.avoidingPredicate = predicate;
        return this;
    }

    public AvoidProjectile<E> speedModifier(float mod) {
        this.speedModifier = mod;
        return this;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        List<Projectile> projectiles = (BrainUtils.getMemory(entity, SBLMemoryTypes.INCOMING_PROJECTILES.get()));

        projectiles.sort(Comparator.comparingDouble(entity::distanceToSqr));

        Projectile target = projectiles.get(0);

        if (target == null) {
            return false;
        } else {
            double distToTarget = target.distanceToSqr(entity);
            if (distToTarget > (double)this.noCloserThanSqr) {
                return false;
            } else {
                Vec3 runPos = DefaultRandomPos.getPosAway(entity, 16, 7, target.position());
                if (runPos != null && !(target.distanceToSqr(runPos.x, runPos.y, runPos.z) < distToTarget)) {
                    this.runPath = entity.getNavigation().createPath(runPos.x, runPos.y, runPos.z, 0);
                    return this.runPath != null;
                } else {
                    return false;
                }
            }
        }
    }

    protected boolean shouldKeepRunning(E entity) {
        return !this.runPath.isDone();
    }

    protected void start(E entity) {
        entity.getNavigation().moveTo(this.runPath, (double)this.speedModifier);
    }

    protected void stop(E entity) {
        this.runPath = null;
        entity.getNavigation().setSpeedModifier((double)1.0F);
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of(new Pair[]{Pair.of(SBLMemoryTypes.INCOMING_PROJECTILES.get(), MemoryStatus.VALUE_PRESENT)});
    }
}
