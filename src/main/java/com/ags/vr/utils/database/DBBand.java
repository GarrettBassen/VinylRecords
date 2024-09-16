package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBBand
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
            PreparedStatement statement = con.prepareStatement("INSERT INTO band VALUES (?,?)");
            int bandID = Hash.StringHash(band);
            statement.setInt(1, bandID);
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
     * @return True if the band exists; false otherwise
     */
    public static boolean Contains(String band)
    {
        try
        {
            //searching for the ID in the database
            PreparedStatement statement = con.prepareStatement("SELECT * FROM band WHERE band_id=?");
            statement.setInt(1,Hash.StringHash(band));
            ResultSet result = statement.executeQuery();
            return result.next();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", String.format(
                    "Could not find if database contains band '%s'.\n\nCode: %s\n%s",
                    band,e.getErrorCode(),e.getMessage()
            ));
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
            PreparedStatement statement = con.prepareStatement("SELECT name FROM band WHERE band_id=?");
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
    public static void Delete(String band) throws SQLException
    {
        try
        {
            //deleting band from the database
            PreparedStatement stmt  = con.prepareStatement("DELETE FROM band WHERE band_id = (?)");
            stmt.setInt(1,Hash.StringHash(band));
            stmt.execute();
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }
    }
}
