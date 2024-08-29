package com.ags.vr.objects;

import com.ags.vr.utils.Graphical;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.ags.vr.utils.Connector.con;

public class Stock
{
    private int mediaID;
    private int frontGood = 0;
    private int frontFair = 0;
    private int frontPoor = 0;
    private int backGood = 0;
    private int backFair = 0;
    private int backPoor = 0;

    /**
     * Default constructor
     */
    public Stock() {}

    /**
     * Constructor.
     * @param media Media object
     */
    public Stock(Media media)
    {
        mediaID = media.getMedia_ID();

        try
        {
            //getting the stock values out of the database
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM inventory WHERE media_id = ?");
            stmt.setInt(1, mediaID);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            //setting the values
            frontGood = rs.getInt("front_good");
            frontFair = rs.getInt("front_fair");
            frontPoor = rs.getInt("front_poor");
            backGood = rs.getInt("back_good");
            backFair = rs.getInt("back_fair");
            backPoor = rs.getInt("back_poor");
        }
        catch(Exception e)
        {
            Graphical.ErrorPopup("Stock Creation Error", e.toString());
        }
    }

    /**
     * Returns formatted String array of stock data in the following format:
     * <ol>
     *     <li>mediaID</li>
     *     <li>frontGood</li>
     *     <li>frontFair</li>
     *     <li>frontPoor</li>
     *     <li>backGood</li>
     *     <li>backFair</li>
     *      <li>backPoor</li>
     * </ol>
     * @return String array
     */
    public int[] getData()
    {
        int[] data =
                {
                        mediaID,
                        frontGood,
                        frontFair,
                        frontPoor,
                        backGood,
                        backFair,
                        backPoor
                };

        return data;
    }

    //Getters
    //******************************************************************************

    /**
     * Gets the media_ID
     * @return The media_ID
     */
    public int getMediaID()
    {
        return mediaID;
    }

    /**
     * Gets the number of media in front stock of good quality.
     * @return the number of media in front stock of good quality.
     */
    public int getFrontGood()
    {
        return frontGood;
    }

    /**
     * Gets the number of media in front stock of fair quality.
     * @return the number of media in front stock of fair quality.
     */
    public int getFrontFair()
    {
        return frontFair;
    }

    /**
     * Gets the number of media in front stock of poor quality.
     * @return the number of media in front stock of poor quality.
     */
    public int getFrontPoor()
    {
        return frontPoor;
    }

    /**
     * Gets the number of media in back stock of good quality.
     * @return the number of media in back stock of good quality.
     */
    public int getBackGood()
    {
        return backGood;
    }

    /**
     * Gets the number of media in back stock of fair quality.
     * @return the number of media in back stock of fair quality.
     */
    public int getBackFair()
    {
        return backFair;
    }

    /**
     * Gets the number of media in back stock of poor quality.
     * @return the number of media in back stock of poor quality.
     */
    public int getBackPoor()
    {
        return backPoor;
    }

    //setters
    //**************************************************************************************

    /**
     * WARNING: MAY BREAK MEDIA-STOCK CONNECTION.
     * Changes the current media ID value.
     * @param mediaID New ID that the current media ID will be changed to.
     */
    public void setMediaID(int mediaID)
    {
        this.mediaID = mediaID;
    }

    /**
     * Changes the current frontGood value.
     * @param frontGood New value that will replace the old value.
     */
    public void setFrontGood(int frontGood)
    {
        this.frontGood = frontGood;
    }

    /**
     * Changes the current frontFair value.
     * @param frontFair New value that will replace the old value.
     */
    public void setFrontFair(int frontFair)
    {
        this.frontFair = frontFair;
    }

    /**
     * Changes the current frontPoor value.
     * @param frontPoor New value that will replace the old value.
     */
    public void setFrontPoor(int frontPoor)
    {
        this.frontPoor = frontPoor;
    }

    /**
     * Changes the current backGood value.
     * @param backGood New value that will replace the old value.
     */
    public void setBackGood(int backGood)
    {
        this.backGood = backGood;
    }

    /**
     * Changes the current backFair value.
     * @param backFair New value that will replace the old value.
     */
    public void setBackFair(int backFair)
    {
        this.backFair = backFair;
    }

    /**
     * Changes the current backPoor value.
     * @param backPoor New value that will replace the old value.
     */
    public void setBackPoor(int backPoor)
    {
        this.backPoor = backPoor;
    }
}
