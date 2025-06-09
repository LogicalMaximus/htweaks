package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.network.IMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HTHelicopterInputPacket implements IMessage {
    private boolean inputLeft;
    private boolean inputRight;
    private boolean inputUp;
    private boolean inputDown;
    private boolean inputAscend;
    private boolean inputDescend;

    public HTHelicopterInputPacket(boolean inputLeft, boolean inputRight, boolean inputUp, boolean inputDown, boolean inputAscend, boolean inputDescend) {
        this.inputLeft = inputLeft;
        this.inputRight = inputRight;
        this.inputUp = inputUp;
        this.inputDown = inputDown;
        this.inputAscend = inputAscend;
        this.inputDescend = inputDescend;
    }

    public HTHelicopterInputPacket(FriendlyByteBuf friendlyByteBuf) {
        this.inputLeft = friendlyByteBuf.readBoolean();
        this.inputRight = friendlyByteBuf.readBoolean();
        this.inputUp = friendlyByteBuf.readBoolean();
        this.inputDown = friendlyByteBuf.readBoolean();
        this.inputAscend = friendlyByteBuf.readBoolean();
        this.inputDescend = friendlyByteBuf.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(inputLeft);
        friendlyByteBuf.writeBoolean(inputRight);
        friendlyByteBuf.writeBoolean(inputUp);
        friendlyByteBuf.writeBoolean(inputDown);
        friendlyByteBuf.writeBoolean(inputAscend);
        friendlyByteBuf.writeBoolean(inputDescend);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {

    }
}
