package com.logic.htweaks.commands;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.entity.LevelEntityGetter;

import java.util.List;

public class ClearNPCSCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)
                        Commands.literal("clearhtweaksnpcs")
                                .requires((sourceStack) -> sourceStack
                                        .hasPermission(2)))
                        .executes((context) -> clearNPCS((CommandSourceStack)context.getSource()))));
    }

    private static int clearNPCS(CommandSourceStack stack) {
        LevelEntityGetter<Entity> entities = stack.getLevel().getEntities();

        for(Entity entity : entities.getAll()) {
            if(entity instanceof AbstractScavEntity scavEntity) {
                scavEntity.remove(Entity.RemovalReason.DISCARDED);
            }
            else if(entity instanceof Monster monster) {
                monster.remove(Entity.RemovalReason.DISCARDED);
            }
            else if(entity instanceof ItemEntity item) {
                item.remove(Entity.RemovalReason.DISCARDED);
            }
        }

        stack.sendSuccess(() -> Component.literal("Cleared NPCS"), true);

        return 1;
    }

}
