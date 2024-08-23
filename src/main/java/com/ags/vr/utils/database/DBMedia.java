package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBMedia
{
    /**
     * Inserts the provided media object into the database.
     * @param media Media object
     */
    public static void insertMedia(Media media)
    {
        try
        {
            // TODO CREATE GENRE LINKER ENTRIES AND INVENTORY TABLES

            PreparedStatement stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
            stmt.setInt   (1, media.getID());
            stmt.setString(2, media.getTitle());
            stmt.setString(3, media.getMedium());
            stmt.setString(4, media.getFormat());
            stmt.setShort (5, media.getYear());
            stmt.setInt   (6, media.getBandID());
            stmt.execute();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Could not add media to database insertMedia(Media media) DBMedia.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
        }
    }

    // TODO TEST
    public static boolean ContainsMedia(Media media)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE 'ID'=?");
            statement.setInt(1,media.getID());
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding media ContainsMedia(int ID) DBMedia.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    /**
     * Removes the provided media object from the database.
     * @param media Media object.
     */
    public static void deleteMedia(Media media)
    {
        // TODO TEST
        try
        {
            // TODO REMOVE MEDIA FROM GENRE LINKER AND INVENTORY TABLES
            PreparedStatement statement = con.prepareStatement("DELETE FROM media WHERE ID=?");
            statement.setInt(1,media.getID());
            statement.execute();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
    }
}
