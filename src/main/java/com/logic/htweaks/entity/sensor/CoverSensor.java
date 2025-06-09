package com.logic.htweaks.entity.sensor;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.registries.ModSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CoverSensor<E extends AbstractScavEntity> extends PredicateSensor<BlockPos, E> {
    private static final List<MemoryModuleType<?>> MEMORIES;
    protected @Nullable SquareRadius radius = null;

    public CoverSensor() {
        super((pos, entity) -> {
            return pos != entity.blockPosition();
        });
    }

    public CoverSensor<E> setRadius(double radius) {
        return this.setRadius(radius, radius);
    }

    public CoverSensor<E> setRadius(double xz, double y) {
        this.radius = new SquareRadius(xz, y);
        return this;
    }

    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    public SensorType<? extends ExtendedSensor<?>> type() {
        return (SensorType) ModSensors.NEARBY_COVER_SENSOR.get();
    }

    protected void doTick(ServerLevel level, E entity) {
        SquareRadius radius = this.radius;
        if (radius == null) {
            double dist = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
            radius = new SquareRadius(dist, dist);
        }

        List<BlockPos> positions = getPotentialPositions(entity);
        positions.sort(Comparator.comparingDouble((position) -> {
            return entity.distanceToSqr(position.getCenter());
        }));

        BrainUtils.setMemory(entity, ModMemoryTypes.NEARBY_COVER_POSITIONS.get(), positions);
    }

    public List<BlockPos> getPotentialPositions(E entity) {
        List<BlockPos> coverPositions = new ArrayList<>();
        BlockPos entityPos = entity.blockPosition();
        Level world = entity.level();

        SquareRadius scanRadius;

        if(radius == null) {
            double dist = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
            scanRadius = new SquareRadius(dist, dist);
        }
        else {
            scanRadius = radius;
        }


        for (int x = (int) -scanRadius.xzRadius(); x <= scanRadius.xzRadius(); x++) {
            for (int z = (int) -scanRadius.xzRadius(); z <= scanRadius.xzRadius(); z++) {
                for (int y = (int) -scanRadius.yRadius(); y <= scanRadius.yRadius(); y++) { // Check around eye height
                    BlockPos blockPos = entityPos.offset(x, y, z);
                    BlockState blockState = world.getBlockState(blockPos);

                    if (blockState.isSolid()) {
                        // Find a walkable spot behind the block
                        BlockPos coverPos = findWalkableCover(blockPos, entity);

                        if (coverPos != null) {
                            coverPositions.add(coverPos);
                        }
                    }
                }
            }
        }
        return coverPositions;
    }

    private BlockPos findWalkableCover(BlockPos blockPos, E entity) {
        Level world = entity.level();
        // Check if the space behind the block is walkable
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos checkPos = blockPos.relative(dir);
            if (world.getBlockState(checkPos).isAir() && world.getBlockState(checkPos.above()).isAir()) {
                LivingEntity target = BrainUtils.getTargetOfEntity(entity);

                if(target != null) {
                    if(!hasLineOfSight(target, new Vec3(entity.getX(), entity.getEyeY(), entity.getZ()))) {
                        return checkPos;
                    }
                } else {

                }
            }
        }

        return null;
    }

    public static boolean hasLineOfSight(LivingEntity entity, Vec3 vec31) {
        Vec3 vec3 = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        if (vec31.distanceTo(vec3) > (double)64.0F) {
            return false;
        } else {
            return entity.level().clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
        }
    }

    static {
        MEMORIES = ObjectArrayList.of(new MemoryModuleType[]{ModMemoryTypes.NEARBY_COVER_POSITIONS.get()});
    }
}
