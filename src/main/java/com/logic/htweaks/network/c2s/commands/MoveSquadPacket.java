package com.logic.htweaks.network.c2s.commands;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IPatrolLeader;
import com.logic.htweaks.network.IMessage;
import com.logic.htweaks.registries.ModMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkEvent;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Supplier;

public class MoveSquadPacket implements IMessage {

    public MoveSquadPacket() {

    }

    public MoveSquadPacket(FriendlyByteBuf friendlyByteBuf) {

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

            HitResult hit = player.pick((double)64.0F, 0.0F, false);

            if(hit.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = BlockPos.containing(hit.getLocation());

                List<AbstractScavEntity> entities = level.getEntitiesOfClass(AbstractScavEntity.class, player.getBoundingBox().inflate(64));

                for(AbstractScavEntity scav : entities) {
                    if(((IPatrolLeader)scav).shouldFollowCommands(player)) {
                        BrainUtils.setMemory(scav.getBrain(), ModMemoryTypes.MOVE_POS.get(), pos);
                        BrainUtils.setMemory(scav.getBrain(), ModMemoryTypes.SHOULD_HOLD_POSITION.get(), false);
                    }
                }
            }
        }
    }
}
