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

    //number of files created
    //TODO CHANGE IMPLEMENTATION SO FILENUM IS NOT SERIALIZED
    private static int fileNum = 0;


    /**
     * Initializes the DataBaseSaverLoader object by initializing all array list with
     * Database table values.
     */
    public DataBaseSaverLoader()
    {
        mediaTable = MediaSaver.saveMediaEntries();
        stockTable = InventorySaver.saveInventoryEntries();
        bandTable = BandSaver.saveBandEntries();
        genreTable = GenreSaver.saveGenreEntries();
        genreLinkerTable = GenreLinkerSaver.saveGenreLinkerEntries();
    }

    /**
     * Serializes the database.
     */
    public void save()
    {
        try
        {
            //generate file name
            String filename = "Database-Save" + Year.now() + "-" + MonthDay.now().getMonthValue() + "-" + MonthDay.now().getDayOfMonth();
            fileNum++;
            filename += fileNum;

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
