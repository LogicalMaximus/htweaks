package com.logic.htweaks.entity.sensor;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.registries.ModSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;

public class FlankSensor<E extends AbstractScavEntity> extends PredicateSensor<BlockPos, E> {
    private static final List<MemoryModuleType<?>> MEMORIES;

    protected void doTick(ServerLevel level, E entity) {
       LivingEntity target = BrainUtils.getTargetOfEntity(entity);

       if(target != null) {
           List<Vec3> possiblePositions = new ArrayList<>();

           List<BlockPos> positions = new ArrayList<>();

           double radius = entity.distanceToSqr(target);

           Vec3 targetPos = target.getEyePosition();

           possiblePositions.add(calculatePosition(targetPos, radius,22.5, level));
           possiblePositions.add(calculatePosition(targetPos, radius,45, level));
           possiblePositions.add(calculatePosition(targetPos, radius,60, level));
           possiblePositions.add(calculatePosition(targetPos, radius,75, level));
           possiblePositions.add(calculatePosition(targetPos, radius,90, level));

           possiblePositions.add(calculatePosition(targetPos, radius,-22.5, level));
           possiblePositions.add(calculatePosition(targetPos, radius,-45, level));
           possiblePositions.add(calculatePosition(targetPos, radius,-60, level));
           possiblePositions.add(calculatePosition(targetPos, radius,-75, level));
           possiblePositions.add(calculatePosition(targetPos, radius,-90, level));

           for(Vec3 pos : possiblePositions) {
               if(canPosSeeTarget(pos, entity, target)) {
                   positions.add(new BlockPos((int) pos.x, (int) pos.y, (int) pos.z));
               }
           }

           BrainUtils.setMemory(entity, ModMemoryTypes.FLANK_POS.get(), positions);
       }
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return ModSensors.FLANK_SENSOR.get();
    }

    private boolean canPosSeeTarget(Vec3 checkPos, E entity, LivingEntity target) {
        Vec3 targetPos = target.getEyePosition();
        Vec3 originPos = checkPos.add(0,entity.getEyeHeight(), 0);

        return hasLineOfSight(originPos, entity, targetPos);
    }

    private static boolean hasLineOfSight(Vec3 origin, LivingEntity entity, Vec3 vec31) {
        if (vec31.distanceTo(origin) > (double)160.0F) {
            return false;
        } else {
            return entity.level().clip(new ClipContext(origin, vec31, ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
        }
    }

    private Vec3 calculatePosition(Vec3 origin, double radius, double degrees, ServerLevel level) {

        double x = origin.x + radius + Math.cos(Math.toRadians(degrees));
        double z = origin.z + radius + Math.sin(Math.toRadians(degrees));

        return new Vec3(x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) x, (int) z), z);
    }

    static {
        MEMORIES = ObjectArrayList.of(new MemoryModuleType[]{ModMemoryTypes.FLANK_POS.get()});
    }
}
