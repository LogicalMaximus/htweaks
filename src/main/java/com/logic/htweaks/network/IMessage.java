package com.logic.htweaks.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface IMessage {

    void encode(FriendlyByteBuf friendlyByteBuf);

    void handle(Supplier<NetworkEvent.Context> contextSupplier);

}
