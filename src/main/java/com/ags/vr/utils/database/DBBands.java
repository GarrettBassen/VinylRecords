package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBBands
{
    /**
     * Insets a band into the database by name.
     * @param band Band Name
     * @return True if insert was successful; false otherwise
     */
    public static boolean Insert(String band)
    {
        try
        {
            //inserting the band into the database
            PreparedStatement statement = con.prepareStatement("INSERT INTO bands VALUES (?,?)");
            statement.setInt   (1, Hash.StringHash(band));
            statement.setString(2, band);
            statement.execute();
            return true;
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }
    }

    /**
     * Checks if the given band exists within the database.
     * @param band Band Name
     * @param create Create band if it does not exist
     * @return True if the band exists; false otherwise
     */
    public static boolean Contains(String band, boolean create)
    {
        try
        {
            //searching for the ID in the database
            PreparedStatement statement = con.prepareStatement("SELECT * FROM bands WHERE band_id=?");
            statement.setInt(1,Hash.StringHash(band));
            boolean exists = statement.executeQuery().next();

            if (create && !exists)
            {
                return Insert(band);
            }
            else
            {
                return exists;
            }
        }
        catch (SQLException e)
        {
            // TODO FIX ERROR
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }
    }

    /**
     * TODO COMMENT AND TEST
     * @param bandID
     * @return
     */
    public static String GetName(int bandID)
    {
        try
        {
            // Get band by ID
            PreparedStatement statement = con.prepareStatement("SELECT name FROM bands WHERE ID=?");
            statement.setInt(1,bandID);
            ResultSet result = statement.executeQuery();

            // Get band name
            result.next();
            return result.getString(1);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("SQL ERROR","Error getting band.\ngetBand() | Database.java");
            return "__NULL_ERROR";
        }
    }

    /**
     * Removed a band from the database.
     * @param band name of band to be removed
     * @return true if the band was removed or was already not in the database, false otherwise.
     * @throws SQLException
     */
    public static boolean Delete(String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(band));

        try
        {
            //deleting band from the database
            stmt = con.prepareStatement("DELETE FROM bands WHERE ID = (?)");
            stmt.setString(1,ID);
            stmt.execute();
            //as band was deleted so updating bool
            bool = true;
        }
        catch (SQLException e)
        {
            //throwing error
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              BAND LINKER                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static boolean createBandLink(Media media, String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //IDs of the media and band
        String mediaID = String.valueOf(media.getID());
        String bandID = String.valueOf(Hash.StringHash(band));

        try
        {
            //updating the band_ID
            stmt = con.prepareStatement("UPDATE media SET band_ID = (?) WHERE ID = (?)");
            stmt.setString(1,bandID);
            stmt.setString(2,mediaID);
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
