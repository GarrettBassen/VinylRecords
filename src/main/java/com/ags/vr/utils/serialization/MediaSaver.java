package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class MediaSaver {
    /**
     * Gets every entry from the media table and returns it in an array list of media objects.
     * @return Arraylist of media entries
     */
    public static ArrayList<Media> saveMediaEntries()
    {
        try
        {
            ArrayList<Media> mediaEntries = new ArrayList<>();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM media");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                mediaEntries.add(new Media(rs));
            }

            return mediaEntries;
        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("Media Save Error", e.getMessage());
        }
        return null;
    }
}
