package com.ags.vr.utils;

import static com.ags.vr.utils.Connector.con;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for database helper utilities.
 */
public class DBHelper
{
    /**
     * Takes n many strings, concatenates them, strips of special characters, then returns hash from inputs.
     * @param string 1-n String inputs
     * @return Integer unique hash code
     */
    public static int StringHash(String... string)
    {
        // concatenate all strings
        String raw = String.join("",string);

        // Removes all punctuation, white space, and converts string to lowercase then returns hash
        return raw.toLowerCase().strip().replaceAll("[^a-zA-Z0-9]","").hashCode();
    }

    // TODO TEST
    public static boolean MediaExists(int ID)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE 'ID'=?");
            statement.setInt(1,ID);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            System.err.println("ERROR IN MediaExists(int ID) DBHelper.java");
            return false;
        }
    }
}
