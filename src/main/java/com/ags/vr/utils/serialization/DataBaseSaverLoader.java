package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.MonthDay;
import java.time.Year;
import java.util.ArrayList;

public class DataBaseSaverLoader implements java.io.Serializable
{
    //TODO ADD METHOD FOR SERIALIZING THE DB USING THE DB SAVER METHODS
    //TODO ADD METHOD FOR RECONSTRUCTING THE DB USING DESERIALIZED ARRAYLIST

    //Database tables
    private ArrayList<Media> mediaTable;
    private ArrayList<Stock> stockTable;
    private ArrayList<String> bandTable;
    private ArrayList<String> genreTable;
    private ArrayList<String> genreLinkerTable;
    //TODO ADD ARRAY LIST FOR REQUEST TABLE



    /**
     * Initializes the DataBaseSaverLoader object.
     * @param bool True has the constructor initialize all array list. False does not initialize any array list.
     */
    public DataBaseSaverLoader(boolean bool)
    {
        if(bool)
        {
            mediaTable = MediaSaver.saveMediaEntries();
            stockTable = InventorySaver.saveInventoryEntries();
            bandTable = BandSaver.saveBandEntries();
            genreTable = GenreSaver.saveGenreEntries();
            genreLinkerTable = GenreLinkerSaver.saveGenreLinkerEntries();
        }
    }

    /**
     * Serializes the database.
     */
    public void save(String filename)
    {
        try
        {
            //Creating file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            //Saving this object
            out.writeObject(this);

            out.close();
            file.close();
        }

        catch(IOException e)
        {
            Graphical.ErrorPopup("Save Error", e.getMessage());
        }
    }

    //TODO IMPLEMENT
    public void load()
    {

    }
}
