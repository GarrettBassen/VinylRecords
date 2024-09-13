package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import static com.ags.vr.utils.Connector.con;

public class DBGenreLinker
{
    public static boolean Insert(int mediaID, int genreID)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("INSERT INTO genre_linker VALUE (?,?)");
            statement.setInt(1,mediaID);
            statement.setInt(2,genreID);
            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            // TODO FIX ERROR
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Destroys the connection between a media and a genre
     * @param mediaID The media ID
     * @param genreID The genreID
     * @return True if the deletion was successful; false otherwise.
     */
    public static boolean Delete(int mediaID, int genreID)
    {
        try
        {
            PreparedStatement stmt = con.prepareStatement("DELETE FROM genre_linker WHERE media_id=? AND genre_id=?");
            stmt.setInt(1,mediaID);
            stmt.setInt(2,genreID);
            stmt.execute();
            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Linker Deletion Failed", e.getMessage());
        }
        return false;
    }

    /**
     * Returns all genre ID's associated with Media object.
     * @param media Media object
     * @return Int[] genre IDs
     */
    public static int[] getGenres(Media media)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM genre_linker WHERE media_id=?");
            statement.setInt(1,media.getID());
            ResultSet result = statement.executeQuery();

            LinkedList<Integer> genres = new LinkedList<>();
            while (result.next())
            {
                genres.addLast(result.getInt("genre_id"));
            }

            int[] genre_array = new int[genres.size()];
            int i = 0;

            while (!genres.isEmpty())
            {
                genre_array[i++] = genres.pop();
            }

            return genre_array;
        }
        catch (SQLException e)
        {
            // TODO FIX ERROR
            System.out.println(e.getMessage());
            return new int[] {};
        }
    }
}
