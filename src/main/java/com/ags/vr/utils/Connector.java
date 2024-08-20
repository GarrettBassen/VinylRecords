package com.ags.vr.utils;

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
    private static final String url =  "jdbc:mysql://localhost:3306/vinyl_records";
    private static String usr =  "";
    private static String pass = "";

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
