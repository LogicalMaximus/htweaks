package com.logic.htweaks.client.screen;

import com.logic.htweaks.network.HtweaksNetwork;
import com.logic.htweaks.network.c2s.RequestCommandScreenUpdate;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class FactionCommandScreen extends Screen {
    public static final Component TITLE = Component.literal("Faction Command Screen");

    public int currentNumOfSquads = 0;
    public int maxNumOfSquads = 0;



    public FactionCommandScreen() {
        super(TITLE);
    }

    protected void init() {
        HtweaksNetwork.sendToServer(new RequestCommandScreenUpdate());

        //this.addRenderableWidget(this.createButton(this.width/64, this.height/20 + 30, Component.literal("Move"), (button) -> HtweaksNetwork.sendToServer()));
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
