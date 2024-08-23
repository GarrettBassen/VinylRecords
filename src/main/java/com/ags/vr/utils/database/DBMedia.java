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
            String[] data = media.getData();

            PreparedStatement stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
            stmt.setString(1, data[0]);
            stmt.setString(2, data[1]);
            stmt.setString(3, data[2]);
            stmt.setString(4, data[3]);
            stmt.setString(5, data[4]);
            stmt.setString(6, data[5]);
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
    public static boolean ContainsMedia(int ID)
    {
        try
        {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM media WHERE 'ID'=?");
            statement.setInt(1,ID);
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            System.err.println("ERROR IN MediaExists(int ID) DBHelper.java");
            return false;
        }
    }

    /**
     * Removes the provided media from the database.
     * @param media Media object to be delted.
     * @return True if the media was succsefully deleted or already not within
     * the database, otherwise false.
     * @throws SQLException
     */
    public static boolean deleteMedia(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //ID
        String ID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("DELETE FROM media WHERE ID = (?)");
            stmt.setString(1, ID);
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
