package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static DeferredRegister<CreativeModeTab> CREATIVE_TABS;
    public static final RegistryObject<CreativeModeTab> HTWEAKS_CREATIVE_TAB;

    public static void init(IEventBus eventBus) {
        CREATIVE_TABS.register(eventBus);
    }

    static {
        CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Htweaks.MODID);

        HTWEAKS_CREATIVE_TAB = CREATIVE_TABS.register("htweaks_creative_tab", () -> CreativeModeTab.builder().displayItems((parameters, output) -> {
            output.accept(ModItems.SCORPION_TANK.get());
            output.accept(ModItems.SHIP_BLOCK_ITEM.get());
            output.accept(ModItems.DUST_BORON.get());
        }).build());
    }

}
