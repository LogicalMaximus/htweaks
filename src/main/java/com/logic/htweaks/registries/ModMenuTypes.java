package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Htweaks.MODID);

    //public static final RegistryObject<MenuType<SquadSpawnerMenu>> SQUAD_SPAWNER_MENU;

    public static void init(IEventBus modIEventBus) {
        MENU_TYPES.register(modIEventBus);
    }

    static {
        //SQUAD_SPAWNER_MENU = MENU_TYPES.register("squad_spawner", () -> IForgeMenuType.create(SquadSpawnerMenu::new));
    }

}
