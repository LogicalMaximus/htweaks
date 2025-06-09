package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.block.ShipBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    public static final RegistryObject<Block> SHIP_BLOCK;

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Htweaks.MODID);

        SHIP_BLOCK = BLOCKS.register("ship_ballooon", ShipBlock::new);
    }

}
