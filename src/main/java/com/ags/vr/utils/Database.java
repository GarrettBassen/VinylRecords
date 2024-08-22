package com.ags.vr.utils;

import com.ags.vr.objects.Media;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
     * @param genre name of genera added.
     * @return true if the method ran successfully, false if the method failed.
     * @throws SQLException
     */
    public static boolean insertGenre(String genre) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(DBHelper.StringHash(genre));

        try
        {
            //inserting the genera into the database
            stmt = con.prepareStatement("INSERT INTO genres VALUES (?,?)");
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
        String ID = String.valueOf(DBHelper.StringHash(genre));

        try
        {
            //searching for the ID in the database
            stmt = con.prepareStatement("SELECT * FROM genres WHERE ID = (?)");
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
        String ID = String.valueOf(DBHelper.StringHash(genre));

        try
        {
            //deleting the genera from the database
            stmt = con.prepareStatement("DELETE FROM genres WHERE ID = (?)");
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

    /**
     * Creates the connection between the media table and the genres table through an
     * intermediary table genre_linker. Uses the IDs of media and genres to create the connection.
     * @param media Media object that is to be connected with a genre.
     * @param genre Name of Genre that will be connected to a media.
     * @return True if the connection was successfully made, otherwise false.
     * @throws SQLException
     */
    public boolean createGenreLink(Media media, String genre) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;
        String genreID = String.valueOf(DBHelper.StringHash(genre));
        String mediaID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("INSERT INTO genre_linker VALUES (?,?)");
            stmt.setString(1,mediaID);
            stmt.setString(2,genreID);
            stmt.execute();
            bool = true;
        }
        catch (SQLException e)
        {
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


    /**
     * Insets a band into the database,
     * @param band name of band that is to be inserted
     * @return true if the band is successfully inserted, false otherwise
     * @throws SQLException
     */
    public static boolean insertBand(String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(DBHelper.StringHash(band));

        try
        {
            //inserting the band into the database
            stmt = con.prepareStatement("INSERT INTO bands VALUES (?,?)");
            stmt.setString(1, ID);
            stmt.setString(2, band);
            stmt.execute();
            //as band was added so updating bool
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

    /**
     * Checks if the inputted band is contained within the database.
     * @param band name of band being searched for.
     * @return True if the band is within the database, otherwise false.
     * @throws SQLException
     */
    public static boolean containsBand(String band) throws SQLException
    {
        //statement and result set
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //to be returned
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(DBHelper.StringHash(band));

        try
        {
            //searching for the ID in the database
            stmt = con.prepareStatement("SELECT * FROM bands WHERE ID = (?)");
            stmt.setString(1, ID);
            stmt.execute();
            rs = stmt.getResultSet();

            //if there is something within the result set then the band is contained
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
     * Removed a band from the database.
     * @param band name of band to be removed
     * @return true if the band was removed or was already not in the database, false otherwise.
     * @throws SQLException
     */
    public static boolean deleteBand(String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //Generated String ID
        String ID = String.valueOf(DBHelper.StringHash(band));

        try
        {
            //deleting band from the database
            stmt = con.prepareStatement("DELETE FROM bands WHERE ID = (?)");
            stmt.setString(1,ID);
            stmt.execute();
            //as band was deleted so updating bool
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

    /**
     * Inserts the provided media into the database
     * @param media Media object to be inserted.
     * @return True if the media was successfully inserted, otherwise false.
     * @throws SQLException
     */
    public static boolean insertMedia(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //data
        String[] data = media.getData();

        try
        {
            //adding all values to database
           stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
           stmt.setString(1, data[0]);
           stmt.setString(2, data[1]);
           stmt.setString(3, data[2]);
           stmt.setString(4, data[3]);
           stmt.setString(5, data[4]);
           stmt.setString(6, data[5]);
           stmt.execute();
           //data successfully added
           bool = true;
        }
        catch (SQLException e)
        {
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

    /**
     * Checks if the provided media is within the database.
     * @param media Media object to be searched for.
     * @return True if the media is within the database, otherwise false.
     * @throws SQLException
     */
    public static boolean containsMedia(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean bool = false;
        String ID = String.valueOf(media.getID());

        try
        {
            //running query (searching for same ID)
            stmt = con.prepareStatement("SELECT * FROM media WHERE ID = (?)");
            stmt.setString(1, ID);
            stmt.execute();
            rs = stmt.getResultSet();

            //if there is something in rs then the media is contained within the database
            if(rs.next())
            {
                bool = true;
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
        }
        finally
        {
            if(rs != null)
            {
                rs.close();
            }

            if(stmt != null)
            {
                stmt.close();
            }
        }

        return bool;
    }

    /**
     * Removes the provided media from the database.
     * @param media Media object to be delted.
     * @return True if the media was succsefully deleted or already not within
     * the database, otherwise false.
     * @throws SQLException
     */
    public static boolean deleteMedia(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //ID
        String ID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("DELETE FROM media WHERE ID = (?)");
            stmt.setString(1, ID);
            stmt.execute();
            bool = true;
        }
        catch (SQLException e)
        {
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

    // TODO TEST MEDIA METHODS
    // TODO ALLOWS MODIFICATION AND COLLECTION OF DATA FROM DATABASE
}
