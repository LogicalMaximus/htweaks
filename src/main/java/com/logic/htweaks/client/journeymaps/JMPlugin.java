package com.logic.htweaks.client.journeymaps;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.client.ClientHooks;
import com.logic.htweaks.client.patrol.ClientPatrol;
import com.logic.htweaks.client.patrol.ClientPatrolManager;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.DisplayType;
import journeymap.client.api.event.ClientEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.EnumSet;
import java.util.List;

import static journeymap.client.api.event.ClientEvent.Type.*;

@journeymap.client.api.ClientPlugin
public class JMPlugin implements IClientPlugin {
    private IClientAPI jmAPI = null;

    private static JMPlugin INSTANCE;

    public JMPlugin()
    {
        INSTANCE = this;
    }

    @Override
    public void initialize(IClientAPI iClientAPI) {
        this.jmAPI = iClientAPI;

        // Subscribe to desired ClientEvent types from JourneyMap
        this.jmAPI.subscribe(getModId(), EnumSet.of(DEATH_WAYPOINT, MAPPING_STARTED, MAPPING_STOPPED, REGISTRY));

        Htweaks.LOGGER.info("Initialized " + getClass().getName());
    }

    @Override
    public String getModId() {
        return Htweaks.MODID;
    }

    @Override
    public void onEvent(ClientEvent event)
    {
        try
        {
            switch (event.type)
            {
                case MAPPING_STARTED:
                    onMappingStarted(event);
                    break;

                case MAPPING_STOPPED:
                    onMappingStopped(event);
                    break;
            }
        }
        catch (Throwable t)
        {
            Htweaks.LOGGER.error(t.getMessage(), t);
        }
    }

    void onMappingStarted(ClientEvent event)
    {
        if (jmAPI.playerAccepts(Htweaks.MODID, DisplayType.Image))
        {
            jmAPI.removeAll(Htweaks.MODID);

            ClientPatrolManager manager = ClientHooks.getPatrolManager();

            if(manager != null) {
                List<ClientPatrol> patrols = manager.getPatrols();

                for(ClientPatrol patrol : patrols) {
                   PatrolImageOverlayFactory.create(jmAPI, patrols);
                }
            }
        }

    }

    void onMappingStopped(ClientEvent event)
    {
        // Clear everything
        jmAPI.removeAll(Htweaks.MODID);
    }
}
