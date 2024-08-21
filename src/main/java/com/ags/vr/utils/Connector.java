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
    public static Connection con;

    // WARNING Do NOT push with your unique username and password
    private static final String url =  "jdbc:mysql://localhost:3306/oldSkoolDB";
    private static String usr =  "";
    private static String pass = "";

    /**
     * Attempts to connect to database and assigns 'con' Connection object.
     */
    public static void connect()
    {
        // Do nothing if database is connected
        if (con != null)
        {
            return;
        }

        // Try to connect if database is not connected
        try
        {
            con = DriverManager.getConnection(url, usr, pass);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Connection Error",
                    String.format(
                            "Error connecting to database. Please ensure username, password, and url are " +
                            "valid and that the database is running on the device.\n\nCODE: %s\n%s",
                            e.getErrorCode(), e.getMessage()
                    )
            );
        }
    }

    /**
     * Safely disconnects from the database.
     */
    public static void disconnect()
    {
        // Do nothing if database is not connected
        if (con == null)
        {
            return;
        }

        // Try to close database connection
        try
        {
            con.close();
        }
        catch (SQLException e)
        {
            System.out.println("ERROR DISCONNECTING FROM DATABASE!\n\n" + e.getMessage());
        }
    }
}
