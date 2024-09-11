package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;

public class CardGenreEditController implements CardBase
{
    @FXML private ListView<String> list_genres;
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
    void genreCard(MouseEvent event)
    {
        System.out.println("MOUSE CLICKED");
    }

    @FXML
    void GenreCard(MouseEvent mouseEvent)
    {
        System.out.println(this.list_genres.getSelectionModel().getSelectedItem());
    }
}
