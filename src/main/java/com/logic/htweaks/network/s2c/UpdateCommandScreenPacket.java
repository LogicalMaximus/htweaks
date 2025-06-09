package com.logic.htweaks.network.s2c;

import com.logic.htweaks.client.screen.FactionCommandScreen;
import com.logic.htweaks.network.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateCommandScreenPacket implements IMessage {
    private final int currentNumOfSquads;
    private final int maxNumOfSquads;

    public UpdateCommandScreenPacket (int currentNumOfSquads, int maxNumOfSquads) {
        this.currentNumOfSquads = currentNumOfSquads;
        this.maxNumOfSquads = maxNumOfSquads;
    }

    public UpdateCommandScreenPacket (FriendlyByteBuf friendlyByteBuf) {
        this.currentNumOfSquads = friendlyByteBuf.readInt();
        this.maxNumOfSquads = friendlyByteBuf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(currentNumOfSquads);
        friendlyByteBuf.writeInt(maxNumOfSquads);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.screen instanceof FactionCommandScreen screen) {
            screen.currentNumOfSquads = this.currentNumOfSquads;
            screen.maxNumOfSquads = this.maxNumOfSquads;


        }
    }
}
