package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.patrols.Patrol;
import com.logic.htweaks.waypoints.Waypoint;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class ModMemoryTypes {
    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_TYPES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, Htweaks.MODID);

    public static final Supplier<MemoryModuleType<Entity>> MOUNT_TARGET = registerMemoryType("nearby_mountable_targets");

    public static final Supplier<MemoryModuleType<Boolean>> SHOULD_HOLD_POSITION = registerMemoryType("should_hold_position");

    public static final Supplier<MemoryModuleType<Boolean>> SHOULD_DISMOUNT = registerMemoryType("should_dismount");

    public static final Supplier<MemoryModuleType<List<ChunkPos>>> UNCLAIMED_CHUNKS = registerMemoryType("unclaimed_chunks");

    public static final Supplier<MemoryModuleType<List<BlockPos>>> KNOWN_ENEMY_LOCATIONS = registerMemoryType("known_enemy_locations");

    public static final Supplier<MemoryModuleType<ChunkPos>> TARGET_CHUNK_POSITION = registerMemoryType("target_chunk_position");

    public static final Supplier<MemoryModuleType<List<ChunkPos>>> TARGET_CHUNKS = registerMemoryType("target_chunks");

    public static final Supplier<MemoryModuleType<BlockPos>> MOVE_POS = registerMemoryType("movepos");

    public static final Supplier<MemoryModuleType<List<BlockPos>>> NEARBY_COVER_POSITIONS = registerMemoryType("nearby_cover_positions");

    public static final Supplier<MemoryModuleType<BlockPos>> ARTILLERY_STRIKE_POS = registerMemoryType("artillery_strike_pos");

    public static final Supplier<MemoryModuleType<List<BlockPos>>> ARTILLERY_STRIKE_POSITIONS = registerMemoryType("artillery_strike_positions");

    public static final Supplier<MemoryModuleType<List<BlockPos>>> FLANK_POS = registerMemoryType("flank_pos");

    public static final Supplier<MemoryModuleType<BlockPos>> SPAWN_SQUAD_POS = registerMemoryType("spawn_squad_pos");

    public static final Supplier<MemoryModuleType<BlockPos>> MISSILE_STRIKE_POS = registerMemoryType("missile_strike_pos");

    public static final Supplier<MemoryModuleType<List<Waypoint>>> WAYPOINTS = registerMemoryType("waypoints");

    public static final Supplier<MemoryModuleType<Patrol>> CURRENT_CONTROL_SQUAD = registerMemoryType("current_control_squad");

    public static final Supplier<MemoryModuleType<List<Patrol>>> CURRENT_CONTROL_SQUADS = registerMemoryType("current_control_squads");

    public static final Supplier<MemoryModuleType<BlockPos>> TARGET_SQUAD_MOVE_POS = registerMemoryType("target_squad_move_pos");

    public static final Supplier<MemoryModuleType<BlockPos>> TARGET_SQUAD_WAYPOINT_POS = registerMemoryType("target_squad_waypoint_pos");

    public static final Supplier<MemoryModuleType<BlockPos>> TARGET_FLANK_POS = registerMemoryType("target_squad_pos");

    public static void init(IEventBus modEventBus) {
        MEMORY_TYPES.register(modEventBus);
    }

    public static <T> Supplier<MemoryModuleType<T>> registerMemoryType(String id) {
        return ModMemoryTypes.registerMemoryType(id, (Codec)null);
    }

    public static <T> Supplier<MemoryModuleType<T>> registerMemoryType(String id, @Nullable Codec<T> codec) {
        return MEMORY_TYPES.register(id, () -> {
            return new MemoryModuleType(Optional.ofNullable(codec));
        });
    }
}
