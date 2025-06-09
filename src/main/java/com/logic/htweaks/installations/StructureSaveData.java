package com.logic.htweaks.installations;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

public class StructureSaveData extends SavedData {

    private static final String SAVE_DATA = "htstructures";

    private List<ChunkPos> chunkPositions = new ArrayList<>();

    public void addStructurePos(ChunkPos chunkPos) {
        chunkPositions.add(chunkPos);

        this.setDirty();
    }

    public List<ChunkPos> getChunkPositions() {
        return chunkPositions;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for(ChunkPos pos : chunkPositions) {
            CompoundTag tag = new CompoundTag();

            tag.putInt("x", pos.x);
            tag.putInt("z", pos.z);

            listTag.add(tag);
        }

        compoundTag.put("listTag", listTag);

        return compoundTag;
    }

    public static StructureSaveData create() {
        return new StructureSaveData();
    }

    public static StructureSaveData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(StructureSaveData::load, StructureSaveData::create, SAVE_DATA);
    }

    public static StructureSaveData load(CompoundTag tag) {
        StructureSaveData saveData = StructureSaveData.create();

        ListTag listTag = tag.getList("listTag", ListTag.TAG_COMPOUND);

        for(Tag structureTag : listTag) {
            CompoundTag structureCTag = (CompoundTag) structureTag;

            ChunkPos cPos = new ChunkPos(structureCTag.getInt("x"), structureCTag.getInt("z"));

            saveData.chunkPositions.add(cPos);
        }

        return saveData;
    }
}
