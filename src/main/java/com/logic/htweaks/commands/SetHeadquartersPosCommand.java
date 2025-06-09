package com.logic.htweaks.commands;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.config.HTServerConfig;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class SetHeadquartersPosCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)
                        Commands.literal("setheadquarters")
                                .requires((sourceStack) -> sourceStack
                                        .hasPermission(2)))
                        .executes((context) -> setHeadquarters((CommandSourceStack)context.getSource(), BlockPos.containing(((CommandSourceStack)context.getSource()).getPosition())))));
    }

    private static int setHeadquarters(CommandSourceStack stack, BlockPos blockPos) {
        Htweaks.getPlayerFactionSaveData().createHeadquarters(blockPos, HTServerConfig.HEADQUARTERS_RADIUS.get());
        stack.sendSuccess(() -> Component.translatable("commands.setheadquarters.success", new Object[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()}), true);
        return 1;
    }

}
