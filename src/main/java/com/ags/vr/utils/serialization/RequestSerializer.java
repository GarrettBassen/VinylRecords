package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Request;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBMedia;
import com.ags.vr.utils.database.DBRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

//TODO ADD METHOD
public class RequestSerializer
{
    /**
     * Gets every entry from the request table and returns it in an array list of request objects.
     * @return Arraylist of request entries
     */
    public static ArrayList<Request> saveRequestEntries()
    {
        try
        {
            ArrayList<Request> requestEntries = new ArrayList<>();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM request");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                requestEntries.add(new Request(rs));
            }

            return requestEntries;
        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("Request Save Error", e.getMessage());
        }
        return null;
    }

    /**
     * Inserts all data from requestEntries array list into the media table.
     * @param requestEntries Arraylist full of inventory data.
     */
    public static void loadRequestEntries(ArrayList<Request> requestEntries)
    {
        for(Request request : requestEntries)
        {
            DBRequest.Insert(request);
        }
    }
}
