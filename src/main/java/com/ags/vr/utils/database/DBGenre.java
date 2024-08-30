package com.ags.vr.utils.database;

import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBGenre
{
    // TODO TEST
    public static boolean Insert(String genre)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("INSERT INTO genre VALUES (?,?)");
            statement.setInt(1,Hash.StringHash(genre));
            statement.setString(2,genre);
            statement.execute();
            return true;
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

    // TODO TEST
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

    // TODO TEST
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
}
