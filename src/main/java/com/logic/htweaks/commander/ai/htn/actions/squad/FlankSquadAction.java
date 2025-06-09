package com.logic.htweaks.commander.ai.htn.actions.squad;

import com.logic.htweaks.commander.MiltaryAiCommander;
import com.logic.htweaks.commander.ai.htn.actions.AbstractAction;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class FlankSquadAction extends AbstractAction {
    private boolean isFinished = false;

    private static final Random random = new Random();

    private Vec3 calculatePosition(Vec3 origin, double radius, double degrees, ServerLevel level) {

        double x = origin.x + radius + Math.cos(Math.toRadians(degrees));
        double z = origin.z + radius + Math.sin(Math.toRadians(degrees));

        return new Vec3(x, level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, (int) x, (int) z), z);
    }

    @Override
    public boolean canExecute(MiltaryAiCommander commander) {
        return true;
    }

    @Override
    public void start(MiltaryAiCommander commander, ServerLevel level) {
        Patrol patrol = commander.getMemory(ModMemoryTypes.CURRENT_CONTROL_SQUAD.get()).get();
        BlockPos flankAroundPos = commander.getMemory(ModMemoryTypes.TARGET_FLANK_POS.get()).get();

        if(patrol != null && flankAroundPos != null) {
            double degrees;

            if(0.5 < random.nextDouble()) {
                degrees = 90;
            }
            else {
                degrees = -90;
            }

            Vec3 flankPos = calculatePosition(flankAroundPos.getCenter(), 50, degrees, level);

            patrol.setWaypoint(new Waypoint(new BlockPos((int) flankPos.x, (int) flankPos.y, (int) flankPos.z), 5));
        }

        this.isFinished = true;
    }

    @Override
    public void stop(MiltaryAiCommander commander, ServerLevel level) {

        this.isFinished = false;
    }

    @Override
    public void tick(MiltaryAiCommander commander, ServerLevel level) {

    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public double requiredResources() {
        return 0;
    }

    @Override
    public boolean getIsFinished() {
        return isFinished;
    }

    @Override
    public List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(ModMemoryTypes.CURRENT_CONTROL_SQUAD.get(), MemoryStatus.VALUE_PRESENT), Pair.of(ModMemoryTypes.TARGET_FLANK_POS.get(), MemoryStatus.VALUE_PRESENT));
    }
}
