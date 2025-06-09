package com.logic.htweaks.network;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.network.s2c.HTSetCamera;
import com.logic.htweaks.network.s2c.UpdateCommandScreenPacket;
import com.logic.htweaks.network.c2s.RequestCommandScreenUpdate;
import com.logic.htweaks.network.c2s.commands.*;
import com.logic.htweaks.network.c2s.spawner.*;
import com.logic.htweaks.network.s2c.UpdatePatrolReferencePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class HtweaksNetwork {
    public static final String NETWORK_VERSION = "0.1.0";
    private static int channel_id = 0;
    public static SimpleChannel HTWEAKS_CHANNEL;

    public static void init() {
        HTWEAKS_CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Htweaks.MODID, "main"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        HTWEAKS_CHANNEL.messageBuilder(HTSetCamera.class, ++channel_id, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(HTSetCamera::new)
                .encoder(HTSetCamera::encode)
                .consumerMainThread(HTSetCamera::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTHelicopterInputPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTHelicopterInputPacket::new)
                .encoder(HTHelicopterInputPacket::encode)
                .consumerMainThread(HTHelicopterInputPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnSquadPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnSquadPacket::new)
                .encoder(HTSpawnSquadPacket::encode)
                .consumerMainThread(HTSpawnSquadPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnTankSquadPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnTankSquadPacket::new)
                .encoder(HTSpawnTankSquadPacket::encode)
                .consumerMainThread(HTSpawnTankSquadPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnAPCSquadPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnAPCSquadPacket::new)
                .encoder(HTSpawnAPCSquadPacket::encode)
                .consumerMainThread(HTSpawnAPCSquadPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnMortarPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnMortarPacket::new)
                .encoder(HTSpawnMortarPacket::encode)
                .consumerMainThread(HTSpawnMortarPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnATSquadPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnATSquadPacket::new)
                .encoder(HTSpawnATSquadPacket::encode)
                .consumerMainThread(HTSpawnATSquadPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnMGTeamPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnMGTeamPacket::new)
                .encoder(HTSpawnMGTeamPacket::encode)
                .consumerMainThread(HTSpawnMGTeamPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnSniperTeamPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnSniperTeamPacket::new)
                .encoder(HTSpawnSniperTeamPacket::encode)
                .consumerMainThread(HTSpawnSniperTeamPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnWarthogPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnWarthogPacket::new)
                .encoder(HTSpawnWarthogPacket::encode)
                .consumerMainThread(HTSpawnWarthogPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HTSpawnFireTeamPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HTSpawnFireTeamPacket::new)
                .encoder(HTSpawnFireTeamPacket::encode)
                .consumerMainThread(HTSpawnFireTeamPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(ClearPatrolLeaderPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ClearPatrolLeaderPacket::new)
                .encoder(ClearPatrolLeaderPacket::encode)
                .consumerMainThread(ClearPatrolLeaderPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(DismountCommandPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(DismountCommandPacket::new)
                .encoder(DismountCommandPacket::encode)
                .consumerMainThread(DismountCommandPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(HoldPositionCommandPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(HoldPositionCommandPacket::new)
                .encoder(HoldPositionCommandPacket::encode)
                .consumerMainThread(HoldPositionCommandPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(MountVehicleCommandPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MountVehicleCommandPacket::new)
                .encoder(MountVehicleCommandPacket::encode)
                .consumerMainThread(MountVehicleCommandPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(MoveSquadPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MoveSquadPacket::new)
                .encoder(MoveSquadPacket::encode)
                .consumerMainThread(MoveSquadPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(MoveToLeaderPosPacket.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(MoveToLeaderPosPacket::new)
                .encoder(MoveToLeaderPosPacket::encode)
                .consumerMainThread(MoveToLeaderPosPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(UpdateCommandScreenPacket.class, ++channel_id, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdateCommandScreenPacket::new)
                .encoder(UpdateCommandScreenPacket::encode)
                .consumerMainThread(UpdateCommandScreenPacket::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(RequestCommandScreenUpdate.class, ++channel_id, NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestCommandScreenUpdate::new)
                .encoder(RequestCommandScreenUpdate::encode)
                .consumerMainThread(RequestCommandScreenUpdate::handle)
                .add();

        HTWEAKS_CHANNEL.messageBuilder(UpdatePatrolReferencePacket.class, ++channel_id, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(UpdatePatrolReferencePacket::new)
                .encoder(UpdatePatrolReferencePacket::encode)
                .consumerMainThread(UpdatePatrolReferencePacket::handle)
                .add();
    }

    public static void sendTo(ServerPlayer player, IMessage message) {
        HTWEAKS_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToAll(IMessage message) {
        HTWEAKS_CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendToServer(IMessage message) {
        HTWEAKS_CHANNEL.send(PacketDistributor.SERVER.noArg(), message);
    }

    public static void sendToClient(IMessage message) {
        HTWEAKS_CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
}
