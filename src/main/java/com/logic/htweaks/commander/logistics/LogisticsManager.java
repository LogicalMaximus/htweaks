package com.logic.htweaks.commander.logistics;

import com.logic.htweaks.config.HTServerConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;

public class LogisticsManager {
    private double resources;

    private HashMap<String, Integer> gunstockpile = new HashMap<>();

    private int manpower = 0;

    public LogisticsManager() {

    }

    public LogisticsManager(CompoundTag tag) {
        this.load(tag);
    }

    public void setResources(double resources) {
        this.resources = resources;
    }

    public double getResources() {
        return resources;
    }

    public void addResources(double addition) {
        this.resources += addition;
    }

    public void removeResources(double subtraction) {
        this.resources -= subtraction;
    }

    public void save(CompoundTag tag) {
        if(resources <= HTServerConfig.MAX_RESOURCES.get()) {
            tag.putDouble("resources", resources);
        }
        else {
            tag.putDouble("resources", 10000);
        }

        if(!gunstockpile.isEmpty()) {
            ListTag gunstockpileTag = new ListTag();

            for(String gunID : gunstockpile.keySet()) {
                CompoundTag gunTag = new CompoundTag();

                gunTag.putString("gunID", gunID);
                gunTag.putInt("numOfGuns", gunstockpile.get(gunID));

                gunstockpileTag.add(gunstockpileTag);
            }

            tag.put("gunstockpileTag", gunstockpileTag);
        }
    }

    public void load(CompoundTag tag) {
        if(tag.contains("resources")) {
            this.resources = tag.getDouble("resources");
        }

        if(tag.contains("gunstockpile")) {
            ListTag gunstockpileTag = tag.getList("gunstockpile", Tag.TAG_COMPOUND);

            for(Tag gunTag : gunstockpileTag) {
                CompoundTag compoundGunTag = (CompoundTag) gunTag;

                this.gunstockpile.put(compoundGunTag.getString("gunID"), compoundGunTag.getInt("numOfGuns"));
            }
        }
    }
}
