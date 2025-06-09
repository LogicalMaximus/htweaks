package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.commander.MiltaryCommanderManager;
import com.logic.htweaks.entity.sensor.ChunkSensor;
import com.logic.htweaks.entity.sensor.FlankSensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import com.logic.htweaks.entity.sensor.CoverSensor;

import java.util.function.Supplier;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Htweaks.MODID);

    public static final Supplier<SensorType<CoverSensor<?>>> NEARBY_COVER_SENSOR = registerSensorType("nearby_cover_sensor", CoverSensor::new);
    public static final Supplier<SensorType<ChunkSensor<?>>> UNCLAIMED_CHUNKS = registerSensorType("chunk_sensor", ChunkSensor::new);

    public static final Supplier<SensorType<FlankSensor<?>>> FLANK_SENSOR = registerSensorType("flank_sensor", FlankSensor::new);

    public static void init(IEventBus modEventBus) {
        SENSORS.register(modEventBus);
    }

    public static <T extends ExtendedSensor<?>> Supplier<SensorType<T>> registerSensorType(String id, Supplier<T> sensor) {
        return SENSORS.register(id, () -> {
            return new SensorType(sensor);
        });
    }
}
