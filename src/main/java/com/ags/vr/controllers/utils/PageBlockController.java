package com.ags.vr.controllers.utils;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class PageBlockController
{
    @FXML private AnchorPane pane_blocker;

    public void setVisibility(boolean condition)
    {
        this.pane_blocker.setVisible(condition);
    }
}