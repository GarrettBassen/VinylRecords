package com.ags.vr.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.ibatis.jdbc.ScriptRunner;
import java.io.Reader;
import java.io.BufferedReader;

/**
 * Connects software to database using user credentials and assigns 'con' object for use throughout software.
 */
public class Connector
{
    // Connection object
    public static Connection con;

    // WARNING Do NOT push with your unique username and password
    private static String url =  "jdbc:mysql://localhost:3306/oldSkoolDB";
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
            try
            {
                sqlInstall();
            }
            //TODO MAKE SAFE
            catch (Exception e1)
            {
                Graphical.ErrorPopup("Database Connection Error",
                        String.format("Error connecting to database. Please ensure username, password, and url are " +
                                        "valid and that the database is running on the device.\n\nCODE: %s\n%s",
                                e.getErrorCode(), e.getMessage()));
            }
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

    // TODO CALL IN SETUP WIZARD
    /**
     * Installs the database schema oldSkoolDB.
     */
    public static void sqlInstall()
    {
        try
        {
            // Connect and install sql schema
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306", usr, pass);

            //Initialize the script runner
            ScriptRunner sr = new ScriptRunner(con);
            //Creating a reader object
            Reader reader = new BufferedReader(new FileReader("database/buildOldSkoolDB.sql"));
            //Running the script
            sr.runScript(reader);

            con.close();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("ERROR INSTALLING DATABASE",String.format(
                    "ERROR INSTALLING DATABASE sqlInstall() | Connector.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
        }
        catch (FileNotFoundException e)
        {
            Graphical.ErrorPopup("ERROR INSTALLING DATABASE",String.format(
                    "File not found error\n\n%s", e.getMessage()
            ));
        }
    }

    //TODO MAKE INSTALL SAFER AND FASTER
}
