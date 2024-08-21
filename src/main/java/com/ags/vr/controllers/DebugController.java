package com.ags.vr.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class DebugController
{
    // Variables
    @FXML
    private Button btn_submit;
    @FXML
    private CheckBox cb_format_DLP;
    @FXML
    private CheckBox cb_format_EP;
    @FXML
    private CheckBox cb_format_LP;
    @FXML
    private CheckBox cb_format_single;
    @FXML
    private CheckBox cb_medium_CD;
    @FXML
    private CheckBox cb_medium_cassette;
    @FXML
    private CheckBox cb_medium_vinyl;
    @FXML
    private TextField txt_album;
    @FXML
    private TextField txt_band;
    @FXML
    private TextField txt_genre;
    @FXML
    private TextField txt_year;

    // Methods
    @FXML
    void AddMedia(ActionEvent event)
    {
        // TODO MAKE SQL QUERY HERE
    }
}
