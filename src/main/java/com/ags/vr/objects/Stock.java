package com.ags.vr.objects;

import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBInventory;
import com.ags.vr.utils.database.DBMedia;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stock implements Serializable
{
    private int media_id;
    private byte frontGood = 0;
    private byte frontFair = 0;
    private byte frontPoor = 0;
    private byte backGood = 0;
    private byte backFair = 0;
    private byte backPoor = 0;

    /**
     * Default constructor
     */
    public Stock() {}

    /**
     * Constructs Stock object using media ID and byte array data.
     * <br \>Byte[] Format:
     * <ul>
     *     <li>[0] Front Good</li>
     *     <li>[1] Front Fair</li>
     *     <li>[2] Front Poor</li>
     *     <li>[3] Back Good</li>
     *     <li>[4] Back Fair</li>
     *     <li>[5] Back Poor</li>
     * </ul>
     * @param media_id Media ID
     * @param data Byte[] stock amounts
     */
    public Stock(int media_id, byte[] data)
    {
        this.media_id = media_id;
        this.frontGood = data[0];
        this.frontFair = data[1];
        this.frontPoor = data[2];
        this.backGood = data[3];
        this.backFair = data[4];
        this.backPoor = data[5];
    }

    /**
     * Constructs stock object from media object.
     * @param media Media object
     */
    public Stock(Media media)
    {
        try
        {
            ResultSet result = DBInventory.getStock(media);
            result.next();

            //setting the values (null assigns 0)
            try
            {
                this.media_id = result.getInt("media_id");
                this.frontGood = result.getByte("front_good");
                this.frontFair = result.getByte("front_fair");
                this.frontPoor = result.getByte("front_poor");
                this.backGood  = result.getByte("back_good");
                this.backFair  = result.getByte("back_fair");
                this.backPoor  = result.getByte("back_poor");
            }
            //there is no stock entry set all to 0
            catch(SQLException e)
            {
                this.media_id = media.getID();
                this.frontGood = 0;
                this.frontFair = 0;
                this.frontPoor = 0;
                this.backGood  = 0;
                this.backFair  = 0;
                this.backPoor  = 0;
            }
        }
        catch (SQLException e)
        {
            Graphical.ErrorPopup("Database Error",String.format(
                    "ERROR CREATING STOCK IN Stock(ResultSet) | Stock.java\n\nCode: %s\n%s\n",
                    e.getErrorCode(),e.getMessage()
            ));
        }
    }

    /**
     * Returns formatted String array of stock data in the following format:
     * <ol>
     *      <li>mediaID</li>
     *      <li>frontGood</li>
     *      <li>frontFair</li>
     *      <li>frontPoor</li>
     *      <li>backGood</li>
     *      <li>backFair</li>
     *      <li>backPoor</li>
     * </ol>
     * @return String array
     */
    public int[] getData()
    {
        int[] data =
                {
                        this.media_id,
                        this.frontGood,
                        this.frontFair,
                        this.frontPoor,
                        this.backGood,
                        this.backFair,
                        this.backPoor
                };

        return data;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              GETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Gets the media_ID.
     * @return The media_ID
     */
    public int getID()
    {
        return this.media_id;
    }

    /**
     * Gets the number of media in front stock of good quality.
     * @return the number of media in front stock of good quality.
     */
    public int getFrontGood()
    {
        return this.frontGood;
    }

    /**
     * Gets the number of media in front stock of fair quality.
     * @return the number of media in front stock of fair quality.
     */
    public int getFrontFair()
    {
        return this.frontFair;
    }

    /**
     * Gets the number of media in front stock of poor quality.
     * @return the number of media in front stock of poor quality.
     */
    public int getFrontPoor()
    {
        return this.frontPoor;
    }

    /**
     * Gets the number of media in back stock of good quality.
     * @return the number of media in back stock of good quality.
     */
    public int getBackGood()
    {
        return this.backGood;
    }

    /**
     * Gets the number of media in back stock of fair quality.
     * @return the number of media in back stock of fair quality.
     */
    public int getBackFair()
    {
        return this.backFair;
    }

    /**
     * Gets the number of media in back stock of poor quality.
     * @return the number of media in back stock of poor quality.
     */
    public int getBackPoor()
    {
        return this.backPoor;
    }

    /**
     * Returns total stock amount.
     * @return Stock amount
     */
    public int getStockTotal()
    {
        return this.frontGood + this.frontFair + this.frontPoor + this.backGood + this.backFair + this.backPoor;
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /*                                              SETTERS                                                      */
    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * WARNING: MAY BREAK MEDIA-STOCK CONNECTION.
     * Changes the current media ID value.
     * @param mediaID New ID that the current media ID will be changed to.
     */
    public void setID(int mediaID)
    {
        // TODO POSSIBLY CREATE 'oldID' INT
        this.media_id = mediaID;
    }

    /**
     * Changes the current frontGood value.
     * @param frontGood New value that will replace the old value.
     */
    public void setFrontGood(byte frontGood)
    {
        this.frontGood = frontGood;
    }

    /**
     * Changes the current frontFair value.
     * @param frontFair New value that will replace the old value.
     */
    public void setFrontFair(byte frontFair)
    {
        this.frontFair = frontFair;
    }

    /**
     * Changes the current frontPoor value.
     * @param frontPoor New value that will replace the old value.
     */
    public void setFrontPoor(byte frontPoor)
    {
        this.frontPoor = frontPoor;
    }

    /**
     * Changes the current backGood value.
     * @param backGood New value that will replace the old value.
     */
    public void setBackGood(byte backGood)
    {
        this.backGood = backGood;
    }

    /**
     * Changes the current backFair value.
     * @param backFair New value that will replace the old value.
     */
    public void setBackFair(byte backFair)
    {
        this.backFair = backFair;
    }

    /**
     * Changes the current backPoor value.
     * @param backPoor New value that will replace the old value.
     */
    public void setBackPoor(byte backPoor)
    {
        this.backPoor = backPoor;
    }
}
