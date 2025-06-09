package com.logic.htweaks.waypoints;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;

public class Waypoint {
    private final BlockPos pos;

    private final int radius;

    private final AABB aabb;

    public Waypoint(BlockPos pos, int radius) {
        this.pos = pos;
        this.radius = radius;
        this.aabb = this.createAABBWaypoint();
    }

    public Waypoint(CompoundTag tag) {
        this.pos = new BlockPos(tag.getInt("PosX"),tag.getInt("PosY"),tag.getInt("PosZ"));
        this.radius = tag.getInt("radius");
        this.aabb = this.createAABBWaypoint();
    }

    public AABB createAABBWaypoint() {
        BlockPos startPos = new BlockPos(pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius);
        BlockPos endPos =new BlockPos(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius);

        return new AABB(startPos, endPos);
    }

    public int getRadius() {
        return radius;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void save(CompoundTag tag) {
        tag.putInt("radius", this.radius);

        tag.putInt("PosX", this.pos.getX());
        tag.putInt("PosY", this.pos.getY());
        tag.putInt("PosZ", this.pos.getZ());
    }

    public AABB getAabb() {
        return aabb;
    }
}
