package com.AGS.vr.objects;

public class Media
{
    private String title = null;
    private TYPE.medium medium = null;
    private TYPE.format format = null;
    private byte year = -1;
    private int band_id = -1;

    /**
     * Creates media object for use in database.
     * @param title Media Title
     * @param medium TYPE.medium (vinyl, CD, cassette)
     * @param format TYPE.format (Single, EP, LP, DLP)
     * @param year Release Year
     * @param band_id Bind ID
     */
    public Media(String title, TYPE.medium medium, TYPE.format format, byte year, int band_id)
    {
        this.title = title;
        this.medium = medium;
        this.format = format;
        this.year = year;
        this.band_id = band_id;
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
                        Integer.toString(band_id)
                };
    }
}
