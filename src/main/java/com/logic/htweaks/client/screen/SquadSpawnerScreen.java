package com.logic.htweaks.client.screen;


import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.network.c2s.spawner.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SquadSpawnerScreen extends Screen {
    public static final Component TITLE = Component.literal("Squad Spawner");

    public SquadSpawnerScreen() {
        super(TITLE);
    }

    protected void init() {
        this.addRenderableWidget(this.createButton(30, this.height/20 + 30, Component.literal("Spawn Squad"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnSquadPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 30 + 30, Component.literal("Spawn Mechanized Squad"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnTankSquadPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 60 + 30, Component.literal("Spawn APC Squad"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnAPCSquadPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 90 + 30, Component.literal("Spawn Mortar Team"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnMortarPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 120 + 30, Component.literal("Spawn AT Team"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnATSquadPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 150 + 30, Component.literal("Spawn MG Team"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnMGTeamPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 180 + 30, Component.literal("Spawn Sniper Team"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnSniperTeamPacket())));
        this.addRenderableWidget(this.createButton(30, this.height/20 + 210 + 30, Component.literal("Spawn Warthog"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnWarthogPacket())));
        this.addRenderableWidget(this.createButton(this.width/2 - 75, this.height/20 + 30, Component.literal("Spawn Fire Team"), (button) -> HtweaksNetwork.sendToServer(new HTSpawnFireTeamPacket())));
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
