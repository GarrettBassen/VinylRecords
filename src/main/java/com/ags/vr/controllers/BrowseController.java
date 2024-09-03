package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.database.DBGenre;
import com.ags.vr.utils.database.DBMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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

    //the selected medium
    private String medium = "";

    /**
     * Executes when key is typed with text field activated.
     * @param event Event
     */
    @FXML
    void InputAction(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.ENTER))
        {
            String name = txt_search.getText();
            Media media = DBMedia.getMedia(name, medium);
        }
    }

    /**
     * Executes when search button is pressed.
     */
    @FXML
    void SearchButton()
    {
        String name = txt_search.getText();
        Media media = DBMedia.getMedia(name, medium);
    }

    @FXML
    /**
     * Indicates which medium is selected.
     */
    private void mediumControl(ActionEvent event)
    {
        //deselct all at buttons
        vinylRB.setSelected(false);
        cdRB.setSelected(false);
        cassetteRB.setSelected(false);

        if(event.getSource().equals(vinylRB))
        {
            //select vinyl button
            vinylRB.setSelected(true);
            //update number for switch
            medium = "vinyl";
        }
        else if(event.getSource().equals(cdRB))
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
}
