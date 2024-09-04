package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;
import com.ags.vr.utils.database.DBMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.LinkedList;
import java.util.Stack;

import static com.ags.vr.utils.Connector.con;

public class BrowseController
{
    // Variables
    @FXML
    private TextField txt_search;

    //radio buttons
    @FXML
    private RadioButton vinylRB;

    @FXML
    private RadioButton cdRB;

    @FXML
    private RadioButton cassetteRB;

    @FXML
    private TextArea genreInput;

    @FXML
    private TextArea bandInput;

    @FXML
    private RadioButton singleRB;

    @FXML
    private RadioButton epRB;

    @FXML
    private RadioButton lpRB;

    @FXML
    private RadioButton dlpRB;

    @FXML
    private TextField minYear;

    @FXML
    private TextField maxYear;

    @FXML
    private Button search;

    //the selected medium
    private String medium = "";

    //the selected format
    private String format = "";

    /**
     * Executes when key is typed with text field activated.
     * @param event Event
     */
    @FXML
    void InputAction(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            Media[] m = sqlSearch();
        }
    }

    /**
     * Executes when search button is pressed.
     */
    @FXML
    void SearchButton()
    {
        Media[] m = sqlSearch();
    }

    @FXML
    /**
     * Indicates which medium is selected.
     */
    private void mediumControl(ActionEvent event) {

        //deselct all at buttons
        vinylRB.setSelected(false);
        cdRB.setSelected(false);
        cassetteRB.setSelected(false);

        if (event.getSource().equals(vinylRB))
        {
            //select vinyl button
            vinylRB.setSelected(true);
            //update number for switch
            medium = "vinyl";
        }
        else if (event.getSource().equals(cdRB))
        {
            //select cd button
            cdRB.setSelected(true);
            //update number for switch
            medium = "CD";
        }
        else
        {
            //select cassett button
            cassetteRB.setSelected(true);
            //update number for switch
            medium = "cassette";
        }
    }

    @FXML
    /**
     * Indicates which format is selected.
     */
    private void formatControl(ActionEvent event) {

        //deselcting all buttons
        singleRB.setSelected(false);
        epRB.setSelected(false);
        lpRB.setSelected(false);
        dlpRB.setSelected(false);
        if (event.getSource().equals(singleRB))
        {
            //select single button
            singleRB.setSelected(true);
            //update number for switch
            format = "Single";
        }
        else if (event.getSource().equals(epRB))
        {
            //select ep button
            epRB.setSelected(true);
            //update number for switch
            format = "EP";
        }
        else if (event.getSource().equals(lpRB))
        {
            //select lp button
            lpRB.setSelected(true);
            //update number for switch
            format = "LP";
        }
        else
        {
            //select dlp button
            dlpRB.setSelected(true);
            //update number for switch
            format = "DLP";
        }

    }

    //TODO TEST
    /**
     * Searches the database based on a higherarchy of inputs.
     * The title attribute is considered the most important followed by
     * band, genre, medium, format, and years.
     *
     * Should be used with slim() to get correct user input.
     * @return
     */
    private Media[] sqlSearch()
    {
        //order of importance
        //title, band, genre, medium, format, year range

        try {
            //prepared statement
            PreparedStatement stmt;
            ResultSet rs;

            //text entry is not empty
            if (!txt_search.getText().equals(""))
            {
                stmt = con.prepareStatement("SELECT * FROM media WHERE title LIKE ?");
                stmt.setString(1, "%" + txt_search.getText() + "%");
                rs = stmt.executeQuery();
                //create media objects
                return rsToMedia(rs);
            }
            //band entry is not empty
            else if (!bandInput.getText().equals(""))
            {
                //get band entries in a string array
                String allBands = bandInput.getText();
                String[] bands = allBands.split("\n");

                //get a result set for all bands
                //array of result sets
                ResultSet[] rsArr = new ResultSet[bands.length];

                for(int i = 0; i < bands.length; i++)
                {
                    stmt = con.prepareStatement("SELECT * FROM media WHERE band_id = ?");
                    stmt.setInt(1, Hash.StringHash(bands[i]));
                    rsArr[i] = stmt.executeQuery();
                    stmt.close();
                }

                //turn the rs array to a single media array and return
                return rsToMedia(rsArr);

            }
            //genre input is not empty
            else if (!genreInput.getText().equals(""))
            {
                //get genres in a signle array
                String allGenres = genreInput.getText();
                String[] genres = allGenres.split("\n");

                //all result sets saved here
                ResultSet[] rsArr = new ResultSet[genres.length];

                //loop for each genre
                for(int i = 0; i < genres.length; i++)
                {
                    stmt = con.prepareStatement("SELECT * FROM media LEFT (OUTER) JOIN genre_linker ON" +
                            "media.media_id = genre_linker.media_id WHERE genre_linker.genre_id = ?");
                    stmt.setInt(1, Hash.StringHash(genres[i]));
                    rsArr[i] = stmt.executeQuery();
                    stmt.close();
                }

                return rsToMedia(rsArr);
            }
            //medium entry is not empty
            else if (!medium.equals(""))
            {
                stmt = con.prepareStatement("SELECT * FROM media WHERE medium = ?");
                stmt.setString(1, medium);
                rs = stmt.executeQuery();
                return rsToMedia(rs);
            }
            //format entry is not empty
            else if (!format.equals(""))
            {
                stmt = con.prepareStatement("SELECT * FROM media WHERE format = ?");
                stmt.setString(1, format);
                rs = stmt.executeQuery();
                return rsToMedia(rs);
            }
            //year entries are not empty
            else if (!minYear.getText().equals("") || !maxYear.getText().equals(""))
            {
                //get time frame being searched in
                short min, max;
                if(minYear.getText().equals("") && !maxYear.getText().equals(""))
                {
                    min = 1400;
                    max = Short.parseShort(maxYear.getText());
                }
                else if(maxYear.getText().equals(""))
                {
                    min = Short.parseShort(minYear.getText());
                    max = (short) Year.now().getValue();
                }
                else
                {
                    min = Short.parseShort(minYear.getText());
                    max = Short.parseShort(maxYear.getText());
                }

                stmt = con.prepareStatement("SELECT * FROM media WHERE (year >= ?) AND (year <= ?)");
                stmt.setInt(1, min);
                stmt.setInt(2, max);
                rs = stmt.executeQuery();
                return rsToMedia(rs);
            }
            //everything is empty
            else
            {
                stmt = con.prepareStatement("SELECT * FROM media");
                rs = stmt.executeQuery();
                return rsToMedia(rs);
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
        }
        return null;
    }

    private Media[] slimResults()
    {
        return null;
    }

    //TODO test
    /**
     * Turns a result set into an array of media objects.
     * @param rs Result set of a search query
     * @return Array of media objects
     */
    public Media[] rsToMedia(ResultSet rs)
    {
        try
        {
            //get media out of rs and put all in a stack
            Stack<Media> st = new Stack<>();
            int count = 0;
            while(rs.next())
            {
                ++count;
                st.push(new Media(rs));
            }
            //media array of correct size
            Media[] media = new Media[count];

            //fill media array
            for(int i = 0; i < media.length; i++)
            {
                media[i] = st.pop();
            }

            //return the reveresed media array
            for(int i = 0; i < media.length; i++)
            {
                Media temp = media[i];
                media[i] = media[media.length-i-1];
                media[media.length-i-1] = temp;
            }
            return media;

        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
        }
        return null;
    }

    //TODO TEST
    /**
     * Takes an array of result sets and makes them into a single Media array.
     * @param rsArr Result set array.
     * @return Media array.
     */
    public Media[] rsToMedia(ResultSet[] rsArr)
    {
        try
        {
            LinkedList<Media> mediaList = new LinkedList<>();

            //get all media data from rsArr into the linked list
            for (int i = 0; i < rsArr.length; i++)
            {
                while (rsArr[i].next())
                {
                    mediaList.add(new Media(rsArr[i]));
                }
            }

            //get all data from linked list into an array
            Media[] media = new Media[mediaList.size()];
            //TODO TEST
            mediaList.toArray(media);
            return media;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Search Error", e.getMessage());
        }
        return null;
    }
}
