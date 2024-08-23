package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBBands;
import com.ags.vr.utils.database.DBMedia;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import java.time.Year;

public class AddController
{
    // Variables
    @FXML
    private CheckBox cb_CD;
    @FXML
    private CheckBox cb_DLP;
    @FXML
    private CheckBox cb_EP;
    @FXML
    private CheckBox cb_LP;
    @FXML
    private CheckBox cb_cassette;
    @FXML
    private CheckBox cb_single;
    @FXML
    private CheckBox cb_vinyl;
    @FXML
    private TextArea ta_genres;
    @FXML
    private TextField tf_band;
    @FXML
    private TextField tf_title;
    @FXML
    private TextField tf_year;

    // Input validation arrays
    private CheckBox[] array_medium;
    private CheckBox[] array_format;

    /**
     * Initializes arrays for use in input validation.
     */
    @FXML
    private void initialize()
    {
        array_medium = new CheckBox[] { cb_vinyl, cb_CD, cb_cassette };
        array_format = new CheckBox[] { cb_single, cb_EP, cb_LP, cb_DLP };
    }

    /**
     * Adds media when button is pressed.
     * @param event Event
     */
    @FXML
    void AddMedia(ActionEvent event)
    {
        // Validate all text inputs
        if (!TextInputValidation()) return;

        // Validate checkbox sections
        if (!CheckboxValidation(array_medium,"media type"))     return;
        if (!CheckboxValidation(array_format,"album format"))   return;

        Media media = CreateMedia();

        // Create band, media, and inventory table
        if (media != null)
        {
            DBBands.containsBand(media.getBand(), true);
            DBMedia.insertMedia(media);
            // TODO ADD INVENTORY TABLE
        }
    }


    private Media CreateMedia()
    {
        Media media = new Media();
        media.setTitle(tf_title.getText());
        media.setYear(Short.parseShort(tf_year.getText()));
        media.setBand(tf_band.getText());

        // Set medium
        if      (cb_vinyl.isSelected())     { media.setMedium("vinyl"); }
        else if (cb_CD.isSelected())        { media.setMedium("CD"); }
        else if (cb_cassette.isSelected())  { media.setMedium("cassette"); }

        // Set format
        if      (cb_single.isSelected())    { media.setFormat("single"); }
        else if (cb_EP.isSelected())        { media.setFormat("EP"); }
        else if (cb_LP.isSelected())        { media.setFormat("LP"); }
        else if (cb_DLP.isSelected())       { media.setFormat("DLP"); }

        // Check if media exists in database already
        if (DBMedia.ContainsMedia(media))
        {
            boolean GotoPage = Graphical.ConfirmationPopup("Media Already Exists",String.format(
                    "%s by %s is already in your system. Would you like to go to the inventory page to" +
                            "modify this item or its stock?",tf_title.getText(),tf_band.getText())
            );

            if (GotoPage)
            {
                // TODO BRING USER TO INVENTORY PAGE FOR THE MEDIA ENTRY
                System.out.println("FIX ME TextInputValidation() AddController.java");
            }

            return null;
        }

        return media;
    }

    /**
     * Validates all text input fields. If an error is found, a False boolean is returned and the error is
     * displayed to the user in an error popup.
     * @return True for valid inputs; false otherwise
     */
    private boolean TextInputValidation()
    {
        // Validate title and band name fields are not blank
        if (tf_title.getText().isBlank())
        {
            Graphical.ErrorPopup("Invalid Album Name","No album name given. Please add album name.");
            return false;
        }
        if (tf_band.getText().isBlank())
        {
            Graphical.ErrorPopup("Invalid Band Name","No band name given. Please add band name.");
            return false;
        }

        // Validates genres are populated
        if (ta_genres.getText().isBlank())
        {
            Graphical.ErrorPopup("Invalid Genres","No genres given. Please add genre(s).");
            return false;
        }

        // Ensure valid year input
        if (tf_year.getText().isBlank())
        {
            Graphical.ErrorPopup("No Album Year","No album release year given. Please add release year.");
            return false;
        }
        int year = Integer.parseInt(tf_year.getText());
        if (year < 1887 || year > Year.now().getValue())
        {
            Graphical.ErrorPopup("Invalid Year",String.format("The year '%s' is invalid.",year));
            return false;
        }

        return true;
    }

    /**
     * Ensures that one and only one of the checkboxes in the given array are activated. If an error is found,
     * a False boolean is returned and the error is displayed to the user in an error popup.
     * @param array Checkbox array to validate
     * @param section Section title for error message
     * @return True for valid inputs; false otherwise.
     */
    private boolean CheckboxValidation(CheckBox[] array, String section)
    {
        int count = 0;

        // Count activated checkboxes
        for (CheckBox cb : array)
        {
            count += cb.isSelected()? 1 : 0;
        }

        if (count == 0)
        {
            Graphical.ErrorPopup("No Selection",
                    String.format("No %s is selected. Please review selections.",section));
            return false;
        }
        else if (count > 1)
        {
            Graphical.ErrorPopup("Too Many Selections",
                    String.format("More than one %s is selected. Please review selections.",section));
            return false;
        }

        return true;
    }
}
