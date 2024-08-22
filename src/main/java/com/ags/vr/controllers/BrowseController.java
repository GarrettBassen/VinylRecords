package com.ags.vr.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class BrowseController
{
    // Variables
    @FXML
    private TextField txt_search;
    @FXML
    private Button btn_search;

    /**
     * Executes when key is released or mouse selects text field.
     */
    @FXML
    void CreateSpace()
    {
        // TODO MAKE BETTER
        if (txt_search.getText().length() < 4 || !txt_search.getText(0,4).isBlank())
        {
            String str = "";
            if (!txt_search.getText().isBlank())
            {
                str = txt_search.getText().trim();
            }

            txt_search.setText("    " + str);
            txt_search.positionCaret(4);
        }

        if (txt_search.getCaretPosition() < 4)
        {
            txt_search.positionCaret(4);
        }
    }

    /**
     * Executes when enter key is typed and text field is activated.
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

        // TODO PLEASE FIX THIS
        // TODO MAY ERASE EXISTING STRING
        if (txt_search.getText().length() < 4 || !txt_search.getText(0,3).isBlank())
        {
            txt_search.setText("    ");
            txt_search.positionCaret(4);
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
