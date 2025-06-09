package com.logic.htweaks.entity.vehicle.projectile;

import com.logic.htweaks.registries.ModEntities;
import net.genzyuro.artillerysupport.entity.explosive.LightMortarProjectileEntity;
import net.genzyuro.artillerysupport.event.ChunkLoader;
import net.genzyuro.artillerysupport.event.ChunkUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;
import java.util.List;

public class DelayedProjectileEntity<T extends ThrowableItemProjectile> extends Projectile {

    T projectile;

    private int delayTime;

    public DelayedProjectileEntity(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
        super(p_37248_, p_37249_);
    }

    /*
    public DelayedProjectileEntity(T projectile, Level level, int delayTime) {
        super(ModEntities.DELAYED_PROJECTILE.get(), level);

        this.projectile = projectile;
        this.delayTime = delayTime;
    }
    */


    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide) {
            Level var2 = this.level();
            if (var2 instanceof ServerLevel) {
                ServerLevel serverLevel = (ServerLevel)var2;
                ChunkLoader chunkLoader = ChunkLoader.get(serverLevel);
                ChunkPos currentChunk = new ChunkPos(this.blockPosition());
                List<ChunkPos> surroundingChunks = ChunkUtils.getSurroundingChunks(currentChunk, 2);
                Iterator var5 = surroundingChunks.iterator();

                while(var5.hasNext()) {
                    ChunkPos chunk = (ChunkPos)var5.next();
                    chunkLoader.forceLoadChunk(serverLevel, chunk);
                }

                if(delayTime <= 0) {
                    if(this.projectile != null) {
                        this.level().addFreshEntity(projectile);
                        this.discard();
                    }
                }
                else {
                    delayTime--;
                }
            }
        }
    }

    @Override
    public void setPos(double x, double y, double z) {
        if(projectile != null) {
            projectile.setPos(x, y, z);
        }

        super.setPos(x,y,z);
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
        if(projectile != null) {
            projectile.setDeltaMovement(x, y, z);
        }

        super.setDeltaMovement(x, y, z);
    }

    @Override
    public void setDeltaMovement(Vec3 pos) {
        if(projectile != null) {
            projectile.setDeltaMovement(pos);
        }

        super.setDeltaMovement(pos);
    }

    @Override
    protected void defineSynchedData() {

    }
}
