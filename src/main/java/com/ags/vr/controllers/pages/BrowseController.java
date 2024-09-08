package com.ags.vr.controllers.pages;

import com.ags.vr.controllers.cards.BandEditController;
import com.ags.vr.controllers.cards.GenreEditController;
import com.ags.vr.controllers.cards.MediaEditController;
import com.ags.vr.controllers.utils.ContentPaneController;
import com.ags.vr.controllers.cards.MediaCardController;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
    @FXML private AnchorPane pane_base;
    @FXML private VBox pane_content;

    // Popup card
    private FXMLLoader card_media;
    private MediaCardController card_mediaController;

    // Edit media card
    private FXMLLoader card_editMedia;
    private MediaEditController card_editMediaController;

    // Edit genre card
    private FXMLLoader card_editGenre;
    private GenreEditController card_editGenreController;

    // Edit band card
    private FXMLLoader card_editBand;
    private BandEditController card_editBandController;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                      SEARCH FILTER VARIABLES                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    // Title
    @FXML private TextField txt_search;

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

    //used with slim Results to avoid unnecessary slimming
    private int searchCase = 0;

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                       INITIALIZATION METHODS                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @FXML
    void initialize()
    {
        pane_content.getChildren().clear();
        sp_year_min.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1400,Year.now().getValue()));
        sp_year_max.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1400,Year.now().getValue()));
        ClearYearInput();

        InitializePages();
    }

    private void InitializePages()
    {
        // Initialize card popup
        try
        {
            // Get cards
            card_media = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_media.fxml"));
            card_editMedia = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_media_edit.fxml"));
            card_editGenre = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_genre_edit.fxml"));
            card_editBand = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_band_edit.fxml"));

            // Set cards in scene
            pane_base.getChildren().add(card_media.load());
            pane_base.getChildren().add(card_editMedia.load());
            pane_base.getChildren().add(card_editGenre.load());
            pane_base.getChildren().add(card_editBand.load());

            // Get card controllers
            card_mediaController = card_media.getController();
            card_editMediaController = card_editMedia.getController();
            card_editGenreController = card_editGenre.getController();
            card_editBandController = card_editBand.getController();

            // Make cards invisible
            card_mediaController.setVisible(false);
            card_editMediaController.setVisible(false);
            card_editGenreController.setVisible(false);
            card_editBandController.setVisible(false);
        }
        catch (IOException e)
        {
            Graphical.ErrorPopup("Initialization Error",
                    "Error loading popup card in InitializePages() | BrowseController.java");
        }

        // Set edit controller to open edit screen
        card_mediaController.setMediaEditController(card_editMediaController);
        card_mediaController.setGenreEditController(card_editGenreController);
        card_mediaController.setBandEditController(card_editBandController);
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
        try
        {
            // Clear current data, get new data, and display
            pane_content.getChildren().clear();
            Media[] media = sqlSearch();
            media = slimResults(media);
            AddContentPane(card_mediaController, media);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
        }
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
                searchCase = 0;
                return TitleQuery();
            }
            else if (!ta_bands.getText().isBlank())
            {
                searchCase = 1;
                return BandQuery();
            }
            else if (!ta_genres.getText().isBlank())
            {
                searchCase = 2;
                return GenreQuery();
            }
            else if (medium[0] != null)
            {
                searchCase = 3;
                return MediumQuery();
            }
            else if (format[0] != null)
            {
                searchCase = 4;
                return FormatQuery();
            }
            else if (YearInputsChanged())
            {
                searchCase = 5;
                return YearQuery();
            }
            else
            {
                searchCase = 6;
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

    /**
     * Returns Media objects from specified genres
     * @return Media[] data
     * @throws SQLException Exception
     */
    private Media[] GenreQuery() throws SQLException
    {
        PreparedStatement statement = con.prepareStatement(
                "SELECT * FROM media LEFT OUTER JOIN genre_linker ON media.media_id " +
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
    //TODO TEST
    private Media[] slimResults(Media[] media) throws SQLException
    {

        //TODO MAKE FASTER
        if(!ta_bands.getText().isBlank() && searchCase != 1)
        {
            Media[] search = BandQuery();

            //compare the band search to the current media list
            //if the titles mach, keep the media
            //otherwise set the media to null

            for(int i = 0; i < media.length; i++)
            {
                boolean found = false;
                for(int j = 0; j < search.length; j++)
                {
                    if(search[j].getTitle().equals(media[i].getTitle()))
                    {
                        found = true;
                    }
                }
                if(!found)
                {
                    media[i] = null;
                }
            }

            //list of correct size
            LinkedList<Media> slimMedia = new LinkedList<>();

            //fill list
            for(int i = 0; i < media.length; i++)
            {
                if(media[i] != null)
                {
                    slimMedia.addLast(media[i]);
                }
            }
            media = slimMedia.toArray(new Media[0]);
        }

        //TODO MAKE FASTER
        if(!ta_genres.getText().isBlank() && searchCase != 2)
        {
            Media[] search = GenreQuery();

            //compare the genre search to the current media list
            //if the titles mach, keep the media
            //otherwise drop the media to null
            for(int i = 0; i < media.length; ++i)
            {
                boolean found = false;
                for(int j = 0; j < search.length; ++j)
                {
                    //check
                    if(media[i].getTitle().equals(search[j].getTitle()))
                    {
                        found = true;
                    }
                }

                if(!found)
                {
                    media[i] = null;
                }
            }

            //list of correct size
            LinkedList<Media> slimMedia = new LinkedList<>();

            //fill list
            for(int i = 0; i < media.length; i++)
            {
                if(media[i] != null)
                {
                    slimMedia.addLast(media[i]);
                }
            }
            media = slimMedia.toArray(new Media[0]);
        }

        if(medium[0] != null && searchCase != 3)
        {
            //filter for each type of medium
            LinkedList<Media> mediaList = new LinkedList<>();
            for(int i = 0; i < medium.length; i++)
            {
                //slim the current medium
                if(!medium[i].isBlank())
                {
                    for(int j = 0; j < media.length; j++)
                    {
                        if(media[j].getMedium().equals(medium[i]))
                        {
                            mediaList.addLast(media[j]);
                        }
                    }
                }
            }

            //slimed media array
            media = mediaList.toArray(new Media[0]);
        }

        if(format[0] != null && searchCase != 4)
        {
            LinkedList<Media> mediaList = new LinkedList<>();
            for(int i = 0; i < format.length; i++)
            {
                if(!format[i].isBlank())
                {
                    for(int j = 0; j < media.length; j++)
                    {
                        if(media[j].getFormat().equals(format[i]))
                        {
                            mediaList.addLast(media[j]);
                        }
                    }
                }
            }

            //slimmed media list
            media = mediaList.toArray(new Media[0]);
        }

        if(YearInputsChanged() && searchCase != 5)
        {
            //for each media object
            //if the media does not fall within the date range, set the object to null
            for(int i = 0; i < media.length; i++)
            {
                if(media[i].getYear() < sp_year_min.getValue() || media[i].getYear() > sp_year_max.getValue())
                {
                    media[i] = null;
                }
            }

            //list of correct size
            LinkedList<Media> slimMedia = new LinkedList<>();

            //fill list
            for(int i = 0; i < media.length; i++)
            {
                if(media[i] != null)
                {
                    slimMedia.addLast(media[i]);
                }
            }
            media = slimMedia.toArray(new Media[0]);
        }

        return media;
    }


    /**
     * Creates content panes from media object(s) which is then added to the vbox display region.
     * @param media Media object(s)
     */
    private void AddContentPane(MediaCardController cardPopup, Media... media)
    {
        for (Media m : media) {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/utils/pane_content.fxml"));
                pane_content.getChildren().add(loader.load());
                ContentPaneController controller = loader.getController();
                controller.setData(cardPopup, m);
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
