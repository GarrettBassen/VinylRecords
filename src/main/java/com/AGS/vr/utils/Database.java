package com.AGS.vr.utils;

import com.AGS.vr.objects.Media;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.AGS.vr.utils.Connector.conn;

public class Database
{
    /**
     * Inserts media object into database.
     * @param media Media object with data
     * @return True if successful; false otherwise
     */
    public static Boolean InsertMedia(Media media)
    {
        // TODO TEST
        try
        {
            // Get data from media object
            String[] data = media.getData();

            // Create statement and execute
            PreparedStatement statement = conn.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?)");
            statement.setString(1,data[0]);
            statement.setString(2,data[1]);
            statement.setString(3,data[2]);
            statement.setString(4,data[3]);
            statement.setString(5,data[4]);
            statement.execute();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }

        return true;
    }

    // TODO ALLOWS MODIFICATION AND COLLECTION OF DATA FROM DATABASE
}
