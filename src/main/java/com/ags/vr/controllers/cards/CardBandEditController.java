package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;

import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.DBBand;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;
import com.ags.vr.utils.database.DBMedia;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import static com.ags.vr.utils.Connector.con;

public class CardBandEditController implements CardBase
{
    @FXML private HBox pane_base;
    @FXML private TitledPane popUp;
    @FXML private TextField bandDisplay;

    // Previous card
    private CardMediaViewController card_base;
    private Media media;
    private String band = "";

    @FXML
    void initialize()
    {
        this.setVisible(false);
    }

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
    public void setMediaCard(CardMediaViewController controller)
    {
        this.card_base = controller;
    }

    @Override
    public void setVisible(boolean condition)
    {
        this.pane_base.setVisible(condition);
        if (condition)
        {
            card_base.setPageBlockVisibility(true);
            band = media.getBand();
            popUp.setText(band);
            bandDisplay.setText(band);
        }
    }

    @Override
    public void setMedia(Media media) {this.media = media;}

    /**
     * Renames the current medias band.
     * If the user tries to rename a band that is not in the database, the band is added to the system.
     */
    @FXML
    void renameMedia()
    {
        band = bandDisplay.getText();
        //the new band is not already in the system
        if(!DBBand.Contains(band))
        {
            //user conformation
            boolean bool = Graphical.ConfirmationPopup("Band is not in system",
                    "\"" + band + "\" is not in system. Would you like to add it to the system and rename the selected band?");

            if(bool)
            {
                //insert the band
                DBBand.Insert(band);
                //update
                renameMediaHelper();
            }
        }
        else
        {
            //update the band
            renameMediaHelper();
        }
        popUp.setText(band);
    }

    /**
     * Renames the current medias band system-wide. Meaning that if media "A" and media "B"
     * both have the band "Example Band" as their band and the user renames A's band to "New Example".
     * Both media A and B will have their bands set to "New Example".
     */
    @FXML
    void renameSystem()
    {
        String newBand = bandDisplay.getText();;
        if(!DBBand.Contains(newBand))
        {
            //user conformation
            boolean bool = Graphical.ConfirmationPopup("Band is not in system",
                    "\"" + newBand + "\" is not in system. Would you like to add it to the system and rename the selected band?");

            if(bool)
            {
                bool = Graphical.ConfirmationPopup("System Rename", "Are you sure you want to rename the current band name to "
                        + newBand + " throughout the entire system?");

                if(bool)
                {
                    DBBand.Insert(newBand);
                    renameSystemHelper(newBand);
                }
            }
        }
        else
        {
            //user conformation
            boolean bool = Graphical.ConfirmationPopup("System Rename", "Are you sure you want to rename the current band name to "
                    + newBand + " throughout the entire system?");
            if(bool)
                renameSystemHelper(newBand);
        }
    }

    /**
     * Helper function for renameMedia()
     */
    private void renameMediaHelper()
    {
        //new media object with updated band
        Media newMedia = new Media(media.getTitle(), media.getMedium(), media.getFormat(), media.getYear(), band);
        //update
        DBMedia.Update(newMedia, media, DBGenreLinker.getGenres(media));
        media = newMedia;
        //inform user
        Graphical.InfoPopup("Band Rename Successful", "The band was successfully renamed to " + band + ".");
    }

    /**
     * Helper function for renameSystem()
     * @param newBand
     */
    private void renameSystemHelper(String newBand)
    {
        try
        {
            //getGenre media with the old band
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM media WHERE band_id=?");
            stmt.setInt(1, Hash.StringHash(band));
            ResultSet rs = stmt.executeQuery();

            //update all media objects
            while(rs.next())
            {
                Media oldMedia = new Media(rs);
                Media newMedia = new Media(oldMedia.getTitle(), oldMedia.getMedium(), oldMedia.getFormat(), oldMedia.getYear(), newBand);
                DBMedia.Update(newMedia, oldMedia, DBGenreLinker.getGenres(oldMedia));
            }


            //delete old band
            DBBand.Delete(band);

            //inform user of success
            Graphical.InfoPopup("Band Renamed", "Band successfully renamed system wide");
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("System Rename Error", e.getMessage());
        }
    }


}
