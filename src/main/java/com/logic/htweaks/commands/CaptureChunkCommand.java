package com.logic.htweaks.commands;

import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.registries.ModMemoryTypes;
import com.logic.htweaks.utils.CaptureUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.datafixers.util.Pair;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.cadmus.api.claims.ClaimApi;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.claims.ClaimType;
import earth.terrarium.cadmus.common.commands.claims.ClaimException;
import earth.terrarium.cadmus.common.commands.claims.CommandHelper;
import earth.terrarium.cadmus.common.teams.TeamHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.ColumnPosArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;

public class CaptureChunkCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder) Commands.literal("capturechunk")).executes((context) -> {
            ServerPlayer player = ((CommandSourceStack)context.getSource()).getPlayerOrException();
            CommandHelper.runAction(() -> captureChunk(player, player.chunkPosition()));
            return 1;
        })));
    }

    public static void captureChunk(ServerPlayer player, ChunkPos pos) {
        List<ChunkPos> chunkPositions = new ArrayList<>(ChunkPos.rangeClosed(pos, 3).toList());

        for(ChunkPos cPos : chunkPositions) {
            CaptureUtils.captureChunk(Factions.UNSC, (ServerLevel) player.level(), cPos);
        }
    }
}
