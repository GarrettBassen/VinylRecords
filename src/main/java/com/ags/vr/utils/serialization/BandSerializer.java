package com.ags.vr.utils.serialization;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class BandSerializer
{
    /**
     * Gets every entry from the band table and returns it in an array list of strings.
     * The attributes band_id and name are delimited by the | character.
     * @return Array list of band entries.
     */
    public static ArrayList<String> saveBandEntries()
    {
        try
        {
            ArrayList<String> bandEntries = new ArrayList<>();

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM band");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String current = rs.getString("band_id");
                //delimiter
                current += "|";
                current += rs.getString("name");
                bandEntries.add(current);
            }
            return bandEntries;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Band Save Error", e.getMessage());
        }
        return null;
    }

    /**
     * Inserts all data from bandEntries arrayList into the band table.
     * @param bandEntries ArrayList full of band data.
     */
    public static void loadBandEntries(ArrayList<String> bandEntries)
    {
        try
        {
            for(String band : bandEntries)
            {
                String[] bandSplit = band.split("\\|");

                //insert loaded object into database
                PreparedStatement stmt = con.prepareStatement("INSERT INTO band (band_id, name) VALUES (?, ?)");
                //id
                stmt.setString(1, bandSplit[0]);
                //name
                stmt.setString(2, bandSplit[1]);
                stmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Band Load Error", e.getMessage());
        }
    }
}
