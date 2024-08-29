package com.ags.vr.utils.database;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.ags.vr.utils.Connector.con;

public class DBGenres
{
    /**
     * Inserts a genre into the database.
     * @param genre name of genera added.
     * @return true if the method ran successfully, false if the method failed.
     * @throws SQLException
     */
    public static boolean insertGenre(String genre) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(genre));

        try
        {
            //inserting the genera into the database
            stmt = con.prepareStatement("INSERT INTO genre VALUES (?,?)");
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
        finally
        {
            if(stmt != null){
                stmt.close();
            }
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
        //statement and result set
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //to be returned
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(genre));

        try
        {
            //searching for the ID in the database
            stmt = con.prepareStatement("SELECT * FROM genre WHERE genre_id = (?)");
            stmt.setString(1, ID);
            stmt.execute();
            rs = stmt.getResultSet();

            //if there is something within the result set then the genre is contained
            if(rs.next())
            {
                bool = true;
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
     * Deletes genre.
     * @param genre Genre to be deleted.
     * @return true if the genre was successfully deleted or if the genre
     * was already in the database, false otherwise.
     * @throws SQLException
     */
    public static boolean deleteGenre(String genre) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(Hash.StringHash(genre));

        try
        {
            //deleting the genera from the database
            stmt = con.prepareStatement("DELETE FROM genres WHERE genre_id = (?)");
            stmt.setString(1,ID);
            stmt.execute();
            //as genre was deleted so updating bool
            bool = true;
        }
        catch (SQLException e)
        {
            //throwing error
            Graphical.ErrorPopup("Database Error", e.toString());
        }
        finally
        {
            if(stmt != null)
            {
                stmt.close();
            }
        }

        return bool;
    }
}
