package com.logic.htweaks.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.common.util.Lazy;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final Lazy<KeyMapping> SQUAD_COMMAND_KEY = Lazy.of(() -> new KeyMapping("key.htweaks.squad_command_key", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, "key.categories.misc"));

}
