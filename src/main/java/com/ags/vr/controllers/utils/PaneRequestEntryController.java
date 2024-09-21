package com.ags.vr.controllers.utils;

//imports
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;


public class PaneRequestEntryController {

    //declaring FXML variables for any Request Entries
    @FXML private Label request;
    @FXML private Label date;
    @FXML private Label customerName;
    @FXML private Label edit;
    @FXML private ImageView deleteIcon;
    @FXML private Label phoneNumber;
    @FXML private Label email;
    

    //future methods...

    /**
     * Method used to remove request from the database.
     */
    public void deleteRequest() {

        //temporary console print
        System.out.println("Request Deleted...");
    }


}
