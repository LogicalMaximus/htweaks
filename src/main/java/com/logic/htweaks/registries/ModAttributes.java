package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, Htweaks.MODID);

    public static void init(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }

    public static final RegistryObject<Attribute> SKILL_LEVEL = ATTRIBUTES.register("skill_level", () -> new RangedAttribute("skill_level", 0.0, 0.0, 1.0).setSyncable(true));

}
