package com.logic.htweaks.client.journeymaps;

import com.logic.htweaks.Htweaks;
import com.logic.htweaks.client.patrol.ClientPatrol;
import com.mojang.blaze3d.platform.NativeImage;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.display.ImageOverlay;
import journeymap.client.api.model.MapImage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;


public class PatrolImageOverlayFactory {

    public static List<ImageOverlay> create(IClientAPI jmAPI, List<ClientPatrol> patrols)
    {
        List<ImageOverlay> list = new ArrayList<>();

        try
        {
            for(ClientPatrol patrol : patrols) {
                ImageOverlay overlay = createOverlay(jmAPI, patrol.getPos(), 2, 2);

                jmAPI.show(overlay);
                list.add(overlay);
            }
        }
        catch (Throwable t)
        {
            Htweaks.LOGGER.error(t.getMessage(), t);
        }

        return list;
    }

    public static ImageOverlay createOverlay(IClientAPI jmAPI, BlockPos upperLeft, int blocksWide, int blocksTall)
    {
        BlockPos lowerRight = upperLeft.offset(blocksWide, 0, blocksTall);

        // For this example, we'll generate a BufferedImage, but using a pre-existing ResourceLocation works too.
        MapImage image = new MapImage(new ResourceLocation(Htweaks.MODID, "textures/markers/marker_division"), 32, 32);

        image.centerAnchors();
        // Generate a deterministic displayId in case we need to refer to it again
        String displayId = String.format("Patrol", upperLeft.getX(), upperLeft.getZ(), blocksWide, blocksTall);

        ImageOverlay imageOverlay = new ImageOverlay(Htweaks.MODID, displayId, upperLeft, lowerRight, image);

        imageOverlay.getImage().setOpacity(.8f);

        imageOverlay.setDimension(Minecraft.getInstance().player.level().dimension());
        imageOverlay.setLabel("Patrol")
                .setTitle(displayId);

        return imageOverlay;
    }

}
