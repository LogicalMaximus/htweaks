package com.logic.htweaks.network.c2s.commands;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IPatrolLeader;
import com.logic.htweaks.network.IMessage;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Supplier;

public class MountVehicleCommandPacket implements IMessage {
    public MountVehicleCommandPacket() {

    }

    public MountVehicleCommandPacket(FriendlyByteBuf friendlyByteBuf) {

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

            HitResult hit = player.pick((double)20.0F, 0.0F, false);

            if(hit.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult)hit).getEntity();

                List<AbstractScavEntity> entities = level.getEntitiesOfClass(AbstractScavEntity.class, player.getBoundingBox().inflate(64));

                for(AbstractScavEntity scav : entities) {
                    if(((IPatrolLeader)scav).shouldFollowCommands(player)) {
                        BrainUtils.setMemory(scav.getBrain(), ModMemoryTypes.MOUNT_TARGET.get(), entity);
                    }
                }
            }
        }
    }
}
