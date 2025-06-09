package com.logic.htweaks.commander.ai.htn.actions.artillery.single;

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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public abstract class AbstractFireSupportAction extends AbstractAction {
    protected final Random random = new Random();

    private boolean isFinished;

    private BlockPos hitBlockPos;

    private BlockPos orderBlockPos;

    private int ticks;

    private int shots;

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS;

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        hitBlockPos = commander.getMemory(ModMemoryTypes.ARTILLERY_STRIKE_POS.get()).orElse(null);

        if(orderBlockPos != null) {
            hitBlockPos = orderBlockPos;
        }
        else if(hitBlockPos != null) {
            List<ChunkPos> ownedObjectives = new ArrayList<>(commander.getOwnedObjectives());
            if(!ownedObjectives.isEmpty()) {
                ChunkPos targetChunkPos = new ChunkPos(hitBlockPos);

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
        if(ticks % this.getInterval() == 0 && shots < this.getShotAmount()) {
            if (this.hitBlockPos != null) {
                double angle = this.random.nextDouble() * Math.PI * (double)4.0F;
                double radius = this.random.nextDouble() * (double)40.0F;
                MortarUtils.spawnMortarProjectileWithDirection(level, this.hitBlockPos, angle, radius, (double)10.0F, (EntityType)this.getProjectile(), "North");
            }

            shots++;
        }
        if (shots >= this.getShotAmount()) {
            this.isFinished = true;
        }

        ticks++;
    }

    public void setHitBlockPos(BlockPos orderBlockPos) {
        this.orderBlockPos = orderBlockPos;
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
        orderBlockPos = null;
        shots = 0;
        this.isFinished = false;
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    static {
        MEMORY_REQUIREMENTS = ObjectArrayList.of();
    }
}
