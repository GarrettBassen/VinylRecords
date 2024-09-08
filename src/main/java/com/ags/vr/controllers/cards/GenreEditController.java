package com.ags.vr.controllers.cards;

import com.ags.vr.objects.Media;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class GenreEditController
{
    @FXML private HBox pane_base;

    public void setVisible(boolean condition)
    {
        this.pane_base.setVisible(condition);
    }

    public void setMedia(Media media)
    {
        // TODO SET MEDIA
    }

    /**
     * Closes page by setting invisible.
     */
    @FXML
    void Close()
    {
        setVisible(false);
    }
}
