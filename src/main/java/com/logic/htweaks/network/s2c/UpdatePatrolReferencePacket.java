package com.logic.htweaks.network.s2c;

import com.logic.htweaks.client.ClientHooks;
import com.logic.htweaks.network.IMessage;
import com.logic.htweaks.patrols.Patrol;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class UpdatePatrolReferencePacket implements IMessage {
    private final HashMap<UUID, BlockPos> patrolData;


    public UpdatePatrolReferencePacket (HashMap<UUID, BlockPos> patrolData) {
        this.patrolData = patrolData;
    }

    public UpdatePatrolReferencePacket (FriendlyByteBuf friendlyByteBuf) {
        patrolData = new HashMap<>();

        int size = friendlyByteBuf.readInt();

        for(int i = 0; i < size; i++) {
            UUID uuid = friendlyByteBuf.readUUID();
            BlockPos blockPos = friendlyByteBuf.readBlockPos();

            patrolData.put(uuid, blockPos);
        }
    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(patrolData.size());

        for(UUID uuid : patrolData.keySet()) {
            friendlyByteBuf.writeUUID(uuid);
            friendlyByteBuf.writeBlockPos(patrolData.get(uuid));
        }
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if(context != null) {
            context.enqueueWork(() -> ClientHooks.updateClientPatrolManager(patrolData));
        }
    }
}
