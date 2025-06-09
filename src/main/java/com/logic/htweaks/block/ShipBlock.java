package com.logic.htweaks.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class ShipBlock extends Block {
    public ShipBlock() {
        super(Properties.of().instabreak());
    }
}
