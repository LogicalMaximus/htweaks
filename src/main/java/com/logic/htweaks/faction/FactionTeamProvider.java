package com.logic.htweaks.faction;

import com.mojang.authlib.GameProfile;
import earth.terrarium.cadmus.api.claims.InteractionType;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class FactionTeamProvider implements TeamProvider {
    @Override
    public Set<GameProfile> getTeamMembers(String s, MinecraftServer minecraftServer) {
        return Set.of();
    }

    @Override
    public @Nullable Component getTeamName(String s, MinecraftServer minecraftServer) {
        Faction faction = Factions.getFactionByString(s);

        if(faction != null) {
            return Component.literal(faction.getDisplayName());
        }
        else {
            return null;
        }
    }

    @Override
    public @Nullable String getTeamId(MinecraftServer minecraftServer, UUID uuid) {
        return "";
    }

    @Override
    public boolean isMember(String s, MinecraftServer minecraftServer, UUID uuid) {
        return false;
    }

    @Override
    public ChatFormatting getTeamColor(String s, MinecraftServer minecraftServer) {
        Faction faction = Factions.getFactionByString(s);

        if(faction != null) {
            return faction.getColor();
        }
        else {
            return null;
        }
    }

    @Override
    public boolean canBreakBlock(String s, MinecraftServer minecraftServer, BlockPos blockPos, UUID uuid) {
        return false;
    }

    @Override
    public boolean canPlaceBlock(String s, MinecraftServer minecraftServer, BlockPos blockPos, UUID uuid) {
        return false;
    }

    @Override
    public boolean canExplodeBlock(String s, MinecraftServer minecraftServer, BlockPos blockPos, Explosion explosion, UUID uuid) {
        return true;
    }

    @Override
    public boolean canInteractWithBlock(String s, MinecraftServer minecraftServer, BlockPos blockPos, InteractionType interactionType, UUID uuid) {
        return true;
    }

    @Override
    public boolean canInteractWithEntity(String s, MinecraftServer minecraftServer, Entity entity, UUID uuid) {
        return true;
    }

    @Override
    public boolean canDamageEntity(String s, MinecraftServer minecraftServer, Entity entity, UUID uuid) {
        return true;
    }
}
