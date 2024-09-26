package com.ags.vr.controllers.utils;

//imports
import com.ags.vr.objects.Request;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;


public class PaneRequestEntryController {

    //declaring FXML variables for any Request Entries
    @FXML private Label requestName;
    @FXML private Label date;
    @FXML private Label customerName;
    @FXML private Label edit;
    @FXML private ImageView deleteIcon;
    @FXML private Label phoneNumber;
    @FXML private Label email;

    //initialize Request Object
    private Request request;


    //future methods...

    public void setData(Request request)
    {
        // Reference variables
        this.request = request;

        // Display variables
        //this.pane_content.setPrefWidth(Integer.MAX_VALUE);
        this.requestName.setText(request.getRequest());
        this.customerName.setText(request.getCustomerName());
        this.date.setText(String.valueOf(request.getDate()));
        this.phoneNumber.setText(String.valueOf(request.getPhoneNumber()));
        this.email.setText(request.getEmail());
    }

    /**
     * Method used to remove request from the database.
     */
    public void deleteRequest() {

        //temporary console print
        System.out.println("Request Deleted...");
    }

}