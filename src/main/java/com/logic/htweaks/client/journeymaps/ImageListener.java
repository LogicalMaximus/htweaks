package com.logic.htweaks.client.journeymaps;

import journeymap.client.api.display.IOverlayListener;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.util.UIState;
import net.minecraft.core.BlockPos;

import java.awt.geom.Point2D;

public class ImageListener implements IOverlayListener {

    @Override
    public void onActivate(UIState uiState) {

    }

    @Override
    public void onDeactivate(UIState uiState) {

    }

    @Override
    public void onMouseMove(UIState uiState, Point2D.Double aDouble, BlockPos blockPos) {

    }

    @Override
    public void onMouseOut(UIState uiState, Point2D.Double aDouble, BlockPos blockPos) {

    }

    @Override
    public boolean onMouseClick(UIState uiState, Point2D.Double aDouble, BlockPos blockPos, int i, boolean b) {
        return false;
    }

    @Override
    public void onOverlayMenuPopup(UIState uiState, Point2D.Double aDouble, BlockPos blockPos, ModPopupMenu modPopupMenu) {

    }
}
