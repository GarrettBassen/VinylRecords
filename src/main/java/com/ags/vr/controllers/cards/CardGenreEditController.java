package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;


public class CardGenreEditController implements CardBase
{
    @FXML private ListView<String> list_genres;
    @FXML private HBox genre_box;
    @FXML private HBox pane_base;

    private CardMediaViewController card_base;
    private Media media;

    private String selected_genre;

    //popup stuff
    @FXML
    private Button current_remove_button;

    @FXML
    private Button current_rename_button;

    @FXML
    private TextField genreDisplay;

    @FXML
    private TitledPane popUp;

    @FXML
    private Button system_remove_button;

    @FXML
    private Button system_rename_button;

    @FXML
    private Button close_popup_button;

    //add popup stuff
    @FXML
    private TitledPane addGenrePopup;

    @FXML
    private TextField newGenreInput;

    @FXML
    private Button newGenreCancel;

    @FXML
    private Button newGenreConfirm;

    @FXML
    void initialize()
    {
        this.list_genres.setEditable(true);
        this.list_genres.setCellFactory(TextFieldListCell.forListView());
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
        this.setVisible(false);
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
        if (condition) { card_base.setPageBlockVisibility(true); }
    }

    @Override
    public void setMedia(Media media)
    {
        ClearData();
        this.media = media;
        displayGenres();
    }

    private void ClearData()
    {
        this.list_genres.getItems().clear();
    }

    private void displayGenres()
    {
        int[] genreArray = DBGenreLinker.getGenres(this.media);
        for (int genre : genreArray)
        {
            this.list_genres.getItems().add(DBGenre.getName(genre)[0]);
        }
    }

    @FXML
    void GenreCard(MouseEvent mouseEvent)
    {
        if(list_genres.getSelectionModel().getSelectedItem() != null)
        {
            popUp.setVisible(true);
            selected_genre = this.list_genres.getSelectionModel().getSelectedItem();
            popUp.setText(selected_genre);
            genreDisplay.setText(selected_genre);
        }
        else
        {
            addGenrePopup.setVisible(true);
        }
    }

    /**
     * Checks if the genre argument is displayed in the list view.
     * @param genre Genre being serched for.
     * @return True if the genre is displayed; false otherwise.
     */
    private boolean containsGenre(String genre)
    {
        ObservableList<String> obList = list_genres.getItems();
        for(int i = 0; i < obList.size(); i++)
        {
            //check if the genre is in the display
            if(obList.get(i).equals(genre))
            {
                return true;
            }
        }
        return false;
    }

    @FXML
    void closePopup(ActionEvent event)
    {
        popUp.setVisible(false);
        selected_genre = "";
        list_genres.getSelectionModel().clearSelection();
    }

    /**
     * Breaks the connection between the genre argument and the current media.
     * @param event Button press
     */
    @FXML
    void currentRemove(ActionEvent event)
    {
        //if the textbox genre is connected with the media
        if(containsGenre(genreDisplay.getText()))
        {
            DBGenreLinker.Delete(media.getID(), Hash.StringHash(genreDisplay.getText()));
            Graphical.InfoPopup("Removal Successful", "\"" + genreDisplay.getText() + "\" removed successfully.");
            selected_genre = "";
            ClearData();
            displayGenres();
        }
        else
        {
            Graphical.ErrorPopup("Removal Unsuccessful", "\"" + genreDisplay.getText() + "\" is not connected with this media.");
        }
    }

    /**
     * Renames the current genre that is displayed
     * @param event Button press
     */
    @FXML
    void currentRename(ActionEvent event)
    {

        if (!DBGenre.Contains(genreDisplay.getText()))
        {
            boolean bool = Graphical.ConfirmationPopup("Genre is not in system", "\"" + genreDisplay.getText() + "\" is not in system." +
                    "Would you like to add it to the system and rename the selected genre?");

            //if user selects yes
            if (bool)
            {
                //insert and connect the genre
                DBGenre.Insert(genreDisplay.getText());
                renameHelper();
            }
            //do nothing if the user selects no
        }
        else if(!containsGenre(genreDisplay.getText()))
        {
            renameHelper();
        }
        else
        {
            Graphical.ErrorPopup("Genre Rename Error", "\"" + genreDisplay.getText() + "\" is already connected to this media.");
        }
    }

    @FXML
    void systemRemove(ActionEvent event)
    {
        try
        {
            if(containsGenre(genreDisplay.getText()))
            {
                boolean bool = Graphical.ConfirmationPopup("System Removal", "Are you sure you want to remove \"" +
                        genreDisplay.getText() + "\" from your system?");

                if(bool)
                {
                    //get all genre linkers
                    PreparedStatement stmt = con.prepareStatement("SELECT * FROM genre_linker WHERE genre_id=?");
                    stmt.setInt(1, Hash.StringHash(genreDisplay.getText()));
                    ResultSet rs = stmt.executeQuery();

                    //break all connections
                    while(rs.next())
                    {
                        DBGenreLinker.Delete(rs.getInt("media_id"), rs.getInt("genre_id"));
                    }

                    //remove the genre
                    DBGenre.Delete(genreDisplay.getText());

                    //inform user
                    Graphical.InfoPopup("System Removal Successful", "\"" + genreDisplay.getText() + "\" removed successfully system wide.");
                }
            }
            else
            {
                Graphical.ErrorPopup("Removal Unsuccessful", "\"" + genreDisplay.getText() + "\" is not connected with this media.");
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Genre Removal Error", e.getMessage());
        }
    }

    /**
     * Renames the selected genre system-wide.
     * @param event Button Press
     */
    @FXML
    void systemRename(ActionEvent event)
    {
        try{
            if(!DBGenre.Contains(genreDisplay.getText()))
            {
                //user conformation
               boolean bool = Graphical.ConfirmationPopup("Genre is not in system", "\"" + genreDisplay.getText() + "\" is not in system. "
               + "Would you like to rename the selected genre system wide?");

               if(bool)
               {
                   bool = Graphical.ConfirmationPopup("System Rename", "Are you sure you want to rename " + selected_genre + " to "
                           + genreDisplay.getText() + " throughout the entire system?");
                   if(bool)
                   {
                       //inserting and renaming
                        DBGenre.Insert(genreDisplay.getText());
                        systemRenameHelper();
                   }
               }
            }
            else if(!containsGenre(genreDisplay.getText()))
            {
                //user conformation
                boolean bool = Graphical.ConfirmationPopup("System Rename", "Are you sure you want to rename " + selected_genre + " to "
                        + genreDisplay.getText() + " throughout the entire system?");

                if(bool)
                {
                    //renaming
                    systemRenameHelper();
                }
            }
            else
            {
                Graphical.ErrorPopup("Genre Rename Error", "\"" + genreDisplay.getText() + "\" is already connected to this media.");
            }

        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("SQL Error", e.getMessage());
        }
    }

    private void renameHelper()
    {
        //remove the old connection
        DBGenreLinker.Delete(media.getID(), Hash.StringHash(selected_genre));

        //make new connection
        DBGenreLinker.Insert(media.getID(), Hash.StringHash(genreDisplay.getText()));

        //inform user
        Graphical.InfoPopup("Genre renamed", "Genre successfully renamed");

        //change the currently selected genre
        selected_genre = genreDisplay.getText();

        //update the list view
        ClearData();
        displayGenres();

        //reselect the edited name
        list_genres.getSelectionModel().select(selected_genre);
    }


    private void systemRenameHelper() throws SQLException
    {
        //get all genre_linker entries that have the selected genre
        PreparedStatement stmt = con.prepareStatement("SELECT * FROM genre_linker WHERE genre_id=?");
        stmt.setInt(1, Hash.StringHash(selected_genre));
        ResultSet rs = stmt.executeQuery();

        //delete all current genre linkers
        //create new links with the new genre
        while(rs.next())
        {
            DBGenreLinker.Delete(rs.getInt("media_id"), rs.getInt("genre_id"));
            DBGenreLinker.Insert(rs.getInt("media_id"), Hash.StringHash(genreDisplay.getText()));
        }


        //remove the genre from the database
        DBGenre.Delete(selected_genre);
        //change the selected genre and display
        selected_genre = genreDisplay.getText();
        ClearData();
        displayGenres();

        Graphical.InfoPopup("Genre renamed", "Genre successfully renamed system wide");
    }

    //addPopup Methods

    @FXML
    void closeAddGenre(ActionEvent event)
    {
        addGenrePopup.setVisible(false);
        list_genres.getSelectionModel().clearSelection();
    }

    @FXML
    void confirmNewGenre(ActionEvent event) {
        //add and connect the genre if its not already connected to this media object
        if (!containsGenre(newGenreInput.getText()))
        {
            //if the genre is not already in the system add the genre
            if (!DBGenre.Contains(newGenreInput.getText()))
            {
                DBGenre.Insert(newGenreInput.getText());
            }

            //create the new link
            DBGenreLinker.Insert(media.getID(), Hash.StringHash(newGenreInput.getText()));

            //display new data
            ClearData();
            displayGenres();
            //close popup
            addGenrePopup.setVisible(false);
            list_genres.getSelectionModel().clearSelection();
        }
        //the genre is already connected
        else
        {
            Graphical.ErrorPopup("Genre Confirmation Error", "This genre is already connected with this media.");
        }
    }
}
