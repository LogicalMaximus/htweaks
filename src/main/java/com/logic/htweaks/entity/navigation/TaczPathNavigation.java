package com.logic.htweaks.entity.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.NotNull;

public class TaczPathNavigation extends GroundPathNavigation {


    public TaczPathNavigation(Mob p_26448_, Level p_26449_) {
        super(p_26448_, p_26449_);
    }

    protected @NotNull PathFinder createPathFinder(int range) {
        this.nodeEvaluator = new TaczNodeEvaluator();
        this.nodeEvaluator.setCanOpenDoors(true);
        this.nodeEvaluator.setCanPassDoors(true);
        this.nodeEvaluator.setCanFloat(true);
        return new PathFinder(this.nodeEvaluator, range);
    }

}
