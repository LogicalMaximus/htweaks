package com.logic.htweaks;



import com.logic.htweaks.commander.MiltaryCommanderManager;
import com.logic.htweaks.commander.objective.ObjectiveManager;
import com.logic.htweaks.compat.enhancedvisuals.EVAddon;
import com.logic.htweaks.config.HTServerConfig;
import com.logic.htweaks.event.ForgeEvents;
import com.logic.htweaks.event.ModEvents;
import com.logic.htweaks.faction.Faction;
import com.logic.htweaks.faction.Factions;
import com.logic.htweaks.faction.PlayerFactionSaveData;
import com.logic.htweaks.installations.StructureSaveData;
import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.patrols.PatrolSavedData;
import com.logic.htweaks.registries.*;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Htweaks.MODID)
public class Htweaks {

    public static final String MODID = "htweaks";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isFirstAidPresent = false;

    private static PatrolSavedData patrolSavedData;

    private static ObjectiveManager objectiveManager;

    private static PlayerFactionSaveData playerFactionSaveData;

    private static StructureSaveData structureSaveData;

    private static MiltaryCommanderManager miltaryCommanderManager;

    public Htweaks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, HTServerConfig.SPEC, "htweaks-server.toml");

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ForgeEvents.class);

        ModItems.init(modEventBus);
        ModBlocks.init(modEventBus);
        ModEntities.init(modEventBus);
        ModCreativeTabs.init(modEventBus);
        ModAttributes.init(modEventBus);
        ModSoundEvents.init(modEventBus);
        ModMemoryTypes.init(modEventBus);
        ModSensors.init(modEventBus);
        ModMenuTypes.init(modEventBus);

        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        modEventBus.addListener(ModEvents::onAttributeEvent);
        modEventBus.addListener(this::commonSetup);

        isFirstAidPresent = ModList.get().isLoaded("firstaid");

        if(ModList.get().isLoaded("enhancedvisuals")) {
            EVAddon.load();
        }

        //TeamProviderApi.API.register(new ResourceLocation(MODID, "faction"), new FactionTeamProvider());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        HtweaksNetwork.init();
        Factions.init();
    }

    public void onServerStart(ServerStartedEvent event) {
        patrolSavedData = PatrolSavedData.get(event.getServer().getLevel(Level.OVERWORLD));
        objectiveManager = ObjectiveManager.get(event.getServer().getLevel(Level.OVERWORLD));
        playerFactionSaveData = PlayerFactionSaveData.get(event.getServer().getLevel(Level.OVERWORLD));
        structureSaveData = StructureSaveData.get(event.getServer().getLevel(Level.OVERWORLD));
        miltaryCommanderManager = MiltaryCommanderManager.get(event.getServer().getLevel(Level.OVERWORLD));

        for(Faction faction : Factions.getFactions()) {
            PlayerTeam team = event.getServer().getScoreboard().addPlayerTeam(faction.getName());

            team.setDisplayName(Component.literal(faction.getDisplayName()));
            team.setColor(faction.getColor());
        }
    }

    public static PatrolSavedData getPatrolManager() {
        return patrolSavedData;
    }

    public static ObjectiveManager getObjectiveManager() {
        return objectiveManager;
    }

    public static PlayerFactionSaveData getPlayerFactionSaveData() {
        return playerFactionSaveData;
    }

    public static StructureSaveData getStructureSaveData() {
        return structureSaveData;
    }

    public static MiltaryCommanderManager getMiltaryCommanderManager() {
        return miltaryCommanderManager;
    }
}
