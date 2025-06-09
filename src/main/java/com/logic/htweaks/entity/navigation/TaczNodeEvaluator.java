package com.logic.htweaks.entity.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TaczNodeEvaluator extends WalkNodeEvaluator {
    public Node getStart() {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        int i = this.mob.getBlockY();
        BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), (double)i, this.mob.getZ()));
        if (!this.mob.canStandOnFluid(blockstate.getFluidState())) {
            if (this.canFloat() && this.mob.isInWater()) {
                while(true) {
                    if (!blockstate.is(Blocks.WATER) && blockstate.getFluidState() != Fluids.WATER.getSource(false)) {
                        --i;
                        break;
                    }

                    ++i;
                    blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), (double)i, this.mob.getZ()));
                }
            } else if (this.mob.onGround()) {
                i = Mth.floor(this.mob.getY() + (double)0.5F);
            } else {
                BlockPos blockpos;
                for(blockpos = this.mob.blockPosition(); (this.level.getBlockState(blockpos).isAir() || this.level.getBlockState(blockpos).isPathfindable(this.level, blockpos, PathComputationType.LAND)) && blockpos.getY() > this.mob.level().getMinBuildHeight(); blockpos = blockpos.below()) {
                }

                i = blockpos.above().getY();
            }
        } else {
            while(this.mob.canStandOnFluid(blockstate.getFluidState())) {
                ++i;
                blockstate = this.level.getBlockState(blockpos$mutableblockpos.set(this.mob.getX(), (double)i, this.mob.getZ()));
            }

            --i;
        }

        BlockPos blockpos1 = this.mob.blockPosition();
        if (!this.canStartAt(blockpos$mutableblockpos.set(blockpos1.getX(), i, blockpos1.getZ()))) {
            AABB aabb;

            Entity v = this.mob.getVehicle();

            if(v != null) {
                aabb = v.getBoundingBox();
            } else {
                aabb = this.mob.getBoundingBox();
            }

            if (this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, (double)i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.minX, (double)i, aabb.maxZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, (double)i, aabb.minZ)) || this.canStartAt(blockpos$mutableblockpos.set(aabb.maxX, (double)i, aabb.maxZ))) {
                return this.getStartNode(blockpos$mutableblockpos);
            }
        }

        return this.getStartNode(new BlockPos(blockpos1.getX(), i, blockpos1.getZ()));
    }


    private boolean canReachWithoutCollision(Node p_77625_) {
        AABB aabb;

        Entity v = this.mob.getVehicle();

        if(v != null) {
            aabb = v.getBoundingBox();
        } else {
            aabb = this.mob.getBoundingBox();
        }

        Vec3 vec3 = new Vec3((double)p_77625_.x - this.mob.getX() + aabb.getXsize() / (double)2.0F, (double)p_77625_.y - this.mob.getY() + aabb.getYsize() / (double)2.0F, (double)p_77625_.z - this.mob.getZ() + aabb.getZsize() / (double)2.0F);
        int i = Mth.ceil(vec3.length() / aabb.getSize());
        vec3 = vec3.scale((double)(1.0F / (float)i));

        for(int j = 1; j <= i; ++j) {
            aabb = aabb.move(vec3);
            if (this.hasCollisions(aabb)) {
                return false;
            }
        }

        return true;
    }
}
