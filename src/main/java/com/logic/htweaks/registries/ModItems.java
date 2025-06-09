package com.logic.htweaks.registries;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.items.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS;

    public static final RegistryObject<Item> SCORPION_TANK;

    public static final RegistryObject<Item> GHOST;

    public static final RegistryObject<Item> MONGOOSE;

    public static final RegistryObject<Item> WARTHOG;

    public static final RegistryObject<Item> DUST_BORON;

    public static final RegistryObject<Item> DEBUG_ITEM;

    public static final RegistryObject<Item> SQUADSPAWNER_ITEM;

    public static final RegistryObject<Item> ENERGY_SHIELD_UNIT;

    public static final RegistryObject<Item> DESTRUCTION_DEVICE;

    public static final RegistryObject<Item> CALCULATOR_ITEM;

    private static final Item.Properties BASE_ITEM_PROPERTIES = new Item.Properties();

    public static final RegistryObject<BlockItem> SHIP_BLOCK_ITEM;

    public static void init(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    static  {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Htweaks.MODID);

        ENERGY_SHIELD_UNIT = ITEMS.register("shield_unit", () -> new EnergyShieldUnit(new Item.Properties().durability(800)));
        SCORPION_TANK = ITEMS.register("scorpion_tank", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
        SHIP_BLOCK_ITEM = ITEMS.register("ship_ballooon", () -> new BlockItem(ModBlocks.SHIP_BLOCK.get(), BASE_ITEM_PROPERTIES));
        GHOST = ITEMS.register("ghost", () -> new Item(BASE_ITEM_PROPERTIES));
        MONGOOSE = ITEMS.register("mongoose", () -> new Item(BASE_ITEM_PROPERTIES));
        WARTHOG = ITEMS.register("warthog", () -> new Item(BASE_ITEM_PROPERTIES));
        DUST_BORON = ITEMS.register("dust_boron", () -> new Item(BASE_ITEM_PROPERTIES));
        SQUADSPAWNER_ITEM = ITEMS.register("squad_spawner", () -> new SquadSpawner(BASE_ITEM_PROPERTIES));
        DEBUG_ITEM = ITEMS.register("debug_item", () -> new DebugItem(BASE_ITEM_PROPERTIES));
        DESTRUCTION_DEVICE = ITEMS.register("destruction_device", () -> new DestructionDevice(new Item.Properties().durability(1).fireResistant().rarity(Rarity.EPIC)));
        CALCULATOR_ITEM = ITEMS.register("calculator_item", () -> new CalculatorItem(BASE_ITEM_PROPERTIES));
    }
}
