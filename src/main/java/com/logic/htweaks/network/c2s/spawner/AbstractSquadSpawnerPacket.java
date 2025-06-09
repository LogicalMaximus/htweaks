package com.logic.htweaks.network.c2s.spawner;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.items.SquadSpawner;
import com.logic.htweaks.network.IMessage;
import com.logic.htweaks.patrols.Patrol;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class AbstractSquadSpawnerPacket implements IMessage {
    public AbstractSquadSpawnerPacket() {

    }

    @Override
    public void encode(FriendlyByteBuf friendlyByteBuf) {

    }

    abstract void spawnSquad(Patrol patrol, int factionID, BlockPos pos, Level level);

    @Override
    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if(context != null && context.getSender() != null) {
            ServerPlayer player = context.getSender();
            ItemStack mainHandItem = player.getMainHandItem();

            if(mainHandItem.getItem() instanceof SquadSpawner ) {
                CompoundTag itemStackTag = mainHandItem.getOrCreateTag();

                if(itemStackTag.contains("factionID")) {
                    int factionID = itemStackTag.getInt("factionID");

                    spawnSquad(Htweaks.getPatrolManager().createPatrol(Factions.getFactionByID(factionID)),factionID, player.blockPosition(), player.level());
                }
            }
        }
    }

}
