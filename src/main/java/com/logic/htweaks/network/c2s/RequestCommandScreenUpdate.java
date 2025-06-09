package com.logic.htweaks.network.c2s;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.network.IMessage;
import com.logic.htweaks.network.s2c.UpdateCommandScreenPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestCommandScreenUpdate implements IMessage {

    public RequestCommandScreenUpdate () {

    }

    public RequestCommandScreenUpdate (FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if(context != null) {
            ServerPlayer player = context.getSender();

            if(player != null) {
                int currentNumOfSquads = Htweaks.getPatrolManager().getPatrolsByFaction(((IScavFaction)player).htweaks$getFaction()).size();
                int maxNumOfSquads = HTServerConfig.MAX_NUM_OF_SQUADS.get();



                UpdateCommandScreenPacket packet = new UpdateCommandScreenPacket(currentNumOfSquads, maxNumOfSquads);

                HtweaksNetwork.sendTo(player, packet);
            }
        }
    }
}
