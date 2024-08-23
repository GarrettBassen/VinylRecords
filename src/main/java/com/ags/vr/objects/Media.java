package com.ags.vr.objects;

import com.ags.vr.utils.database.DBBands;
import com.ags.vr.utils.database.Hash;

/**
 * Media object adds readability and make media-related database operations easier.
 */
public class Media
{
    private int ID = Integer.MIN_VALUE;
    private int oldID = Integer.MIN_VALUE;
    private String title = "";
    private TYPE.medium medium = null;
    private TYPE.format format = null;
    private short year = Short.MIN_VALUE;
    private String band = "";

    /**
     * Default constructor.
     */
    public Media() {}

    /**
     * Creates media object for use in database.
     * @param title Media Title
     * @param medium TYPE.medium (vinyl, CD, cassette)
     * @param format TYPE.format (Single, EP, LP, DLP)
     * @param year Release Year
     * @param band Band Name
     */
    public Media(String title, TYPE.medium medium, TYPE.format format, short year, String band)
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
     * @param ID Media ID
     * @param title Media Title
     * @param medium TYPE.medium (vinyl, CD, cassette)
     * @param format TYPE.format (Single, EP, LP, DLP)
     * @param year Release Year
     * @param bandID Band ID
     */
    public Media(Integer ID, String title, TYPE.medium medium, TYPE.format format, short year, int bandID)
    {
        this.ID = ID;
        this.title = title;
        this.medium = medium;
        this.format = format;
        this.year = year;
        this.band = DBBands.getBand(bandID);
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
                        Integer.toString(this.ID),
                        this.title,
                        Integer.toString(this.medium.ordinal()),
                        Integer.toString(this.format.ordinal()),
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
        if (this.ID != Integer.MIN_VALUE)
        {
            this.oldID = this.ID;
        }

        // TODO CREATE BAND IN LINKER IF BAND DOES NOT EXIST
        this.ID = Hash.StringHash(this.title,this.band);
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
     * Sets medium type (Vinyl, CD, Cassette).
     * @param medium TYPE.medium type
     */
    public void setMedium(TYPE.medium medium)
    {
        this.medium = medium;
    }

    /**
     * Sets album format (Single, EP, LP, DLP).
     * @param format TYPE.format type
     */
    public void setFormat(TYPE.format format)
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
    public int getID()
    {
        return this.ID;
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
     * Returns medium type (Vinyl, CD, Cassette).
     * @return TYPE.medium type
     */
    public TYPE.medium getMedium()
    {
        return this.medium;
    }

    /**
     * Returns album format (Single, EP, LP, DLP).
     * @return TYPE.format type
     */
    public TYPE.format getFormat()
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
     * @return band name
     */
    public String getBand()
    {
        return this.band;
    }
}
