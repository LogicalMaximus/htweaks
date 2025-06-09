package com.logic.htweaks.entity.navigation;

import com.atsuishio.superbwarfare.entity.vehicle.Ah6Entity;
import com.atsuishio.superbwarfare.entity.vehicle.base.HelicopterEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.MobileVehicleEntity;
import com.atsuishio.superbwarfare.entity.vehicle.base.VehicleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class TaczMoveControl extends MoveControl {
    public TaczMoveControl(Mob p_24983_) {
        super(p_24983_);
    }

    public void tick() {
        if (this.operation == MoveControl.Operation.STRAFE) {
            float f = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float f1 = (float)this.speedModifier * f;
            float f2 = this.strafeForwards;
            float f3 = this.strafeRight;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 *= f4;
            f3 *= f4;
            float f5 = Mth.sin(this.mob.getYRot() * ((float)Math.PI / 180F));
            float f6 = Mth.cos(this.mob.getYRot() * ((float)Math.PI / 180F));
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            if (!this.isWalkable(f7, f8)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.mob.setSpeed(f1);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        } else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - this.mob.getY();
            double d3 = d0 * d0 + d2 * d2 + d1 * d1;
            if (d3 < (double)2.5000003E-7F) {
                this.mob.setZza(0.0F);
                return;
            }

            float f9 = (float)(Mth.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f9, 90.0F));
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));

            if(this.mob.getVehicle() instanceof MobileVehicleEntity vehicle) {
                if(vehicle.getFirstPassenger() == this.mob) {
                    float rot = this.rotlerp(this.mob.getYRot(), f9, 6.0F);

                    float angle = (float) VehicleEntity.calculateAngle(vehicle.getDeltaMovement(), vehicle.getViewVector(1.0F));
                    double s0;
                    if (Mth.abs(angle) < 90.0F) {
                        s0 = vehicle.getDeltaMovement().horizontalDistance();
                    } else {
                        s0 = -vehicle.getDeltaMovement().horizontalDistance();
                    }

                    vehicle.setLeftWheelRot((float)((double)vehicle.getLeftWheelRot() - (double)1.25F * s0 + (double)Mth.clamp(0.75F * rot - vehicle.getLeftWheelRot(), -5.0F, 5.0F)));
                    vehicle.setRightWheelRot((float)((double)vehicle.getRightWheelRot() - (double)1.25F * s0 - (double)Mth.clamp(0.75F * rot - vehicle.getRightWheelRot(), -5.0F, 5.0F)));
                    vehicle.setLeftTrack((float)((double)vehicle.getLeftTrack() - 5.969026041820607 * s0 + Mth.clamp(1.2566370801612687 * rot - vehicle.getLeftTrack(), (double)-5.0F, (double)5.0F)));
                    vehicle.setRightTrack((float)((double)vehicle.getRightTrack() - 5.969026041820607 * s0 - Mth.clamp(1.2566370801612687 * rot - vehicle.getRightTrack(), (double)-5.0F, (double)5.0F)));

                    vehicle.setYRot(rot);

                    if(Mth.degreesDifference(rot, f9) <= 6.0F) {
                        vehicle.setDeltaMovement(vehicle.getDeltaMovement().add(vehicle.getViewVector(1.0F).scale((double)((!vehicle.isInWater() && !vehicle.onGround() ? 0.13F : (vehicle.isInWater() && !vehicle.onGround() ? 2.0F : 2.4F)) * (Float)0.20F))));
                    }
                }
            }

            BlockPos blockpos = this.mob.blockPosition();
            BlockState blockstate = this.mob.level().getBlockState(blockpos);
            VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level(), blockpos);
            if (d2 > (double)this.mob.getStepHeight() && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
                this.mob.getJumpControl().jump();
                this.operation = MoveControl.Operation.JUMPING;
            }
        } else if (this.operation == MoveControl.Operation.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.onGround()) {
                this.operation = MoveControl.Operation.WAIT;
            }
        } else {
            this.mob.setZza(0.0F);
        }

    }

    private boolean isWalkable(float p_24997_, float p_24998_) {
        PathNavigation pathnavigation = this.mob.getNavigation();
        if (pathnavigation != null) {
            NodeEvaluator nodeevaluator = pathnavigation.getNodeEvaluator();
            if (nodeevaluator != null && nodeevaluator.getBlockPathType(this.mob.level(), Mth.floor(this.mob.getX() + (double)p_24997_), this.mob.getBlockY(), Mth.floor(this.mob.getZ() + (double)p_24998_)) != BlockPathTypes.WALKABLE) {
                return false;
            }
        }

        return true;
    }
}
