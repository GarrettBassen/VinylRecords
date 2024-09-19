package com.ags.vr.objects;

//imports
import com.ags.vr.utils.Graphical;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Request Object that'll be used in various ways...
 */
public class Request implements Serializable {

    //variable declaration
    private String customerName;
    private double phoneNumber;
    private String email;
    private short date;
    private String request;


    //------------------ Constructors ------------------

    /**
     * Default constructor.
     */
    public Request() {}

    /**
     * Constructor to create a Request object with the following parameters.
     * @param customerName is string holding the respected information.
     * @param phoneNumber is double holding the respected information.
     * @param email is string holding the respected information.
     * @param date is short holding the respected information.
     * @param request is string holding the respected information.
     */
    public Request(String customerName, double phoneNumber, String email, short date, String request) {

        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email =email;
        this.date = date;
        this.request = request;
    }

    /**
     * Creates Request object from database. WARNING you must supply a valid result set (example result.next()).
     * @param request Request ResultSet
     */
    public Request(ResultSet request)
    {
        try
        {
            this.customerName = request.getString("customer");
            this.phoneNumber = request.getDouble("phone_number");
            this.email = request.getString("email");
            this.date = request.getShort("date");
            this.request = request.getString("request");
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Error creating request", String.format(
                    "Error creating request in Request(ResultSet) | Request.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
        }
    }


    //------------------ Setters ------------------

    /**
     * Method to set the customer name.
     * @param customerName is a string that represents who is making a request.
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * Method to set the phone number.
     * @param phoneNumber is a double that represents one way of contacting the customer.
     */
    public void setPhoneNumber(double phoneNumber) { this.phoneNumber = phoneNumber; }

    /**
     * Method to set the email.
     * @param email is a string that represents one way of contacting the customer.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Method to set the date.
     * @param date is a short that represents the date that the request was made/entered into the program.
     */
    public void setDate(short date) { this.date = date; }

    /**
     * Method to set the request that was made.
     * @param request is a string that represents the title and format of the media being requested.
     */
    public void setRequest(String request) { this.request = request; }


    //------------------ Getters ------------------

    /**
     * Method to return the customers name to the user.
     * @return a String representing the name of who is making the request.
     */
    public String getCustomerName() { return customerName; }

    /**
     * Method to return one way to contact the customer.
     * @return a double representing the phone number of the customer making the request.
     */
    public double getPhoneNumber() { return phoneNumber; }

    /**
     * Method to return one way to contact the customer.
     * @return a String representing the email of the customer making the request.
     */
    public String getEmail() { return email; }

    /**
     * Method to return the date of the request.
     * @return a short representing the date that the request was made/entered into the program.
     */
    public short getDate() { return date; }

    /**
     * Method to return the request that was made.
     * @return a String representing the title and format of media that the customer requested.
     */
    public String getRequest() { return request; }
}
