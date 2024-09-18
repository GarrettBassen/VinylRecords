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

    /**
     * Inserts all data from genreLinkerEntries arrayList into the genre_linker table.
     * @param genreLinkerEntries ArrayList full of genre_linker data.
     */
    public static void loadGenreLinkerEntries(ArrayList<String> genreLinkerEntries)
    {
        try
        {
            for(String genreLinker : genreLinkerEntries)
            {
                String[] genreLinkerSplit = genreLinker.split("\\|");

                //insert loaded object into database
                PreparedStatement stmt = con.prepareStatement("INSERT INTO genre_linker (media_id, genre_id) VALUES (?, ?)");
                //media id
                stmt.setString(1, genreLinkerSplit[0]);
                //genre id
                stmt.setString(2, genreLinkerSplit[1]);
                stmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Genre Linker Load Error", e.getMessage());
        }
    }
}
