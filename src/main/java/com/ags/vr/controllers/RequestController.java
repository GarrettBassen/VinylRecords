package com.ags.vr.controllers;

//imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


/**
 * Manages the request page where the user can look to see if they have any current requests or add to said list.
 * Upon navigating to the request page it will be populated with the current requests (if any). Additionally, there
 * is the option to add new requests with a simple pop-up that has the text fields required to populate the database.
 * All proper error handling for correct input, addition/deletion from the database, modification of said data, and
 * variables with not null constraints will be accounted for. Phone number and Email are the only inputs that will be
 * allowed to be left null.
 */
public class RequestController {

    //declaring FXML variables
    @FXML private Button addBtn; //calls the launchAddPopUp method
    @FXML private ImageView refreshBtn; //calls the refreshRequests method


    //todo!
    /**
     * Method used to initialize the page with possible data from the database
     */
    @FXML void initialize() {

    }


    //todo!
    /**
     * Method used to launch a popup that will contain text fields that when entered will be stored
     * in the database and later displayed to the user.
     * @param e is the action of the button being pressed by the user
     */
    public void launchAddPopUp(ActionEvent e) {

    }

    //todo!
    /**
     * Method used to refresh the text area with the data that is stored in the request table from the database.
     * @param e is the action of the button being pressed by the user
     */
    public void refreshRequests(ActionEvent e) {

    }

    //todo!
    /**
     * Method used to build a string using the data that is stored in the request table from the database.
     * These strings will later be displayed in the text area that contains the current requests.
     */
    public void buildRequestString() {

    }
}