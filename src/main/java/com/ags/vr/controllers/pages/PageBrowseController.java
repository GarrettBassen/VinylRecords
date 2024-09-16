package com.ags.vr.controllers.pages;

import com.ags.vr.controllers.cards.CardBandEditController;
import com.ags.vr.controllers.cards.CardGenreEditController;
import com.ags.vr.controllers.cards.CardMediaEditController;
import com.ags.vr.controllers.utils.ContentPaneController;
import com.ags.vr.controllers.cards.CardMediaViewController;
import com.ags.vr.controllers.utils.PageBlockController;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import com.ags.vr.utils.database.DBGenreLinker;
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
import java.util.*;

import static com.ags.vr.utils.Connector.con;

public class PageBrowseController
{
    // Panes
    @FXML private AnchorPane pane_base;
    @FXML private VBox pane_content;

    // Current page controller reference
    private PageBrowseController page_browseController;

    // Popup card controllers
    private CardMediaViewController card_mediaController;
    private CardMediaEditController card_editMediaController;
    private CardGenreEditController card_editGenreController;
    private CardBandEditController card_editBandController;

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

    // Filter helper variables
    private String[] medium = new String[4];
    private String[] format = new String[5];

    // Used with slim Results to avoid unnecessary slimming
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

        Arrays.fill(medium,"");
        Arrays.fill(format,"");

        InitializePages();
    }

    /**
     * Initializes card popups.
     */
    private void InitializePages()
    {
        // Initialize all cards
        try
        {
            // Get cards
            FXMLLoader card_block = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/utils/page_block.fxml"));
            FXMLLoader card_media = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_media_view.fxml"));
            FXMLLoader card_editMedia = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_media_edit.fxml"));
            FXMLLoader card_editGenre = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_genre_edit.fxml"));
            FXMLLoader card_editBand = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/cards/card_band_edit.fxml"));

            // Set cards in scene
            pane_base.getChildren().add(card_block.load());
            pane_base.getChildren().add(card_media.load());
            pane_base.getChildren().add(card_editMedia.load());
            pane_base.getChildren().add(card_editGenre.load());
            pane_base.getChildren().add(card_editBand.load());

            // Get card controllers
            PageBlockController page_blockController = card_block.getController();
            card_mediaController = card_media.getController();
            card_editMediaController = card_editMedia.getController();
            card_editGenreController = card_editGenre.getController();
            card_editBandController = card_editBand.getController();

            // Set base card controller reference for 'Previous Page' functionality
            card_editMediaController.setMediaCard(card_mediaController);
            card_editGenreController.setMediaCard(card_mediaController);
            card_editBandController.setMediaCard(card_mediaController);

            // Setup page blocker
            card_mediaController.setPageBlock(page_blockController);
        }
        catch (IOException e)
        {
            Graphical.ErrorPopup("Initialization Error", String.format(
                    "Error loading popup card in InitializePages() | BrowseController.java\n\nError:%s",
                    e.getMessage())
            );
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
            // Do nothing if year inputs are invalid
            if (!YearValidation()) { return; }

            // Clear current data, get new data, and display
            pane_content.getChildren().clear();
            Media[] media = sqlSearch();
            media = slimResults(media);
            AddContentPane(media);
            ClearParameters();
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
    /*                                            SEARCH METHODS                                                     */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Searches the database based on a hierarchy of inputs.
     * The title attribute is considered the most important followed by
     * band, genre, medium, format, and years.<br \><br \>
     * WARNING: Should be used with slim() to get correct user input.
     * @return Media[] filtered Media[] list
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
            else if (!medium[0].isBlank())
            {
                searchCase = 3;
                return MediumQuery();
            }
            else if (!format[0].isBlank())
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
                // All inputs left blank. Grab all data
                searchCase = 6;
                PreparedStatement statement = con.prepareStatement("SELECT * FROM media");
                ResultSet result = statement.executeQuery();
                return rsToMedia(result);
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
            return null;
        }
    }

    /**
     * Slims Media[] to only contain Media objects with specified filter parameters.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     * @throws SQLException Exception
     */
    private Media[] slimResults(Media[] mediaList) throws SQLException
    {
        // Slim media to only specified bands
        if(!ta_bands.getText().isBlank() && searchCase != 1)
        {
            mediaList = FilterBands(mediaList);
        }

        // Slim media to only specified genres
        if(!ta_genres.getText().isBlank() && searchCase != 2)
        {
            mediaList = FilterGenres(mediaList);
        }

        // Slim down to specified mediums
        if(!medium[0].isEmpty() && searchCase != 3)
        {
            mediaList = FilterMedium(mediaList);
        }

        // Slim media to only specified formats
        if(!format[0].isEmpty() && searchCase != 4)
        {
            mediaList = FilterFormat(mediaList);
        }

        // Slim media to only specified year range
        if(YearInputsChanged() && searchCase != 5)
        {
            mediaList = FilterYearRange(mediaList);
        }

        return mediaList;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                            FILTER METHODS                                                     */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Filters media list to contain only Media objects with specified bands.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     */
    private Media[] FilterBands(Media[] mediaList)
    {
        // Setup hashed band filter HashSet for slimming medias
        HashSet<Integer> bands = new HashSet<>();
        for (CharSequence bandInput : ta_bands.getParagraphs())
        {
            bands.add(Hash.StringHash(bandInput.toString()));
        }

        // If media band exists in filter set, add media to slimmed media array
        LinkedList<Media> slimmedMedia = new LinkedList<>();
        for (Media media : mediaList)
        {
            if (bands.contains(Hash.StringHash(media.getBand())))
            {
                slimmedMedia.addLast(media);
            }
        }

        return slimmedMedia.toArray(new Media[0]);
    }

    /**
     * Filters media list to contain only Media objects that contain at least one of the specified genre.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     */
    private Media[] FilterGenres(Media[] mediaList)
    {
        // Setup hashed genre filter HashSet for slimming medias
        HashSet<Integer> genres = new HashSet<>();
        for (CharSequence genre : ta_genres.getParagraphs())
        {
            genres.add(Hash.StringHash(genre.toString()));
        }

        // If media contains a genre in the filter set, add media to slimmed media array
        LinkedList<Media> slimmedMedia = new LinkedList<>();
        for (Media media : mediaList)
        {
            for (int mediaGenre : DBGenreLinker.getGenres(media))
            {
                if (genres.contains(mediaGenre))
                {
                    slimmedMedia.addLast(media);
                }
            }
        }

        return slimmedMedia.toArray(new Media[0]);
    }

    /**
     * Filters media list to contain only Media objects of the specified medium types.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     */
    private Media[] FilterMedium(Media[] mediaList)
    {
        // Setup medium filter HashSet for slimming medias
        HashSet<String> mediums = new HashSet<>();
        for (int i = 1; i < medium.length; ++i)
        {
            if (!medium[i].isBlank())
            {
                mediums.add(medium[i]);
            }
        }

        // Slim media to only specified mediums
        LinkedList<Media> slimmedMedia = new LinkedList<>();
        for (Media media : mediaList)
        {
            if (mediums.contains(media.getMedium()))
            {
                slimmedMedia.addLast(media);
            }
        }

        return slimmedMedia.toArray(new Media[0]);
    }

    /**
     * Filters media list to contain only Media objects of the specified format.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     */
    private Media[] FilterFormat(Media[] mediaList)
    {
        // Setup format filter HashSet for slimming medias
        HashSet<String> formats = new HashSet<>();
        for (int i = 1; i < format.length; ++i)
        {
            if (!format[i].isBlank())
            {
                formats.add(format[i]);
            }
        }

        // Slim media to only specified formats
        LinkedList<Media> slimmedMedia = new LinkedList<>();
        for (Media media : mediaList)
        {
            if (formats.contains(media.getFormat()))
            {
                slimmedMedia.addLast(media);
            }
        }

        //slimmed media list
        return slimmedMedia.toArray(new Media[0]);
    }

    /**
     * Filters media list to contain only Media objects within specified year range.
     * @param mediaList Media[] list
     * @return Filtered Media[] list
     */
    private Media[] FilterYearRange(Media[] mediaList)
    {
        // Slim media to specified year range
        LinkedList<Media> slimmedMedia = new LinkedList<>();
        for (Media media : mediaList)
        {
            if(media.getYear() <= sp_year_max.getValue() && media.getYear() >= sp_year_min.getValue())
            {
                slimmedMedia.addLast(media);
            }
        }

        return slimmedMedia.toArray(new Media[0]);
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

    /**
     * Creates content panes from media object(s) which is then added to the vbox display region.
     * @param media Media object(s)
     */
    private void AddContentPane(Media... media)
    {
        for (Media m : media) {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/utils/pane_content.fxml"));
                pane_content.getChildren().add(loader.load());
                ContentPaneController controller = loader.getController();
                controller.setData(card_mediaController, m);
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

    /**
     * Clears all search parameters and variables.
     */
    private void ClearParameters()
    {
        this.searchCase = 0;
        Arrays.fill(medium,"");
        Arrays.fill(format,"");
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

    /**
     * Ensures the year range is searchable and that the minimum range is less than or equal to the maximum range.
     * @return True if range is valid; false otherwise
     */
    private boolean YearValidation()
    {
        if ((sp_year_min.getValue() != null && sp_year_max.getValue() != null)
                && (sp_year_min.getValue() > sp_year_max.getValue()))
        {
            Graphical.ErrorPopup("Invalid Year Input", String.format(
                    "Please check the year input boxes. Your current year range is unsearchable '%s to %s'",
                    sp_year_min.getValue(), sp_year_max.getValue()
            ));
            return false;
        }

        return true;
    }
}
