package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBGenreLinker
{
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
