package com.logic.htweaks.entity.vehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public interface IVehicleEntity<E extends Entity> {
    @NotNull E getEntity();

    int getMaxPassengers();

    int getVehicleHealth();

    void fireShoot();

    boolean isTransport();

    EntityType getEntityType();

    VehicleType getVehicleType();
}
