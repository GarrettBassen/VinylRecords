package com.ags.vr.controllers.pages;

//imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Manages the request page where the user can look to see if they have any current requests or add to said list.
 * Upon navigating to the request page it will be populated with the current requests (if any). Additionally, there
 * is the option to add new requests with a simple pop-up that has the text fields required to populate the database.
 * All proper error handling for correct input, addition/deletion from the database, modification of said data, and
 * variables with not null constraints will be accounted for. Phone number and Email are the only inputs that will be
 * allowed to be left null.
 */
public class PageRequestController {

    //declaring FXML variables for the Request Page
    @FXML private Button addBtn; //calls the launchAddPopUp method
    @FXML private ImageView refreshBtn; //calls the refreshRequests method

    //declaring FXML variables for the Add Request pop-up
    @FXML private TextField customerField;
    @FXML private TextField dateField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField requestField;

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
     * @param event is the action of the button being pressed by the user
     */
    public void launchAddPopUp(ActionEvent event) {
        //try catch block to catch any errors with locating the correct file path
        try {
            //create new FXMLLoader object with the file path to the new scene
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ags/vr/fxml/pages/page_requests_add.fxml"));
            Parent popUp = fxmlLoader.load();

            //create new stage object and set title
            Stage addRequest = new Stage();
            addRequest.setTitle("Old Skool's Inventory Management System");

            //set size of stage
            addRequest.setMinWidth(400);
            addRequest.setMinHeight(500);

            //set scene and display
            addRequest.setScene(new Scene(popUp));
            addRequest.show();
        } catch (Exception e) {
            System.out.println("There were issues loading the new window!");
        }
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