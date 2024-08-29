package com.ags.vr.utils.database;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBStock
{
    // TODO COMMENT
    public static void Insert(Stock stock)
    {
        // TODO TEST
        try
        {
            PreparedStatement statement = con.prepareStatement("INSERT INTO inventory VALUES (?,?,?,?,?,?,?)");
            statement.setInt(1, stock.getMediaID());
            statement.setInt(2, stock.getFrontGood());
            statement.setInt(3, stock.getFrontFair());
            statement.setInt(4, stock.getFrontPoor());
            statement.setInt(5, stock.getBackGood());
            statement.setInt(6, stock.getBackFair());
            statement.setInt(7, stock.getBackPoor());
            statement.execute();
        }
        catch (SQLException e)
        {
            // TODO UPDATE ERROR
            Graphical.ErrorPopup("Database Error", e.toString());
        }
    }

    /**
     * Updates the values of the stock table with new values from a provided Stock object.
     * @param stock Stock object of which the stock table will get its new values from.
     * @return True if the stock was updated successfully, otherwise false.
     * @throws SQLException
     */
    public static boolean Modify(Stock stock) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //updated values of stock table
        String[] data = stock.getData();

        try
        {
            // TODO MAKE LOOK GOOD IF POSSIBLE
            //updating the table with all the values from the stock object
            stmt = con.prepareStatement("UPDATE inventory SET front_good = (?), front_fair = (?), front_poor = (?), back_good = (?), back_fair = (?), back_poor = (?) WHERE media_id = (?)");
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
    public static boolean Delete(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //mediaID, used to find which stock entry to delete.
        String mediaID = String.valueOf(media.getMedia_ID());

        try
        {
            //deleting the stock table entry
            stmt = con.prepareStatement("DELETE FROM inventory WHERE media_id = (?)");
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
