package com.ags.vr.utils.serialization;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class GenreLinkerSerializer
{
    public static ArrayList<String> saveGenreLinkerEntries()
    {
        try
        {
            ArrayList<String> genreLinkerEntries = new ArrayList<>();

            PreparedStatement stmt = con.prepareStatement("SELECT * FROM genre_linker");
            ResultSet rs = stmt.executeQuery();

            while(rs.next())
            {
                String current = rs.getString("media_id");
                //delimiter
                current += "|";
                current += rs.getString("genre_id");
                genreLinkerEntries.add(current);
            }

            return genreLinkerEntries;
        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("Genre Linker Save Error", e.getMessage());
        }
        return null;
    }
}
