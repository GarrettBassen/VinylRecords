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
    public static void Insert(Media media)
    {
        try
        {
            // TODO CREATE GENRE LINKER ENTRIES AND INVENTORY TABLES

            PreparedStatement stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
            stmt.setInt   (1, media.getMedia_ID());
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
                    "Could not add media to database in insertMedia(Media media) DBMedia.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
        }
    }

    /**
     * Checks if media exists within database.
     * @param media Media object
     * @return True if media exists; false otherwise
     */
    public static boolean Contains(Media media)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE media_id=?");
            statement.setInt(1,media.getMedia_ID());
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding media in ContainsMedia(int ID) | DBMedia.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    /**
     * Checks if media exists within database.
     * @param name String name of the media
     * @return True if media exists; false otherwise
     */
    public static boolean Contains(String name)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE title=?");
            statement.setString(1,name);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding media in ContainsMedia(int ID) | DBMedia.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    public static Media getMedia(String name)
    {
        short year;
        String medium = "";
        String format = "";
        String band = "";

        try
        {
            //get the media from the database based on its name
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE title=?");
            statement.setString(1,name);
            ResultSet result = statement.executeQuery();
            //create and return new media object
            return new Media(result);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
        return null;
    }

    /**
     * Removes the provided media object from the database.
     * @param media Media object.
     */
    public static void Delete(Media media)
    {
        // TODO TEST
        try
        {
            // TODO REMOVE MEDIA FROM GENRE LINKER AND INVENTORY TABLES
            PreparedStatement statement = con.prepareStatement("DELETE FROM media WHERE media_id=?");
            statement.setInt(1,media.getMedia_ID());
            statement.execute();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
    }
}
