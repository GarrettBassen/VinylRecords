package com.ags.vr.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BrowseController
{
    // Variables
    @FXML
    private TextField txt_search;
    @FXML
    private Button btn_search;

    /**
     * Executes when enter key is pressed and search textfield is activated.
     * @param event Event
     */
    @FXML
    void InputValidation(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            // TODO SEARCH
            System.out.println("Enter Pressed");
        }
    }

    /**
     * Executes when search button is pressed.
     * @param event Event
     */
    @FXML
    void SearchButton(ActionEvent event)
    {
        // TODO SEARCH
        System.out.println("Search Button Pressed");
    }
}
