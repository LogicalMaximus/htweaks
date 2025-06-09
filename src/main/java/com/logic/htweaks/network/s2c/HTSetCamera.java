package com.logic.htweaks.network.s2c;

import com.logic.htweaks.network.IMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class HTSetCamera implements IMessage {
    private int entityID;

    public HTSetCamera(int entityID) {
        this.entityID = entityID;
    }

    public HTSetCamera(FriendlyByteBuf friendlyByteBuf) {
        this.entityID = friendlyByteBuf.readInt();
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(entityID);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft minecraft = Minecraft.getInstance();

        Entity entity = minecraft.level.getEntity(entityID);

        minecraft.levelRenderer.allChanged();
    }
}
