package com.logic.htweaks.compat.enhancedvisuals;

import com.logic.htweaks.Htweaks;
import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class EVAddon {
    public static SuppressionHandler suppressionHandler;

    public static void load() {
        VisualRegistry.registerHandler(new ResourceLocation(Htweaks.MODID, "suppression"), suppressionHandler = new SuppressionHandler());
    }
}
