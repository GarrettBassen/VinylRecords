package com.ags.vr.controllers.pages;

//imports
import com.ags.vr.objects.Request;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBMedia;
import com.ags.vr.utils.database.DBRequest;
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

        //future code to check for any errors in the user input using the validation method...

        //create the request
        Request request = createRequest();

        //insert request into the Database
        DBRequest.Insert(request);

        //call helper methods to clear the text fields and also ensure that the popup is "closed"
        closePopUp();

        //inform the user that the adding of the request was successful via popup
        Graphical.ConfirmationPopup("Success Adding Request",String.format(
                "'%s' was requested by '%s' was successfully added to your requests",
                request.getRequest(), request.getCustomerName()
        ));
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
     * Method used to create a Request Object from the user input.
     * @return the newly developed Request Object.
     */
    private Request createRequest() {

        //initialize new Request Object
        Request request = new Request();

        //get text fields entered by the user and set them to the Request Object
        request.setCustomerName(customerField.getText());
        request.setPhoneNumber(Double.parseDouble(phoneField.getText()));
        request.setEmail(emailField.getText());
        request.setDate(Short.parseShort(dateField.getText()));
        request.setRequest(requestField.getText());

        //return the Request Object
        return request;
    }

    /**
     * Helper method used to close the popup if exit icon or submit button is pressed.
     * Additionally, the helper function to clear the text fields will be called as well.
     */
    public void closePopUp() {

        clearFields(); //first call the helper method to clear all text fields
        addPopUp.setVisible(false); //toggle the visibility to false
    }


    /**
     * Method used to clear all the text fields.
     */
    public void clearFields() {

        //clear all text fields
        customerField.clear();
        phoneField.clear();
        emailField.clear();
        dateField.clear();
        requestField.clear();
    }
}