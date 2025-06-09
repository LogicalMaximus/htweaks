package com.logic.htweaks.client.screen;

import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.network.c2s.commands.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SquadCommandScreen extends Screen {
    public static final Component TITLE = Component.literal("Squad Command");

    public SquadCommandScreen() {
        super(TITLE);
    }

    protected void init() {
        this.addRenderableWidget(this.createButton(30, this.height/20 + 30, Component.literal("Move"), (button) -> HtweaksNetwork.sendToServer(new MoveSquadPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 30 + 35, Component.literal("Move Here"), (button) -> HtweaksNetwork.sendToServer(new MoveToLeaderPosPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 60 + 35, Component.literal("Dismount Your Vehicle"), (button) -> HtweaksNetwork.sendToServer(new DismountCommandPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 90 + 35, Component.literal("Hold Your Position"), (button) -> HtweaksNetwork.sendToServer(new HoldPositionCommandPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 120 + 35, Component.literal("Mount Vehicle"), (button) -> HtweaksNetwork.sendToServer(new MountVehicleCommandPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 150 + 35, Component.literal("Clear Leader"), (button) -> HtweaksNetwork.sendToServer(new ClearPatrolLeaderPacket())));
    }


    public void render(GuiGraphics p_283520_, int p_281826_, int p_283378_, float p_281975_) {
        this.renderBackground(p_283520_);
        p_283520_.drawCenteredString(this.font, this.title, this.width / 2, 15, 16777215);
        super.render(p_283520_, p_281826_, p_283378_, p_281975_);
    }

    private Button createButton(int x, int y, Component title, Button.OnPress pressMethod) {
        return Button.builder(title, pressMethod).pos(x, y).build();
    }
}
