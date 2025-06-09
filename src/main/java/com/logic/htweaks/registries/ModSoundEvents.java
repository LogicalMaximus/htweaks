package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Htweaks.MODID);

    public static void init (IEventBus modEventBus) {
        SOUND_EVENTS.register(modEventBus);
    }

    public static final RegistryObject<SoundEvent> TANK_SHOOT = SOUND_EVENTS.register("tank_shoot", () -> {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(Htweaks.MODID, "tank_shoot"));
    });

    public static final RegistryObject<SoundEvent> TANK_HIT = SOUND_EVENTS.register("tank_hit", () -> {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(Htweaks.MODID, "tank_hit"));
    });

    public static final RegistryObject<SoundEvent> WARTHOG_SHOOT = SOUND_EVENTS.register("warthog_shoot", () -> {
        return SoundEvent.createVariableRangeEvent(new ResourceLocation(Htweaks.MODID, "warthog_shoot"));
    });

}
