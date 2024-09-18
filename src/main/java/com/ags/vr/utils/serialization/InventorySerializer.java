package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBInventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class InventorySerializer
{
    //TODO COULD BE FASTER
    /**
     * Gets every entry from the inventory table and returns it in an array list of stock objects.
     * @return Array list of inventory entries.
     */
    public static ArrayList<Stock> saveInventoryEntries()
    {
        try
        {
            ArrayList<Stock> stockEntries = new ArrayList<>();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM media");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                stockEntries.add(new Stock(new Media(rs)));
            }

            return stockEntries;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Inventory Save Error", e.getMessage());
        }
        return null;
    }


    /**
     * Inserts all data from stockEntries array list into the inventory table.
     * @param stockEntries Arraylist full of inventory data.
     */
    public static void loadInventoryEntries(ArrayList<Stock> stockEntries)
    {
        for (Stock stock : stockEntries)
        {
            DBInventory.Insert(stock);
        }
    }
}
