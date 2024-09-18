package com.ags.vr.utils.serialization;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class GenreSaver
{
    public static ArrayList<String> saveGenreEntries()
    {
        try
        {
            ArrayList<String> genreEntries = new ArrayList<>();

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM genre");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String current = rs.getString("genre_id");
                //delimiter
                current += "|";
                current += rs.getString("name");
                genreEntries.add(current);
            }
            return genreEntries;
        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("Genre Save Error", e.getMessage());
        }
        return null;
    }

}
