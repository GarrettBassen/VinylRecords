package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;

import com.ags.vr.objects.Media;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class BandEditController implements CardBase
{
    @FXML private HBox pane_base;

    // Previous card
    private MediaCardController card_base;
    private Media media;

    @Override
    public void Close()
    {
        this.setVisible(false);
        card_base.setPageBlockVisibility(false);
    }

    @Override
    public void Back()
    {
        this.Close();
        this.card_base.setVisible(true);
    }

    @Override
    public void setMediaCard(MediaCardController controller)
    {
        this.card_base = controller;
    }

    @Override
    public void setVisible(boolean condition)
    {
        this.pane_base.setVisible(condition);
        if (condition) { card_base.setPageBlockVisibility(true); }
    }

    @Override
    public void setMedia(Media media)
    {
        this.media = media;
    }
}
