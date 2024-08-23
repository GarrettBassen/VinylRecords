package com.ags.vr.utils;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import static com.ags.vr.utils.Connector.con;

public class Database
{
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              GENRE METHODS                                                */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              GENRE LINKER                                                 */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              BAND METHODS                                                 */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
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
     * TODO COMMENT AND TEST
     * @param bandID
     * @return
     */
    public static String getBand(int bandID)
    {
        try
        {
            // Get band by ID
            PreparedStatement statement = con.prepareStatement("SELECT name FROM bands WHERE ID=?");
            statement.setInt(1,bandID);
            ResultSet result = statement.executeQuery();

            // Get band name
            result.next();
            return result.getString(1);
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("SQL ERROR","Error getting band.\ngetBand() | Database.java");
            return "__NULL_ERROR";
        }
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


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              BAND LINKER                                                  */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static boolean createBandLink(Media media, String band) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //IDs of the media and band
        String mediaID = String.valueOf(media.getID());
        String bandID = String.valueOf(DBHelper.StringHash(band));

        try
        {
            //updating the band_ID
            stmt = con.prepareStatement("UPDATE media SET band_ID = (?) WHERE ID = (?)");
            stmt.setString(1,bandID);
            stmt.setString(2,mediaID);
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


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              MEDIA METHODS                                                */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * Inserts the provided media object into the database.
     * @param media Media object to be inserted.
     * @return True if insert was successful; false otherwise.
     */
    public static boolean insertMedia(Media media)
    {
        try
        {
            //adding all values to database
            String[] data = media.getData();
            PreparedStatement stmt = con.prepareStatement("INSERT INTO media VALUES (?,?,?,?,?,?)");
            stmt.setString(1, data[0]);
            stmt.setString(2, data[1]);
            stmt.setString(3, data[2]);
            stmt.setString(4, data[3]);
            stmt.setString(5, data[4]);
            stmt.setString(6, data[5]);
            stmt.execute();

            return true;
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error", e.toString());
            return false;
        }
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              STOCK METHODS                                                */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Creates a media table for the provided media. All stock values are set to 0
     * as the default.
     * @param media Media object of which a table will be created for.
     * @return True if the table was created successfully, otherwise false.
     * @throws SQLException
     */
    public static boolean createStock(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //media ID (used to connect stock to media
        String mediaID = String.valueOf(media.getID());

        try
        {
            stmt = con.prepareStatement("INSERT INTO stock VALUES (?,?,?,?,?,?,?)");
            stmt.setString(1, mediaID);
            stmt.setString(2, "0");
            stmt.setString(3, "0");
            stmt.setString(4, "0");
            stmt.setString(5, "0");
            stmt.setString(6, "0");
            stmt.setString(7, "0");
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
     * Updates the values of the stock table with new values from a provided Stock object.
     * @param stock Stock object of which the stock table will get its new values from.
     * @return True if the stock was updated successfully, otherwise false.
     * @throws SQLException
     */
    public static boolean modifyStock(Stock stock) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //updated values of stock table
        String[] data = stock.getData();

        try
        {
            // TODO MAKE LOOK GOOD IF POSSIBLE
            //updating the table with all the values from the stock object
            stmt = con.prepareStatement("UPDATE stock SET front_good = (?), front_fair = (?), front_poor = (?), back_good = (?), back_fair = (?), back_poor = (?) WHERE ID = (?)");
            stmt.setString(1, data[1]);
            stmt.setString(2, data[2]);
            stmt.setString(3, data[3]);
            stmt.setString(4, data[4]);
            stmt.setString(5, data[5]);
            stmt.setString(6, data[6]);
            stmt.setString(7, data[0]);
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
     * Deletes the stock entry from the database. Uses a media object to find and
     * delete its corresponding stock entry.
     * @param media Media object that will have its corresponding stock entry deleted.
     * @return True if the table was successfully deleted.
     * @throws SQLException
     */
    public static boolean deleteStock(Media media) throws SQLException
    {
        PreparedStatement stmt = null;
        boolean bool = false;

        //mediaID, used to find which stock entry to delete.
        String mediaID = String.valueOf(media.getID());

        try
        {
            //deleting the stock table entry
            stmt = con.prepareStatement("DELETE FROM stock WHERE ID = (?)");
            stmt.setString(1, mediaID);
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

    // TODO ALLOWS MODIFICATION AND COLLECTION OF DATA FROM DATABASE
}
