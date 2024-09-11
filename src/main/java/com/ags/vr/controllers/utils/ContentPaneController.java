package com.ags.vr.controllers.utils;

import com.ags.vr.controllers.cards.CardMediaViewController;
import com.ags.vr.objects.Media;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Content display panes for the browse page
 */
public class ContentPaneController
{
    @FXML private AnchorPane pane_content;
    @FXML private ImageView img_medium;
    @FXML private Text txt_band;
    @FXML private Text txt_format;
    @FXML private Text txt_title;
    @FXML private Text txt_year;

    // Popup reference
    private CardMediaViewController cardPopup;
    private Media media;


    /**
     * Opens card popup to display data of given Media object.
     */
    @FXML
    void OpenDisplay()
    {
        cardPopup.setMedia(this.media);
        cardPopup.setVisible(true);
    }

    /**
     * Sets data in content pane with given Media object.
     * @param media Media object
     */
    public void setData(CardMediaViewController cardPopup, Media media)
    {
        // Reference variables
        this.media = media;
        this.cardPopup = cardPopup;

        // Display variables
        this.pane_content.setPrefWidth(Integer.MAX_VALUE);
        this.txt_title.setText(media.getTitle());
        this.txt_band.setText(media.getBand());
        this.txt_format.setText(media.getFormat());
        this.txt_year.setText(String.valueOf(media.getYear()));
        this.img_medium.setImage(new Image(getClass().getResourceAsStream(String.format("/com/ags/vr/artwork/icons/%s.png", media.getMedium()))));
    }
}