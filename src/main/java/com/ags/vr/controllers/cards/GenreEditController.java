package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBGenreLinker;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.HBox;

import java.awt.event.MouseEvent;

public class GenreEditController implements CardBase
{
    @FXML private ListView<String> list_genres;
    @FXML private HBox pane_base;

    ObservableList<String> genres = FXCollections.observableArrayList();

    private MediaCardController card_base;
    private Media media;
    //Test
    private MouseEvent mouseEvent;

    @FXML
    void initialize()
    {
        this.list_genres.setEditable(true);
        this.list_genres.setCellFactory(TextFieldListCell.forListView());
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
    public void setMediaCard(MediaCardController controller)
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

}
