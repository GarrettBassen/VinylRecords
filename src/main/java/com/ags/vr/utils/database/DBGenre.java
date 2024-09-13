package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBGenre
{
    public static boolean Insert(String genre)
    {
        try
        {
            //check if the genre is already in the system
            if(!Contains(genre))
            {
                PreparedStatement statement = con.prepareStatement("INSERT INTO genre VALUES (?,?)");
                statement.setInt(1,Hash.StringHash(genre));
                statement.setString(2,genre);
                statement.execute();
                return true;
            }
            else
            {
                throw new SQLException("Genre already exists");
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error inserting '%s' in Insert(String) | DBGenre.java\n\nCode: %s\n%s",
                    genre,e.getErrorCode(),e.getMessage()
            ));
            return false;
        }
    }

    public static boolean Contains(String genre)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM genre WHERE genre_id=?");
            statement.setInt(1,Hash.StringHash(genre));
            return statement.executeQuery().next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error selecting '%s' in Contains(String) | DBGenre.java\n\nCode: %s\n%s",
                    genre,e.getErrorCode(),e.getMessage()
            ));
            return false;
        }
    }

    //TODO TEST
    public static boolean Delete(String genre)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("DELETE FROM genre WHERE genre_id=?");
            statement.setInt(1,Hash.StringHash(genre));
            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "Error deleting '%s' in Delete(String) | DBGenre.java\n\nCode: %s\n%s",
                    genre,e.getErrorCode(),e.getMessage()
            ));
            return false;
        }
    }

    /**
     * Gets name of n genre IDs returned in string array.
     * @param genreID n genre IDs
     * @return Genre string array
     */
    public static String[] getName(int... genreID)
    {
        try
        {
            String[] names = new String[genreID.length];

            // Get string name for all genres
            for (int i = 0; i < genreID.length; ++i)
            {
                PreparedStatement statement = con.prepareStatement("SELECT name FROM genre WHERE genre_id=?");
                statement.setInt(1,genreID[i]);
                ResultSet result = statement.executeQuery();
                result.next();
                names[i] = result.getString(1);
            }

            return names;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", String.format(
                    "Could not get genre name(s) in getName(int...) | DBGenre.java\n\nCode: %s\n%s",
                    e.getErrorCode(), e.getMessage()
            ));
            return null;
        }
    }
}
