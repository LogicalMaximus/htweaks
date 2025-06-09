package com.logic.htweaks.commander.ai.htn.actions.artillery.multi;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.genzyuro.artillerysupport.MortarUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public abstract class AbstractMultiTargetFireSupportAction extends AbstractAction {
    protected final Random random = new Random();

    private boolean isFinished;

    private List<BlockPos> hitBlockPos;

    private BlockPos currentTargetPos;

    private int ticks;

    private int shots;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    private List<BlockPos> orderBlockPos;

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        hitBlockPos = commander.getMemory(ModMemoryTypes.ARTILLERY_STRIKE_POSITIONS.get()).orElse(null);

        if(orderBlockPos != null) {
            hitBlockPos = orderBlockPos;
        }
        else if(hitBlockPos != null) {
            List<ChunkPos> ownedObjectives = new ArrayList<>(commander.getOwnedObjectives());
            if(!ownedObjectives.isEmpty()) {
                ChunkPos targetChunkPos = new ChunkPos(hitBlockPos.stream().findFirst().get());

                ownedObjectives.sort(Comparator.comparingDouble((e) -> e.getChessboardDistance(targetChunkPos)));

                ChunkPos closestChunk = ownedObjectives.get(0);
                if(closestChunk.getChessboardDistance(targetChunkPos) <= this.getMaxDistance()) {
                    return true;
                }
            }
        }



        return false;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {

    }

    @Override
    public void tick(MiltaryAiCommander commander, ServerLevel level) {
        if(hitBlockPos == null) {
            this.isFinished = true;

            return;
        }

        if(ticks % this.getInterval() == 0 && shots < this.getShotAmount()) {
            Optional<BlockPos> optionalBlockPos = hitBlockPos.stream().findFirst();

            if(optionalBlockPos.isPresent()) {
                currentTargetPos = optionalBlockPos.get();

                hitBlockPos.remove(currentTargetPos);
            }
            else {
                currentTargetPos = null;
            }

            if (this.currentTargetPos != null) {
                double angle = this.random.nextDouble() * Math.PI * (double)2.0F;
                double radius = this.random.nextDouble() * (double)30.0F;
                MortarUtils.spawnMortarProjectileWithDirection(level, this.currentTargetPos, angle, radius, (double)8.0F, (EntityType)this.getProjectile(), "North");
            }
            else {
                this.isFinished = true;
            }

            shots++;
        }
        else if(shots > this.getShotAmount()) {
            this.isFinished = true;
        }

        ticks++;
    }

    abstract int getMaxDistance();

    abstract int getInterval();

    abstract int getShotAmount();

    abstract EntityType<? extends ThrowableItemProjectile> getProjectile();

    @Override
    public boolean getIsFinished() {
        return isFinished;
    }

    @Override
    public void stop(MiltaryAiCommander commander, ServerLevel level) {
        ticks = 0;
        hitBlockPos = null;
        currentTargetPos = null;
        shots = 0;
        this.isFinished = false;
    }

    public void setHitBlockPos(List<BlockPos> orderBlockPos) {
        this.orderBlockPos = orderBlockPos;
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of();
    }
}
