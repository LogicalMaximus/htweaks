package com.logic.htweaks.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Random;

public class HtweaksUtils {

    private static Random random = new Random();

    @Nullable
    public static BlockPos func_221244_a(BlockPos p_221244_1_, int spread, ServerLevel world) {
        BlockPos blockpos = null;

        for(int i = 0; i < 10; ++i) {
            int j = p_221244_1_.getX() + random.nextInt(spread * 2) - spread;
            int k = p_221244_1_.getZ() + random.nextInt(spread * 2) - spread;
            int l = world.getHeight(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);

            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, world, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }

        return blockpos;
    }

    public static boolean func_226559_a_(BlockPos p_226559_1_, ServerLevel world) {
        Iterator var2 = BlockPos.betweenClosed(p_226559_1_, p_226559_1_.offset(1, 2, 1)).iterator();

        BlockPos blockpos;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            blockpos = (BlockPos)var2.next();
        } while(world.getBlockState(blockpos).getBlockSupportShape(world, blockpos).isEmpty() && world.getFluidState(blockpos).isEmpty());

        return false;
    }

    public static void spawnHighAngleMortarProjectileWithDirection(Level level, BlockPos hitBlockPos, double angle, double radius, double distance, double speed, EntityType<?> entityType, String direction) {
        if (direction == null) {
            direction = "North";
        }

        double spawnX = (double)hitBlockPos.getX() + Math.cos(angle) * radius;
        double spawnZ = (double)hitBlockPos.getZ() + Math.sin(angle) * radius;
        double spawnY = (double)(hitBlockPos.getY() + 350);
        double targetX = (double)hitBlockPos.getX() + Math.cos(angle) * radius;
        double targetZ = (double)hitBlockPos.getZ() + Math.sin(angle) * radius;
        double targetY = (double)hitBlockPos.getY();
        switch (direction) {
            case "North":
                spawnZ -= 127.0 + distance;
                targetZ += 15.0;
                break;
            case "North-East":
                spawnX += 90.0 + distance;
                spawnZ -= 90.0 + distance;
                targetX -= 10.0;
                targetZ += 10.0;
                break;
            case "East":
                spawnX += 127.0 + distance;
                targetX -= 15.0;
                break;
            case "South-East":
                spawnX += 90.0 + distance;
                spawnZ += 90.0 + distance;
                targetX -= 10.0;
                targetZ -= 10.0;
                break;
            case "South":
                spawnZ += 127.0 + distance;
                targetZ -= 15.0;
                break;
            case "South-West":
                spawnX -= 90.0 + distance;
                spawnZ += 90.0 + distance;
                targetX += 10.0;
                targetZ -= 10.0;
                break;
            case "West":
                spawnX -= 127.0 + distance;
                targetX += 15.0;
                break;
            case "North-West":
                spawnX -= 90.0 + distance;
                spawnZ -= 90.0 + distance;
                targetX += 10.0;
                targetZ += 10.0;
        }

        double dx = targetX - spawnX;
        double dy = targetY - spawnY;
        double dz = targetZ - spawnZ;
        double sqrt_distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double velocityX = dx / sqrt_distance * speed;
        double velocityY = dy / sqrt_distance * speed;
        double velocityZ = dz / sqrt_distance * speed;
        ThrowableItemProjectile mortar = (ThrowableItemProjectile)entityType.create(level);
        if (mortar != null) {
            mortar.setPos(spawnX, spawnY, spawnZ);
            mortar.setDeltaMovement(velocityX, velocityY, velocityZ);
            level.addFreshEntity(mortar);
        }

    }

    public static double getAngleDistanceModifier(double distance, int x, int random) {
        double modifier = distance / (double)x;
        return (modifier - (double)HtweaksUtils.random.nextInt(-random, random)) / 100.0;
    }

    public static float getForceDistanceModifier(double distance, double base) {
        double modifier = 0.0;
        if (distance > 4000.0) {
            modifier = base * 0.09;
        } else if (distance > 3750.0) {
            modifier = base * 0.075;
        } else if (distance > 3500.0) {
            modifier = base * 0.055;
        } else if (distance > 3000.0) {
            modifier = base * 0.03;
        } else if (distance > 2500.0) {
            modifier = base * 0.01;
        }

        return (float)modifier;
    }

    public static double getAngleHeightModifier(double distance, double heightDiff, double modifier) {
        if (distance >= 2000.0) {
            return heightDiff * 1.15 * modifier;
        } else if (distance >= 1750.0) {
            return heightDiff * 1.05 * modifier;
        } else if (distance >= 1500.0) {
            return heightDiff * 0.6 * modifier;
        } else if (distance >= 1250.0) {
            return heightDiff * 0.5 * modifier;
        } else if (distance >= 1000.0) {
            return heightDiff * 0.4 * modifier;
        } else if (distance >= 750.0) {
            return heightDiff * 0.3 * modifier;
        } else {
            return distance >= 500.0 ? heightDiff * 0.2 * modifier : 0.0;
        }
    }
}
