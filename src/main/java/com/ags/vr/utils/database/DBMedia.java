package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

/**
 * Handles media database operations including the following:
 * <ul>
 *     <li>{@link #Insert(Media)}</li>
 *     <li>{@link #Delete(Media)}</li>
 *     <li>{@link #getMedia(int)}</li>
 *     <li>{@link #getMedia(String)}</li>
 *     <li>{@link #Contains(Media)}</li>
 *     <li>{@link #Contains(int)}</li>
 *     <li>{@link #Contains(String)} (Media)}</li>
 * </ul>
 */
public class DBMedia
{
    /**
     * Inserts the provided media object into the database.<br />
     * <strong>WARNING: Band ID must exist in band table or insertion will fail</strong>
     * @param media Media object
     * @return True if inserted; False otherwise
     */
    public static boolean Insert(Media media)
    {
        try
        {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
            stmt.setInt   (1, media.getID());
            stmt.setString(2, media.getTitle());
            stmt.setString(3, media.getMedium());
            stmt.setString(4, media.getFormat());
            stmt.setShort (5, media.getYear());
            stmt.setInt   (6, media.getBandID());
            stmt.execute();
            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Could not add '%s' to database in Insert(Media) | DBMedia.java\n\nCode: %s\n%s",
                    media.getTitle(), e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    /**
     * Checks if media object exists within database, returning a boolean value.
     * @param media Media object
     * @return True if media exists; False otherwise
     */
    public static boolean Contains(Media media)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE media_id=?");
            statement.setInt(1,media.getID());
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding '%s' in ContainsMedia(Media) | DBMedia.java\n\nCode: %s\n%s",
                    media.getTitle(), e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    // TODO ADD BAND FOR HASH SEARCH
    /**
     * Checks if media with specified ID exists within database, returning a boolean value.
     * @param hash Hash ID
     * @return True if media exists; False otherwise
     */
    public static boolean Contains(int hash)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE media_id=?");
            statement.setInt(1, hash);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding ID:%s in ContainsMedia(String) | DBMedia.java\n\nCode: %s\n%s",
                    hash, e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    // TODO TEST
    public static boolean Contains(String title, String medium)
    {
        try
        {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM media WHERE title=? AND medium=?");
            stmt.setString(1,title);
            stmt.setString(2,medium);
            ResultSet result = stmt.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error finding '%s' in ContainsMedia(String) | DBMedia.java\n\nCode: %s\n%s",
                    title, e.getErrorCode(), e.getMessage()
            ));
            return false;
        }
    }

    // TODO TEST
    public static Media getMedia(int hash)
    {
        try
        {
            //get the media from the database based on its name
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE media_id=?");
            statement.setInt(1, hash);
            ResultSet result = statement.executeQuery();
            return new Media(result);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error getting ID:%s in getMedia(String, String) | DBMedia.java\n\nCode: %s\n%s",
                    hash, e.getErrorCode(), e.getMessage()
            ));
            return null;
        }
    }

    public static Media getMedia(String title, String medium)
    {
        try
        {
            //get the media from the database based on its name
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM media WHERE title=? AND medium=?");
            stmt.setString(1,title);
            stmt.setString(2,medium);
            ResultSet result = stmt.executeQuery();
            return new Media(result);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error getting '%s' in getMedia(String, String) | DBMedia.java\n\nCode: %s\n%s",
                    title, e.getErrorCode(), e.getMessage()
            ));
            return null;
        }
    }

    /**
     * Removes the provided media object from the database, returning a boolean value.
     * @param media Media object.
     * @return True if successfully deleted; False otherwise
     */
    public static boolean Delete(Media media)
    {
        // TODO SEE ABOUT RETURNING TRUE IF VALUE NEVER EXISTED IN DATABASE (check SQLException)
        // TODO TEST
        try
        {
            PreparedStatement statement = con.prepareStatement("DELETE FROM media WHERE media_id=?");
            statement.setInt(1,media.getID());
            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }
    }


    public static void Update(Media newMedia, int oldID)
    {
        try
        {
            //TODO COMPRESS IF POSSIBLE
            PreparedStatement stmt = con.prepareStatement("UPDATE media, SET media_id=(?), title=(?), medium=(?), album_format=(?), year=(?),band_id=(?), WHERE media_id=(?)");
            //updating media values
            stmt.setInt(1,newMedia.getID());
            stmt.setString(2,newMedia.getTitle());
            stmt.setString(3,newMedia.getMedium());
            stmt.setString(4,newMedia.getFormat());
            stmt.setShort(5,newMedia.getYear());
            stmt.setInt(6,newMedia.getBandID());
            //old ID from the old media object
            stmt.setInt(7,oldID);
            stmt.execute();

        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
    }
}
