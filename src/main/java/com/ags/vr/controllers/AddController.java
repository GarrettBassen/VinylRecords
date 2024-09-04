package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.Year;

/**
 * Manages the add page where the user inputs all data for the media object and then may add it to the database where
 * an inventory is created and all associated data such as genres and bands are linked to the media. Proper error
 * checking and error displays are utilized to ensure the user may not insert improperly formatted data and that they
 * know when an error occurs and how to fix it.
 */
public class AddController
{
    // Medium variables
    @FXML private CheckBox cb_vinyl;
    @FXML private CheckBox cb_CD;
    @FXML private CheckBox cb_cassette;

    // Album format variables
    @FXML private CheckBox cb_DLP;
    @FXML private CheckBox cb_EP;
    @FXML private CheckBox cb_LP;
    @FXML private CheckBox cb_single;

    // Text variables
    @FXML private TextArea ta_genres;
    @FXML private TextField tf_band;
    @FXML private TextField tf_title;
    @FXML private TextField tf_year;

    // Spinners are integers treated as bytes
    @FXML private Spinner<Integer> sp_front_good;
    @FXML private Spinner<Integer> sp_front_fair;
    @FXML private Spinner<Integer> sp_front_poor;
    @FXML private Spinner<Integer> sp_back_good;
    @FXML private Spinner<Integer> sp_back_fair;
    @FXML private Spinner<Integer> sp_back_poor;

    // Input validation arrays
    private Spinner<Integer>[] array_inventory;
    private CheckBox[] array_medium;
    private CheckBox[] array_format;


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                       INITIALIZATION METHODS                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Initializes arrays for use in input validation.
     */
    @FXML
    private void initialize()
    {
        SpinnerInitialize();
        array_medium = new CheckBox[] { cb_vinyl, cb_CD, cb_cassette };
        array_format = new CheckBox[] { cb_single, cb_EP, cb_LP, cb_DLP };
    }

    /**
     * Initializes spinners and sets value range.
     */
    private void SpinnerInitialize()
    {
        array_inventory = new Spinner[] {
                sp_front_good, sp_front_fair, sp_front_poor,
                sp_back_good, sp_back_fair, sp_back_poor
        };

        // Initialize spinners
        for (Spinner<Integer> sp : array_inventory)
        {
            sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,127));
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            EVENT METHODS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Upon button press, validates all inputs and adds media if no errors are found. If the band or genres are not
     * found in the database, they are added in their respective tables. An inventory is created for the media using
     * its media ID, and the genres are linked to the media in the genre linker.
     */
    @FXML
    private void AddMedia()
    {
        // Validate all inputs and terminate for any errors
        if (!TextInputValidation())                                    { return; }
        if (!CheckboxValidation(array_medium,"media type"))     { return; }
        if (!CheckboxValidation(array_format,"album format"))   { return; }

        // Create media from data, warn user if media already exists within database and terminate if so
        Media media = CreateMedia();
        if (DBMedia.Contains(media))
        {
            Graphical.InfoPopup("Media Already Exists",String.format(
                    "'%s' by '%s' is already in your system. To modify this album, please go to browse " +
                            "and select the 'Edit' button.",tf_title.getText(),tf_band.getText())
            );
            return;
        }

        // Add band to database if it does not exist
        if (!DBBand.Contains(media.getBand()))
        {
            DBBand.Insert(media.getBand());
        }

        // Insert media, inventory, and link genres
        DBMedia.Insert(media);
        DBInventory.Insert(new Stock(media.getID(),getStockData()));
        LinkGenres(media.getID());

        // Clear inputs and inform user about success
        ClearInputs();
        Graphical.ConfirmationPopup("Success Adding Media",String.format(
                "'%s' by '%s' was successfully added to your inventory",
                media.getTitle(), media.getBand()
        ));
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                           VALIDATION METHODS                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Ensures that one and only one of the checkboxes in the given array are activated. If an error is found,
     * a False boolean is returned and the issue is displayed to the user in an error popup.
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

    /**
     * Validates all text input fields. If an error is found with any input, a False boolean is returned and the
     * specific issue is displayed to the user in an error popup.
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
        String year = tf_year.getText().strip();
        if (year.isBlank())
        {
            Graphical.ErrorPopup("No Album Year","No album release year given. Please add release year.");
            return false;
        }
        else if (!year.matches("^\\d+$"))
        {
            Graphical.ErrorPopup("Text in Album Year Input",
                    "Non-numeric text was detected in release year input box. " +
                            "Please delete any white space or characters.");
            return false;
        }
        else if (Integer.parseInt(year) < 1400 || Integer.parseInt(year) > Year.now().getValue())
        {
            Graphical.ErrorPopup("Invalid Year",String.format(
                    "The year '%s' is invalid.",Integer.parseInt(year))
            );
            return false;
        }

        return true;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              HELPER METHODS                                                   */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Creates media object from validated inputs and returns for further use.
     * @return Media object
     */
    private Media CreateMedia()
    {
        // Set text inputs
        Media media = new Media();
        media.setTitle(tf_title.getText().strip());
        media.setYear(Short.parseShort(tf_year.getText()));
        media.setBand(tf_band.getText().strip());

        // Set medium
        if      (cb_vinyl.isSelected())     { media.setMedium("vinyl"); }
        else if (cb_CD.isSelected())        { media.setMedium("CD"); }
        else if (cb_cassette.isSelected())  { media.setMedium("cassette"); }

        // Set format
        if      (cb_single.isSelected())    { media.setFormat("single"); }
        else if (cb_EP.isSelected())        { media.setFormat("EP"); }
        else if (cb_LP.isSelected())        { media.setFormat("LP"); }
        else if (cb_DLP.isSelected())       { media.setFormat("DLP"); }

        return media;
    }

    /**
     * Links all given genres from the genre text are to mediaID in the genre_linker table. Adds hashed genres to
     * genre table if they do not already exist; else, their ID is grabbed and used.
     * @param mediaID Media ID
     */
    private void LinkGenres(int mediaID)
    {
        // Gets each line in genres text area as a CharSequence
        ObservableList<CharSequence> genres = ta_genres.getParagraphs();

        // Loop through all genres and insert into genre linker
        for (CharSequence g : genres)
        {
            // Skip empty lines
            if (g.toString().isBlank()) { continue; }

            // Add genre if it does not exist
            if (!DBGenre.Contains(g.toString()))
            {
                DBGenre.Insert(g.toString().strip());
            }

            // Link genre to media
            DBGenreLinker.Insert(mediaID, Hash.StringHash(g.toString()));
        }
    }

    /**
     * Generates stock data array from inputs.
     * @return Byte[] stock data
     */
    private byte[] getStockData()
    {
        return new byte[]
                {
                        sp_front_good.getValue().byteValue(),
                        sp_front_fair.getValue().byteValue(),
                        sp_front_poor.getValue().byteValue(),
                        sp_back_good.getValue().byteValue(),
                        sp_back_fair.getValue().byteValue(),
                        sp_back_poor.getValue().byteValue()
                };
    }

    private void ClearInputs()
    {
        // Clear checkboxes
        for (CheckBox cb : array_medium)
        {
            cb.setSelected(false);
        }
        for (CheckBox cb : array_format)
        {
            cb.setSelected(false);
        }

        // Clear inventory spinners
        for (Spinner<Integer> s : array_inventory)
        {
            s.getValueFactory().setValue(0);
        }

        // Clear text inputs
        tf_title.clear();
        tf_band.clear();
        tf_year.clear();
        ta_genres.clear();
    }
}
