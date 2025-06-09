package com.logic.htweaks.network.c2s.commands;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IPatrolLeader;
import com.logic.htweaks.network.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ClearPatrolLeaderPacket implements IMessage {
    public ClearPatrolLeaderPacket() {

    }

    public ClearPatrolLeaderPacket(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if(context != null) {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            List<AbstractScavEntity> entities = level.getEntitiesOfClass(AbstractScavEntity.class, player.getBoundingBox().inflate(64));

            for(AbstractScavEntity scav : entities) {
                if(((IPatrolLeader)scav).shouldFollowCommands(player) && scav.getVehicle() != null) {
                    ((IPatrolLeader)scav).htweaks$clearPatrolLeader();
                }
            }
        }
    }
}
