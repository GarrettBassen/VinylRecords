package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

public class CardGenreEditController implements CardBase
{
    @FXML private ListView<String> list_genres;
    @FXML private HBox genre_box;
    @FXML private HBox pane_base;

    private CardMediaViewController card_base;
    private Media media;

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
        addGenres();
    }

    private void ClearData()
    {
        this.list_genres.getItems().clear();
    }

    private void addGenres()
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
        System.out.println(this.list_genres.getSelectionModel().getSelectedItem());
        //broken
            /*
            //com/ags/vr/fxml/pages/page_add.fxml
            ///com/ags/vr/fxml/popups/popup_genre_edit.fxml
            FXMLLoader popupLoader = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/popups/popup_genre_edit.fxml"));
            pane_base.getChildren().add(popupLoader.load());
            genre_popup_base = popupLoader.getController();
             */
        popUp.setVisible(true);
        // TODO REMOVE AFTER TESTING
        // list_genres.setVisible(false);
    }

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

    @FXML
    void closePopup(ActionEvent event)
    {
        popUp.setVisible(false);
        list_genres.setVisible(true);
    }

    @FXML
    void currentRemove(ActionEvent event) {

    }

    @FXML
    void currentRename(ActionEvent event) {

    }

    @FXML
    void systemRemove(ActionEvent event) {

    }

    @FXML
    void systemRename(ActionEvent event) {

    }
}
