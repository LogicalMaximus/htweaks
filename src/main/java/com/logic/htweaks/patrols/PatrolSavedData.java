package com.logic.htweaks.patrols;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.Htweaks;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.network.s2c.UpdatePatrolReferencePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PatrolSavedData extends SavedData {

    private int ticks = 0;

    public static final String SAVE_ID = "patrols";

    private final List<@NotNull Patrol> patrols = new ArrayList<>();

    private final ServerLevel level;

    public PatrolSavedData() {
        level = ServerLifecycleHooks.getCurrentServer().overworld();
    }

    public void tick() {
        for(Patrol patrol : patrols) {
            patrol.tick();
        }

        if(ticks % 20 == 0) {
            HashMap<UUID, BlockPos> patrolsData = new HashMap<>();

            for(Patrol patrol : patrols) {
                if(patrol.getPos() != null) {
                    patrolsData.put(patrol.getUUID(), patrol.getPos().getWorldPosition());
                }
            }

            HtweaksNetwork.sendToClient(new UpdatePatrolReferencePacket(patrolsData));
        }

        ticks++;
    }

    public Patrol createPatrol(Faction faction) {
        Patrol patrol = new Patrol(UUID.randomUUID(), faction, this.level);

        this.addPatrol(patrol);

        return patrol;
    }

    private void addPatrol(Patrol patrol) {
        this.patrols.add(patrol);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for(Patrol patrol : patrols) {
            CompoundTag tag = new CompoundTag();

            patrol.save(tag);

            listTag.add(tag);
        }

        compoundTag.put("listTag", listTag);

        return compoundTag;
    }

    public static PatrolSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(PatrolSavedData::load, PatrolSavedData::create, SAVE_ID);
    }

    public List<Patrol> getPatrols() {
        return patrols;
    }

    //To Do: replace with predicate: low priority
    public List<Patrol> getPatrolsByFaction(Faction faction) {
        List<Patrol> patrolsInFaction = new ArrayList<>();

        for(Patrol patrol : patrols) {
            if(patrol.getFaction() == faction) {
                patrolsInFaction.add(patrol);
            }
        }

        return patrolsInFaction;
    }

    public int getPatrolCount() {
        return patrols.size();
    }

    public boolean canCreatePatrol() {
        return this.getPatrolCount() < HTServerConfig.MAX_PATROL.get();
    }

    private static PatrolSavedData create() {
        return new PatrolSavedData();
    }

    private static PatrolSavedData load(CompoundTag compoundTag) {
        PatrolSavedData data = PatrolSavedData.create();

        ListTag listTag = compoundTag.getList("listTag", Tag.TAG_COMPOUND);

        for(Tag tag : listTag) {
            CompoundTag patrolTag = (CompoundTag) tag;

            Patrol patrol = new Patrol(patrolTag, data.level);

            data.addPatrol(patrol);
        }

        return data;
    }

    @Nullable
    public Patrol getPatrol(UUID patrolUUID) {
        for(Patrol patrol : patrols) {
            if(patrol.getUUID() == patrolUUID) {
                //patrol.addMember(entity);

                return patrol;
            }
        }

        return null;
    }
}
