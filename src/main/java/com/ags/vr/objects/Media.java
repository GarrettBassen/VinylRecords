package com.ags.vr.objects;

import com.ags.vr.utils.database.DBBands;
import com.ags.vr.utils.database.Hash;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Media object adds readability and make media-related database operations easier.
 */
public class Media
{
    // Variables
    private int media_ID = Integer.MIN_VALUE;
    private int oldID = Integer.MIN_VALUE;
    private short year = Short.MIN_VALUE;
    private String title = "";
    private String medium = "";
    private String format = "";
    private String band = "";

    /**
     * Default constructor.
     */
    public Media() {}

    /**
     * Creates media object for use in database.
     * @param title Media Title
     * @param medium vinyl, CD, cassette
     * @param format Single, EP, LP, DLP
     * @param year Release Year
     * @param band Band Name
     */
    public Media(String title, String medium, String format, short year, String band)
    {
        this.title = title;
        this.medium = medium;
        this.format = format;
        this.year = year;
        this.band = band;
        setID();
    }

    /**
     * Creates media object from database.
     * @param media Media ResultSet
     */
    public Media(ResultSet media)
    {
        // TODO TEST
        try
        {
            media.next();
            this.media_ID = media.getInt("media_id");
            this.title = media.getString("title");
            this.medium = media.getString("medium");
            this.format = media.getString("album_format");
            this.year = media.getShort("year");
            this.band = DBBands.GetName(media.getInt("band_id"));
        }
        catch (SQLException e)
        {
            // TODO DISPLAY ERROR
        }
    }

    /**
     * Returns formatted String array of media data in the following format:
     * <ol>
     *     <li>ID</li>
     *     <li>Title</li>
     *     <li>Medium</li>
     *     <li>Album Format</li>
     *     <li>Year</li>
     *     <li>Band ID</li>
     * </ol>
     * @return String array
     */
    public String[] getData()
    {
        return new String[]
                {
                        Integer.toString(this.media_ID),
                        this.title,
                        this.medium,
                        this.format,
                        Short.toString(this.year),
                        Integer.toString(Hash.StringHash(this.band))
                };
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              SETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Saves original ID to oldID and sets new ID as a hash of the concatenated album name and band name.
     */
    private void setID()
    {
        // Do nothing if one or more hash input is null
        if (this.title.isBlank() || this.band.isBlank()) { return; }

        // Save ID for database safety if media
        if (this.media_ID != Integer.MIN_VALUE)
        {
            this.oldID = this.media_ID;
        }

        // TODO CREATE BAND IN LINKER IF BAND DOES NOT EXIST
        this.media_ID = Hash.StringHash(this.title,this.band);
    }

    /**
     * Sets album title and updates media ID.
     * @param title title
     */
    public void setTitle(String title)
    {
        this.title = title;
        setID();
    }

    /**
     * Sets medium type.
     * @param medium vinyl, CD, cassette
     */
    public void setMedium(String medium)
    {
        this.medium = medium;
    }

    /**
     * Sets album format.
     * @param format single, EP, LP, DLP
     */
    public void setFormat(String format)
    {
        this.format = format;
    }

    /**
     * Sets release year.
     * @param year year
     */
    public void setYear(short year)
    {
        this.year = year;
    }

    /**
     * Sets band name and updates media ID.
     * @param band band Name
     */
    public void setBand(String band)
    {
        this.band = band;
        this.setID();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              GETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Returns media ID.
     * @return Media ID.
     */
    public int getMedia_ID()
    {
        return this.media_ID;
    }

    /**
     * Returns old ID if ID was changed due to album or band name modification. Used to delete previous entry
     * from database before adding new media object with correct ID.
     * @return Old ID or Integer.MIN_VALUE if ID has not changed.
     */
    public int getOldID()
    {
        return this.oldID;
    }

    /**
     * Returns album title.
     * @return Album title
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * Returns medium type.
     * @return vinyl, CD, cassette
     */
    public String getMedium()
    {
        return this.medium;
    }

    /**
     * Returns album format.
     * @return single, EP, LP, DLP
     */
    public String getFormat()
    {
        return this.format;
    }

    /**
     * Returns album release year.
     * @return Release year
     */
    public short getYear()
    {
        return this.year;
    }

    /**
     * Returns band name.
     * @return Band Name
     */
    public String getBand()
    {
        return this.band;
    }

    /**
     * Returns band ID hash.
     * @return Band ID
     */
    public int getBandID()
    {
        return Hash.StringHash(this.band);
    }
}
