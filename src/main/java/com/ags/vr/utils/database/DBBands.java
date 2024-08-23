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
     * Insets a band into the database,
     * @param band name of band that is to be inserted
     * @return true if the band is successfully inserted, false otherwise
     * @throws SQLException
     */
    public static boolean insertBand(String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(band));

        try
        {
            //inserting the band into the database
            stmt = con.prepareStatement("INSERT INTO bands VALUES (?,?)");
            stmt.setString(1, ID);
            stmt.setString(2, band);
            stmt.execute();
            //as band was added so updating bool
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

    /**
     * Checks if the inputted band is contained within the database.
     * @param band name of band being searched for.
     * @return True if the band is within the database, otherwise false.
     * @throws SQLException
     */
    public static boolean containsBand(String band) throws SQLException
    {
        //statement and result set
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //to be returned
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(band));

        try
        {
            //searching for the ID in the database
            stmt = con.prepareStatement("SELECT * FROM bands WHERE ID = (?)");
            stmt.setString(1, ID);
            stmt.execute();
            rs = stmt.getResultSet();

            //if there is something within the result set then the band is contained
            if(rs.next())
            {
                bool = true;
            }

        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }

        finally
        {
            if(rs != null){
                rs.close();
            }

            if(stmt != null){
                stmt.close();
            }
        }

        return bool;
    }

    /**
     * TODO COMMENT AND TEST
     * @param bandID
     * @return
     */
    public static String getBand(int bandID)
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
    public static boolean deleteBand(String band) throws SQLException
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
