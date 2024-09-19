package com.ags.vr.controllers.pages;

//imports
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


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
    @FXML private BorderPane rootPane;
    @FXML private Button addBtn; //calls the launchAddPopUp method
    @FXML private ImageView refreshBtn; //calls the refreshRequests method
    @FXML private TextArea requests;

    //declaring FXML variables for the Add Request pop-up
    @FXML private VBox addPopUp;
    @FXML private ImageView closeBtn; //calls the closePopUp method
    @FXML private TextField customerField;
    @FXML private TextField dateField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField requestField;
    @FXML private Button submitBtn; //calls the submitRequest method

    //todo!
    /**
     * Method used to initialize the page with possible data from the database
     */
    @FXML void initialize() {

    }


    //------------------ Functionality ------------------

    /**
     * Method used to launch a popup that will contain text fields that when
     * entered will be stored in the database and later displayed to the user.
     * @param event is the action of the add request button being pressed by the user.
     */
    public void launchAddPopUp(ActionEvent event) { addPopUp.setVisible(true); }


    //todo!
    /**
     * Method used to send the data that the user inputs to the
     * database to be stored and later displayed to the user.
     * WIP to error handle any improper text fields input.
     * Upon clicking submit if there are no errors then the popup close.
     * @param event is the action of the submit button being pressed by the user.
     */
    public void submitRequest(ActionEvent event) {

        //future code to check for any errors in the user input...


        //call helper method so that the popup can be "closed"
        closePopUp();

    }


    //todo!
    /**
     * Method used to refresh the text area with the data that is stored in the request table from the database.
     * @param e is the action of the button being pressed by the user
     */
    public void refreshRequests(ActionEvent e) {

    }


    //------------------ Validation ------------------

    /**
     * Method to validate user input ensuring that the data may be stored.
     * @return a boolean value, true meaning the input can be stored and false resulting in the user having to try again.
     */
    private boolean userInputValidation() {
        return true; //for now...
    }

    
    //------------------ Helper Methods ------------------

    /**
     * Helper method used to close the popup if exit icon or submit button is pressed.
     */
    public void closePopUp() { addPopUp.setVisible(false); }

}