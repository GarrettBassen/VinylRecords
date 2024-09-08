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


    /**
     * Creates the connection between the media table and the genres table through an
     * intermediary table genre_linker. Uses the IDs of media and genres to create the connection.
     * @param media Media object that is to be connected with a genre.
     * @param genre Name of Genre that will be connected to a media.
     * @return True if the connection was successfully made, otherwise false.
     * @throws SQLException
     */
    public boolean createGenreLink(Media media, String genre) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;
        String genreID = String.valueOf(Hash.StringHash(genre));
        String mediaID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("INSERT INTO genre_linker VALUES (?,?)");
            stmt.setString(1,mediaID);
            stmt.setString(2,genreID);
            stmt.execute();
            bool = true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
        finally
        {
            if(stmt != null)
            {
                stmt.close();
            }
        }
        return bool;
    }
}
