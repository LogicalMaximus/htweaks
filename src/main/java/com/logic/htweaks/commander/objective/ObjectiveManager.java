package com.logic.htweaks.commander.objective;

import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObjectiveManager extends SavedData {
    private static final String SAVE_DATA = "objectives";

    private final List<Objective> objectives = new ArrayList<>();

    private final ServerLevel level;

    private ObjectiveManager() {
        level = ServerLifecycleHooks.getCurrentServer().overworld();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag objectivesTag = new ListTag();

        for(Objective objective : objectives) {
            CompoundTag tag = new CompoundTag();

            objective.save(tag);

            objectivesTag.add(tag);
        }

        compoundTag.put("objectivesTag", objectivesTag);

        return compoundTag;
    }

    public void tick() {
        for(Objective objective : objectives) {
            objective.tick();
        }
    }

    public Objective createObjective(BlockPos pos, int radius) {
        Waypoint objectiveWaypoint = new Waypoint(pos, radius);

        Objective objective = new Objective(this.level, UUID.randomUUID(), objectiveWaypoint);


        this.objectives.add(objective);

        this.setDirty();

        return objective;
    }

    public void removeObjective(UUID uuid) {
        Objective obj = this.getObjectiveByUUID(uuid);

        if(objectives.contains(obj)) {
            objectives.remove(obj);
        }
    }

    @Nullable
    public Objective getObjectiveByUUID(UUID uuid) {
        for(Objective objective : objectives) {
            if(objective.getUUID() == uuid) {
                return objective;
            }
        }

        return null;
    }

    public static ObjectiveManager create() {
        return new ObjectiveManager();
    }

    public static ObjectiveManager get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ObjectiveManager::load, ObjectiveManager::create, SAVE_DATA);
    }

    public static ObjectiveManager load(CompoundTag tag) {
        ServerLevel level = ServerLifecycleHooks.getCurrentServer().overworld();

        ObjectiveManager objectiveManager = ObjectiveManager.create();

        ListTag objectivesTag = tag.getList("objectivesTag", Tag.TAG_COMPOUND);

        for(Tag objectiveTag : objectivesTag) {
            CompoundTag compoundTag = (CompoundTag) objectiveTag;

            Objective objective = new Objective(level, compoundTag);

            objectiveManager.objectives.add(objective);
        }

        return objectiveManager;
    }

    public List<Objective> getObjectives() {
        return objectives;
    }
}
