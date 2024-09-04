package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.time.Year;
import java.util.Arrays;
import java.util.LinkedList;

import static com.ags.vr.utils.Connector.con;

public class BrowseController
{
    // Variables
    @FXML private TextField txt_search;
    @FXML private VBox pane_content;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                      SEARCH FILTER VARIABLES                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // Medium
    @FXML private RadioButton rb_vinyl;
    @FXML private RadioButton rb_CD;
    @FXML private RadioButton rb_cassette;

    // Genre
    @FXML private TextArea ta_genres;

    // Band
    @FXML private TextArea ta_bands;

    // Format
    @FXML private RadioButton rb_single;
    @FXML private RadioButton rb_EP;
    @FXML private RadioButton rb_LP;
    @FXML private RadioButton rb_DLP;

    // Year
    @FXML private Spinner<Integer> sp_year_min;
    @FXML private Spinner<Integer> sp_year_max;

    private String[] medium = new String[4];
    private String[] format = new String[5];


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                       INITIALIZATION METHODS                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    void initialize()
    {
        sp_year_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1400,Year.now().getValue()));
        sp_year_max.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1400,Year.now().getValue()));
        ClearYearInput();
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            EVENT METHODS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Executes when key is typed with text field activated.
     * @param event Event
     */
    @FXML
    void InputAction(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            Search();
        }
    }

    /**
     * Executes when search button is pressed.
     */
    @FXML
    void Search()
    {
        // Clear current data, get new data, and display
        pane_content.getChildren().clear();
        Media[] media = sqlSearch();
        //media = slimResults(media); TODO IMPLEMENT AND UNCOMMENT
        AddContentPane(media);
    }

    /**
     * Clears values of spinner year input boxes.
     */
    @FXML
    void ClearYearInput()
    {
        sp_year_min.getValueFactory().setValue(Integer.MIN_VALUE);
        sp_year_max.getValueFactory().setValue(Integer.MAX_VALUE);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            DATABASE METHODS                                                   */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Searches the database based on a hierarchy of inputs.
     * The title attribute is considered the most important followed by
     * band, genre, medium, format, and years.
     *
     * Should be used with slim() to get correct user input.
     * @return Media[] data
     */
    private Media[] sqlSearch()
    {
        // Order of importance
        // Title, band, genre, medium, format, year range

        try
        {
            if (!txt_search.getText().isBlank())
            {
                return TitleQuery();
            }
            else if (!ta_bands.getText().isBlank())
            {
                return BandQuery();
            }
            else if (!ta_genres.getText().isBlank())
            {
                return GenreQuery();
            }
            else if (medium[0] != null)
            {
                return MediumQuery();
            }
            else if (format[0] != null)
            {
                return FormatQuery();
            }
            else if (YearInputsChanged())
            {
                return YearQuery();
            }
            else
            {
                // All inputs left blank. Grab all data
                PreparedStatement statement = con.prepareStatement("SELECT * FROM media");
                ResultSet result = statement.executeQuery();
                return rsToMedia(result);
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
        }
        return null;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            DATABASE METHODS                                                   */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Returns Media objects with titles closely matching the input String.
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] TitleQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE title LIKE ?");
        statement.setString(1, "%" + txt_search.getText() + "%");
        ResultSet result = statement.executeQuery();

        Media[] media = rsToMedia(result);

        statement.close();
        return media;
    }

    /**
     * Returns Media objects from specified bands.
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] BandQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE band_id = ?");
        ObservableList<CharSequence> bands = ta_bands.getParagraphs();
        LinkedList<Media> mediaList = new LinkedList<>();

        // Get all media objects for bands given
        for (CharSequence band : bands)
        {
            if (band.isEmpty()) { continue; }

            statement.setInt(1, Hash.StringHash(band.toString()));
            ResultSet result = statement.executeQuery();

            while (result.next())
            {
                mediaList.addLast(new Media(result));
            }
        }

        statement.close();
        return mediaList.toArray(new Media[0]);
    }

    // TODO FIX
    private Media[] GenreQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement(
                "SELECT * FROM media LEFT (OUTER) JOIN genre_linker ON media.media_id " +
                        "= genre_linker.media_id WHERE genre_linker.genre_id = ?"
        );
        ObservableList<CharSequence> genres = ta_genres.getParagraphs();
        LinkedList<Media> mediaList = new LinkedList<>();

        // Get all media objects for genres given
        for (CharSequence genre : genres)
        {
            if (genre.isEmpty()) { continue; }

            statement.setInt(1, Hash.StringHash(genre.toString()));
            ResultSet result = statement.executeQuery();

            while (result.next())
            {
                mediaList.addLast(new Media(result));
            }
        }

        statement.close();
        return mediaList.toArray(new Media[0]);
    }

    /**
     * Returns Media objects of the selected medium format.
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] MediumQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE medium = ?");
        LinkedList<Media> mediaList = new LinkedList<>();

        // Get all media with selected medium
        for (int i = 1; i < medium.length; ++i)
        {
            if (!medium[i].isBlank())
            {
                statement.setString(1, medium[i]);
                ResultSet result = statement.executeQuery();

                // Add media object
                while (result.next())
                {
                    mediaList.addLast(new Media(result));
                }
            }
        }

        statement.close();
        return mediaList.toArray(new Media[0]);
    }

    /**
     * Returns Media objects of the selected album format.
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] FormatQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE album_format = ?");
        LinkedList<Media> mediaList = new LinkedList<>();

        // Get all media with selected format
        for (int i = 1; i < format.length; ++i)
        {
            if (!format[i].isBlank())
            {
                statement.setString(1, format[i]);
                ResultSet result = statement.executeQuery();

                // Add media object
                while (result.next())
                {
                    mediaList.addLast(new Media(result));
                }
            }
        }

        statement.close();
        return mediaList.toArray(new Media[0]);
    }

    /**
     * Returns all media within the given year range.
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] YearQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE (year >= ?) AND (year <= ?)");
        statement.setInt(1, sp_year_min.getValue() != null ? sp_year_min.getValue() : Integer.MIN_VALUE);
        statement.setInt(2, sp_year_max.getValue() != null ? sp_year_max.getValue() : Integer.MAX_VALUE);
        ResultSet result = statement.executeQuery();

        Media[] media = rsToMedia(result);

        statement.close();
        return media;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            HELPER METHODS                                                     */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // TODO FINISH AND COMMENT
    private Media[] slimResults(Media[] media)
    {
        return null;
    }

    /**
     * Creates content panes from media object(s) which is then added to the vbox display region.
     * @param media Media object(s)
     */
    private void AddContentPane(Media... media)
    {
        for (Media m : media) {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ags/vr/pages/pane_content.fxml"));
                pane_content.getChildren().add(loader.load());
                ContentPaneController controller = loader.getController();
                controller.setData(m);
            }
            catch (IOException e)
            {
                Graphical.ErrorPopup("Error displaying content", String.format(
                        "Could not display media in AddContentPane(Media...) | BrowseController.java\n\n%s",
                        e.getMessage()
                ));
            }
        }
    }

    /**
     * Gets all media objects from n many result sets.
     * @param sets n many result sets
     * @return Media[] data
     */
    private Media[] rsToMedia(ResultSet... sets) throws SQLException
    {
        LinkedList<Media> mediaList = new LinkedList<>();

        for (ResultSet set : sets)
        {
            while (set != null && set.next())
            {
                mediaList.addLast(new Media(set));
            }
        }

        return mediaList.toArray(new Media[0]);
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                           VALIDATION METHODS                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Assigns valid medium name to respective medium array indices for later use in SQL query.
     */
    @FXML
    private void mediumControl()
    {
        // Clear medium
        Arrays.fill(medium,"");

        // Assign each selected value with valid medium string
        if (rb_vinyl.isSelected())
        {
            medium[0] = "True";
            medium[1] = "vinyl";
        }
        if (rb_CD.isSelected())
        {
            medium[0] = "True";
            medium[2] = "CD";
        }
        if (rb_cassette.isSelected())
        {
            medium[0] = "True";
            medium[3] = "cassette";
        }
    }

    /**
     * Indicates which format is selected.
     */
    @FXML
    private void formatControl() {

        // Clear format
        Arrays.fill(format,"");

        // Assign each selected value with valid format string
        if (rb_single.isSelected())
        {
            format[0] = "True";
            format[1] = "Single";
        }
        if (rb_EP.isSelected())
        {
            format[0] = "True";
            format[2] = "EP";
        }
        if (rb_LP.isSelected())
        {
            format[0] = "True";
            format[3] = "LP";
        }
        if (rb_DLP.isSelected())
        {
            format[0] = "True";
            format[4] = "DLP";
        }
    }

    /**
     * Checks if year inputs are modified by user.
     * @return True if modified; false otherwise
     */
    private boolean YearInputsChanged()
    {
        return (
                (sp_year_min.getValue() != null && sp_year_min.getValue() != 1400) ||
                (sp_year_max.getValue() != null && sp_year_max.getValue() != Year.now().getValue())
        );
    }
}
