package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBStock
{
    /**
     * Creates a media table for the provided media. All stock values are set to 0
     * as the default.
     * @param media Media object of which a table will be created for.
     * @return True if the table was created successfully, otherwise false.
     * @throws SQLException
     */
    public static boolean createStock(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //media ID (used to connect stock to media
        String mediaID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("INSERT INTO stock VALUES (?,?,?,?,?,?,?)");
            stmt.setString(1, mediaID);
            stmt.setString(2, "0");
            stmt.setString(3, "0");
            stmt.setString(4, "0");
            stmt.setString(5, "0");
            stmt.setString(6, "0");
            stmt.setString(7, "0");
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

    /**
     * Updates the values of the stock table with new values from a provided Stock object.
     * @param stock Stock object of which the stock table will get its new values from.
     * @return True if the stock was updated successfully, otherwise false.
     * @throws SQLException
     */
    public static boolean modifyStock(Stock stock) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //updated values of stock table
        String[] data = stock.getData();

        try
        {
            // TODO MAKE LOOK GOOD IF POSSIBLE
            //updating the table with all the values from the stock object
            stmt = con.prepareStatement("UPDATE stock SET front_good = (?), front_fair = (?), front_poor = (?), back_good = (?), back_fair = (?), back_poor = (?) WHERE ID = (?)");
            stmt.setString(1, data[1]);
            stmt.setString(2, data[2]);
            stmt.setString(3, data[3]);
            stmt.setString(4, data[4]);
            stmt.setString(5, data[5]);
            stmt.setString(6, data[6]);
            stmt.setString(7, data[0]);
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

    /**
     * Deletes the stock entry from the database. Uses a media object to find and
     * delete its corresponding stock entry.
     * @param media Media object that will have its corresponding stock entry deleted.
     * @return True if the table was successfully deleted.
     * @throws SQLException
     */
    public static boolean deleteStock(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //mediaID, used to find which stock entry to delete.
        String mediaID = String.valueOf(media.getID());

        try
        {
            //deleting the stock table entry
            stmt = con.prepareStatement("DELETE FROM stock WHERE ID = (?)");
            stmt.setString(1, mediaID);
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
