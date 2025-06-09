package com.logic.htweaks.commander.objective;

import com.corrinedev.tacznpcs.common.entity.AbstractScavEntity;
import com.logic.htweaks.bridge.IScavFaction;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.waypoints.Waypoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Objective implements IScavFaction {

    //Tactical Area Of Responsibility
    private final Waypoint toar;

    private final UUID id;

    private final Level level;

    private List<Waypoint> installations;

    private Faction faction;

    private boolean isUnderCapture;

    private int captureTicks = 0;

    public Objective(Level level, UUID id, Waypoint toar) {
        this.level = level;
        this.toar = toar;
        this.id = id;

        faction = Factions.DEFAULT;
    }

    public Objective(Level level, CompoundTag tag) {
        this.level = level;

        this.toar = new Waypoint(tag.getCompound("toarTag"));
        this.id = tag.getUUID("id");

        this.isUnderCapture = tag.getBoolean("isUnderCapture");
        this.captureTicks = tag.getInt("captureTicks");

        ListTag waypointsListTag = tag.getList("waypointsListTag", Tag.TAG_COMPOUND);

        for(Tag waypointTag : waypointsListTag) {
            Waypoint waypoint = new Waypoint((CompoundTag) waypointTag);

            this.installations.add(waypoint);
        }

        if(tag.contains("factionID")) {
            this.faction = Factions.getFactionByID(tag.getInt("factionID"));
        }
        else {
            this.faction = Factions.DEFAULT;
        }
    }

    public void tick() {

        List<AbstractScavEntity> entities = level.getEntitiesOfClass(AbstractScavEntity.class, toar.getAabb());
        List<Player> players = level.getEntitiesOfClass(Player.class, toar.getAabb());

        List<IScavFaction> enemies = new ArrayList<>();
        List<IScavFaction> allies = new ArrayList<>();

        for(Player player : players) {
            if(((IScavFaction)player).htweaks$getFaction() != this.htweaks$getFaction()) {
                enemies.add((IScavFaction) player);
            }
            else if(((IScavFaction)player).htweaks$getFaction() == this.htweaks$getFaction()) {
                allies.add((IScavFaction) player);
            }
        }

        for(AbstractScavEntity entity : entities) {
            if(entity.deadAsContainer) {
                continue;
            }

            if(((IScavFaction)entity).htweaks$getFaction() != this.htweaks$getFaction()) {
                enemies.add((IScavFaction) entity);
            }
            else if(((IScavFaction)entity).htweaks$getFaction() == this.htweaks$getFaction()) {
                allies.add((IScavFaction) entity);
            }
        }

        if(!enemies.isEmpty() && allies.isEmpty() && !this.isUnderCapture) {
            this.isUnderCapture = true;

            for(Player player : players) {
                player.sendSystemMessage(Component.literal("Objective Is Now Under Capture"));
            }
        }
        else if (enemies.isEmpty() && !allies.isEmpty()) {
            this.isUnderCapture = false;

            this.captureTicks = 0;

            for(Player player : players) {
                player.sendSystemMessage(Component.literal("Objective Is No Longer Under Capture"));
            }
        }

        if(isUnderCapture) {
            if(captureTicks >= HTServerConfig.OBJECTIVE_CAPTURE_TIME.get()) {
                if(!enemies.isEmpty()) {
                    this.htweaks$setFaction(enemies.get(0).htweaks$getFaction());
                }
                else {
                    this.htweaks$setFaction(Factions.DEFAULT);
                }

                for (Player player : players) {
                    player.sendSystemMessage(Component.literal("The Objective Has Been Captured"));
                }

                isUnderCapture = false;
                captureTicks = 0;
            } else {
                captureTicks++;
            }
        }
    }


    public Waypoint getToar() {
        return toar;
    }

    public UUID getUUID() {
        return id;
    }

    public List<Waypoint> getInstallations() {
        return installations;
    }

    public void save(CompoundTag tag) {
        tag.putUUID("id", this.id);

        tag.putInt("caputreTicks", this.captureTicks);

        tag.putBoolean("isUnderCapture", this.isUnderCapture);

        CompoundTag toarTag = new CompoundTag();

        this.toar.save(toarTag);

        tag.put("toarTag", toarTag);

        ListTag waypointsListTag = new ListTag();

        for(Waypoint waypoint : installations) {
            CompoundTag waypointTag = new CompoundTag();

            waypoint.save(waypointTag);

            waypointsListTag.add(waypointTag);
        }

        tag.put("waypointsListTag", waypointsListTag);

        if(faction != null) {
            tag.putInt("factionID", faction.getID());
        }
    }

    @Override
    @Nullable
    public Faction htweaks$getFaction() {
        return faction;
    }

    @Override
    public void htweaks$setFaction(Faction faction) {
        this.faction = faction;
    }
}
