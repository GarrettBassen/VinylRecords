package com.ags.vr.utils.serialization;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class GenreSerializer
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

    /**
     * Inserts all data from genreEntries arrayList into the genre_linker table.
     * @param genreEntries ArrayList full of genre data.
     */
    public static void loadGenreEntries(ArrayList<String> genreEntries)
    {
        try
        {
            for(String genre : genreEntries)
            {
                String[] genreSplit = genre.split("\\|");

                //insert loaded object into database
                PreparedStatement stmt = con.prepareStatement("INSERT INTO genre (genre_id, name) VALUES (?, ?)");
                //id
                stmt.setString(1, genreSplit[0]);
                //name
                stmt.setString(2, genreSplit[1]);
                stmt.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Genre Load Error", e.getMessage());
        }
    }

}
