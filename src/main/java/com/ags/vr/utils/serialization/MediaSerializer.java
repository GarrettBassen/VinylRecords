package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBMedia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class MediaSerializer {
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

    /**
     * Inserts all data from mediaEntries array list into the media table.
     * @param mediaEntries Arraylist full of inventory data.
     */
    public static void loadMediaEntries(ArrayList<Media> mediaEntries)
    {
        for(Media media : mediaEntries)
        {
            DBMedia.Insert(media);
        }
    }
}
