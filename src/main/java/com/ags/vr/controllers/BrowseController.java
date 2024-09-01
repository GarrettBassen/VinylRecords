package com.ags.vr.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class BrowseController
{
    // Variables
    @FXML
    private TextField txt_search;

    /**
     * Executes when key is typed with text field activated.
     * @param event Event
     */
    @FXML
    void InputAction(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            // TODO SEARCH
            System.out.println("Enter Pressed");
        }
    }

    /**
     * Executes when search button is pressed.
     */
    @FXML
    void SearchButton()
    {
        // TODO SEARCH
        System.out.println("Search Button Pressed");
    }
}
