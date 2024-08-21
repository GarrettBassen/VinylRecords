package com.ags.vr.utils;

import com.ags.vr.objects.Media;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import static com.ags.vr.utils.Connector.con;


public class Database
{
    /**
     * Inserts media object into database.
     * @param media Media object with data
     * @return True if successful; false otherwise
     */
    public static boolean InsertMedia(Media media)
    {
        // TODO TEST
        try
        {
            // Get data from media object
            String[] data = media.getData();

            // Create statement and execute
            PreparedStatement statement = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?)");
            statement.setString(1,data[0]);
            statement.setString(2,data[1]);
            statement.setString(3,data[2]);
            statement.setString(4,data[3]);
            statement.setString(5,data[4]);
            statement.execute();
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }

        return true;
    }

    /**
     * Inserts a genre into the database.
     * @param genre name of genera added
     * @return true if the method ran successfully, false if the method failed
     * @throws SQLException
     */
    public static boolean insertGenre(String genre) throws SQLException
    {
        boolean bool = false;

        //copy of genre, will be modified to a standard input for hashing
        String hashStr = genre.toLowerCase();
        hashStr = hashStr.strip();

        //hash code that will be stored as the genres ID
        int hash = generalUtil.hash(hashStr);
        String ID = String.valueOf(hash);


        try
        {
            //inserting the genera into the database
            PreparedStatement stmt = con.prepareStatement("INSERT INTO genres VALUES (?,?)");
            stmt.setString(1, ID);
            stmt.setString(2,genre);
            stmt.execute();
            //as genre was added so updating bool
            bool = true;
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }

        return bool;
    }

    /**
     * Checks if the given genre is contained within genre table
     * @param genre name of genre to be checked
     * @return true if the genre is contained, false if not
     * @throws SQLException
     */
    public static boolean containsGenre(String genre) throws SQLException{
        //statement and result
        Statement stmt = null;
        ResultSet rs = null;
        //to be returned
        boolean bool = false;

        //hash value (ID) of genre
        genre = genre.toLowerCase();
        genre = genre.strip();
        int ID = generalUtil.hash(genre);


        try
        {
            stmt = con.createStatement();
            //checking if genre is in database
            rs = stmt.executeQuery("SELECT * FROM genres");

            //checking result
            rs = stmt.getResultSet();

            //loop through result to search for genre
            while(rs.next())
            {
                //if genre is found update bool to true
                if(rs.getInt("ID")==ID)
                {
                    bool = true;
                }
            }
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }

        finally
        {
            if(rs != null){
                rs.close();
            }

            if(stmt != null){
                stmt.close();
            }
        }
        return bool;
    }

    /**
     * Deletes genre
     * @param genre genre to be deleted
     * @return true if the genre was successfully deleted, false otherwise
     * @throws SQLException
     */
    public static boolean deleteGenre(String genre) throws SQLException
    {
        boolean bool = false;

        //hash value (ID) of genre
        genre = genre.toLowerCase();
        genre = genre.strip();
        String ID = Integer.toString(generalUtil.hash(genre));

        try
        {
            //inserting the genera into the database
            PreparedStatement stmt = con.prepareStatement("DELETE FROM genres WHERE ID = (?)");
            stmt.setString(1,ID);
            stmt.execute();
            //as genre was added so updating bool
            bool = true;
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }

        return bool;
    }

    // TODO ALLOWS MODIFICATION AND COLLECTION OF DATA FROM DATABASE
}
