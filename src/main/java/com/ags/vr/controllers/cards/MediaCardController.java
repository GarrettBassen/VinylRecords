package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.PageBlockController;
import com.ags.vr.objects.Media;

import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;

import javafx.fxml.FXML;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MediaCardController
{
    // Display variables
    @FXML private HBox pane_base;
    @FXML private TextArea ta_genres;
    @FXML private Text txt_band;
    @FXML private Text txt_title;
    @FXML private ImageView img_medium;

    // Reference variables
    private Media media;
    private PageBlockController pageBlockController;
    private MediaEditController mediaEditController;
    private GenreEditController genreEditController;
    private BandEditController bandEditController;

    /**
     * Closes popup card by setting it to invisible.
     */
    @FXML
    void Close()
    {
        this.setVisible(false);
        this.setPageBlockVisibility(false);
    }

    // Opens page to edit media
    @FXML
    void EditMedia()
    {
        setVisible(false);
        mediaEditController.setMedia(this.media);
        mediaEditController.setVisible(true);
    }

    @FXML
    void EditGenre()
    {
        setVisible(false);
        genreEditController.setMedia(this.media);
        genreEditController.setVisible(true);
    }

    @FXML
    void EditBand()
    {
        setVisible(false);
        bandEditController.setMedia(this.media);
        bandEditController.setVisible(true);
    }

    /**
     * Sets media edit controller.
     * @param controller MediaEditController object
     */
    public void setMediaEditController(MediaEditController controller)
    {
        this.mediaEditController = controller;
    }

    /**
     * Sets genre edit controller.
     * @param controller GenreEditController object
     */
    public void setGenreEditController(GenreEditController controller)
    {
        this.genreEditController = controller;
    }

    /**
     * Sets band edit controller.
     * @param controller BandEditController object
     */
    public void setBandEditController(BandEditController controller)
    {
        this.bandEditController = controller;
    }

    /**
     * Sets popup card visibility.
     * @param condition True for visible; False for invisible
     */
    public void setVisible(boolean condition)
    {
        this.pane_base.setVisible(condition);
        if (condition) { this.setPageBlockVisibility(true); }
    }

    /**
     * Sets page blocker visibility.
     * @param condition True make page blocker visible; False make page blocker invisible
     */
    public void setPageBlockVisibility(boolean condition)
    {
        this.pageBlockController.setVisibility(condition);
    }

    /**
     * Sets media to be displayed and populates display data.
     * @param media Media object
     */
    public void setMedia(Media media)
    {
        ClearDisplay();
        this.media = media;
        setDisplay();
    }

    /**
     * Sets page block controller.
     * @param controller PageBlockController
     */
    public void setPageBlock(PageBlockController controller)
    {
        this.pageBlockController = controller;
    }

    /**
     * Sets display data using Media object given in setMedia(Media).
     */
    private void setDisplay()
    {
        // Sets title and band
        this.txt_title.setText(this.media.getTitle());
        this.txt_band.setText("By: " + this.media.getBand());

        // Sets genres
        String[] genres = DBGenre.getName(DBGenreLinker.getGenres(media));
        for (String genre : genres)
        {
            ta_genres.appendText(String.format("%s\n", genre));
        }

        // Set medium image
        this.img_medium.setImage(new Image(getClass().getResourceAsStream(String.format("/com/ags/vr/artwork/icons/%s.png", media.getMedium()))));
    }

    /**
     * Clears display variables.
     */
    private void ClearDisplay()
    {
        this.txt_title.setText("");
        this.txt_band.setText("");
        this.ta_genres.clear();
    }
}
