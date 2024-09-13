package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

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
            result.next();
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
            result.next();
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
            //delete the associated stock table
            PreparedStatement stmt = con.prepareStatement("DELETE FROM inventory WHERE media_id=?");
            stmt.setInt(1,media.getID());
            stmt.execute();
            stmt.close();

            //delete the associated genre linker
            stmt = con.prepareStatement("DELETE FROM genre_linker WHERE media_id=?");
            stmt.setInt(1,media.getID());
            stmt.execute();
            stmt.close();

            //delete the media object
            stmt = con.prepareStatement("DELETE FROM media WHERE media_id=?");
            stmt.setInt(1,media.getID());
            stmt.execute();
            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }
    }

//TODO WRITE HERE
    public static void Update(Media newMedia, Media oldMedia)
    {
        //delete the old media
        Delete(oldMedia);
        //create new media
        Insert(newMedia);
    }

    /**
     * Returns the all the genres associated with the media entry as a string array.
     * @param media Media entry.
     * @return All associated genres.
     */
    public static int[] getGenres(Media media)
    {
        try {
            //statement to get all genre media links
            PreparedStatement statement = con.prepareStatement("SELECT genre_id FROM genre_linker WHERE media_id=?");
            statement.setInt(1, media.getID());
            ResultSet result = statement.executeQuery();

            // Gets all genre IDs
            Stack<Integer> stack = new Stack<>();
            while (result.next())
            {
                stack.push(result.getInt(1));
            }

            // Adds genres to int array
            int[] genres = new int[stack.size()];
            for (int i = 0; i < genres.length; ++i)
            {
                genres[i] = stack.pop();
            }

            return genres;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", String.format(
                    "Could not get genres for '%s' in getGenres(Media) | DBMedia.java\n\nCode: %s\n%s",
                    media.getTitle(), e.getErrorCode(), e.getMessage()
            ));
            return null;
        }
    }
}
