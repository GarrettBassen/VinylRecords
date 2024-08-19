package com.AGS.vr.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connects software to database using user credentials and assigns 'con' object for use throughout software.
 */
public class Connector
{
    // Connection object
    public static Connection conn;

    // WARNING Do NOT push with your unique username and password
    private static final String url =  "jdbc:mysql://localhost:3306/Record_Keeping";
    private static String usr =  "";
    private static String pass = "";

    /**
     * Sets username of log in credentials
     * @param u username
     */
    public static void setUsr(String u){
        usr = u;
    }

    /**
     * Sets password of log in credentials
     * @param p
     */
    public static void setPass(String p){
        pass = p;
    }


    /**
     * Attempts to connect to database and assigns 'con' Connection object.
     */
    public static void connect()
    {
        // Do nothing if database is connected
        if (conn != null)
        {
            return;
        }

        // Try to connect if database is not connected
        try {
            conn = DriverManager.getConnection(url, usr, pass);

            // Display error popup if database is not connected
            if (conn == null)
            {
                Graphical.ErrorPopup("Cannot Connection Error",
                        "Could not connect to database. Please check internet connection and try again.");
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Connection Error", e.toString());
        }
    }
}
