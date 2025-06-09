package com.logic.htweaks.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.NotNull;

public class TaczAirPathNavigation extends FlyingPathNavigation {

    public TaczAirPathNavigation(Mob p_26424_, Level p_26425_) {
        super(p_26424_, p_26425_);
    }

    protected @NotNull PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new TaczAirNodeEvaluator();
        this.nodeEvaluator.setCanOpenDoors(false);
        this.nodeEvaluator.setCanPassDoors(false);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, range);
    }
}
