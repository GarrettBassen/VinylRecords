package com.ags.vr.controllers.utils;

import com.ags.vr.controllers.cards.CardMediaViewController;
import com.ags.vr.objects.Media;
import javafx.fxml.FXML;

public interface CardBase
{
    /**
     * Closes page by setting invisible.
     */
    @FXML
    void Close();

    /**
     * Reverts to base media card page
     */
    @FXML
    void Back();

    /**
     * Grabs reference to media card to allow for a 'Go Back' feature on current card.
     * @param controller MediaCardController
     */
    void setMediaCard(CardMediaViewController controller);

    /**
     * Sets page visibility.
     * @param condition True for visible; False for invisible
     */
    void setVisible(boolean condition);

    /**
     * Sets media reference object.
     * @param media Media object
     */
    void setMedia(Media media);
}
