package com.ags.vr.objects;

/**
 * Media object adds readability and make media-related database operations easier.
 */
public class Media
{
    private String title = null;
    private TYPE.medium medium = null;
    private TYPE.format format = null;
    private byte year = -1;
    private int bandID = -1;

    /**
     * Creates media object for use in database.
     * @param title Media Title
     * @param medium TYPE.medium (vinyl, CD, cassette)
     * @param format TYPE.format (Single, EP, LP, DLP)
     * @param year Release Year
     * @param bandID Bind ID
     */
    public Media(String title, TYPE.medium medium, TYPE.format format, byte year, int bandID)
    {
        this.title = title;
        this.medium = medium;
        this.format = format;
        this.year = year;
        this.bandID = bandID;
    }

    /**
     * Returns formatted String array of media data in the following format:
     * <ol>
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
                        this.title,
                        Integer.toString(this.medium.ordinal()),
                        Integer.toString(this.format.ordinal()),
                        Byte.toString(this.year),
                        Integer.toString(bandID)
                };
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              SETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Sets album title.
     * @param title title
     */
    public void setTitle(String title)
    {
        this.title = title;
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
    public void setYear(byte year)
    {
        this.year = year;
    }

    /**
     * Sets band ID.
     * @param bandID band ID
     */
    public void setBandID(int bandID)
    {
        this.bandID = bandID;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              GETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Returns album title.
     * @return Album title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Returns medium type (Vinyl, CD, Cassette).
     * @return TYPE.medium type
     */
    public TYPE.medium getMedium()
    {
        return medium;
    }

    /**
     * Returns album format (Single, EP, LP, DLP).
     * @return TYPE.format type
     */
    public TYPE.format getFormat()
    {
        return format;
    }

    /**
     * Returns album release year.
     * @return Release year
     */
    public byte getYear()
    {
        return year;
    }

    /**
     * Returns band ID.
     * @return band ID
     */
    public int getBandID()
    {
        return bandID;
    }
}
