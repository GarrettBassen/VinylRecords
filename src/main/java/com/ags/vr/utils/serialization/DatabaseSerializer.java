package com.ags.vr.utils.serialization;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Request;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static com.ags.vr.utils.Connector.con;

public class DatabaseSerializer implements java.io.Serializable
{
    //TODO ADD METHOD FOR SERIALIZING THE DB USING THE DB SAVER METHODS
    //TODO ADD METHOD FOR RECONSTRUCTING THE DB USING DESERIALIZED ARRAYLIST

    //Database tables
    private ArrayList<Media> mediaTable;
    private ArrayList<Stock> stockTable;
    private ArrayList<String> bandTable;
    private ArrayList<String> genreTable;
    private ArrayList<String> genreLinkerTable;
    private ArrayList<Request> requestTable;



    /**
     * Initializes the DataBaseSaverLoader object.
     * @param bool True has the constructor initialize all array list. False does not initialize any array list.
     */
    public DatabaseSerializer(boolean bool)
    {
        if(bool)
        {
            mediaTable = MediaSerializer.saveMediaEntries();
            stockTable = InventorySerializer.saveInventoryEntries();
            bandTable = BandSerializer.saveBandEntries();
            genreTable = GenreSerializer.saveGenreEntries();
            genreLinkerTable = GenreLinkerSerializer.saveGenreLinkerEntries();
            requestTable = RequestSerializer.saveRequestEntries();
        }
    }

    /**
     * Serializes the database.
     * @param filename Name of file to be saved
     * @return Returns true if the save was successful; false otherwise.
     */
    public boolean save(String filename)
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

            return true;
        }

        catch(IOException e)
        {
            Graphical.ErrorPopup("Save Error", e.getMessage());
        }
        return false;
    }

    /**
     * Deserializes database save and sets this object with the deserialized database data.
     * @param filename Name of file to be found.
     * @return True if the file was successfully found; false otherwise.
     */
    public boolean getFile(String filename) {

        // Deserialization
        try
        {
            //Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            //Deserializing object
            DatabaseSerializer loaded = (DatabaseSerializer) in.readObject();

            in.close();
            file.close();

            //update this objects values
            this.mediaTable = loaded.mediaTable;
            this.stockTable = loaded.stockTable;
            this.bandTable = loaded.bandTable;
            this.genreTable = loaded.genreTable;
            this.genreLinkerTable = loaded.genreLinkerTable;
            this.requestTable = loaded.requestTable;
            return true;
        }
        catch (IOException | ClassNotFoundException e)
        {
            Graphical.ErrorPopup("Load Error", e.getMessage());
        }
        return false;
    }

    //TODO test
    public boolean load()
    {
        try
        {
            //deleting all database info
            Statement stmt = con.createStatement();
            //not working
            /*
            stmt.addBatch("DELETE FROM genre_linker");
            stmt.addBatch("DELETE FROM genre");
            stmt.addBatch("DELETE FROM band");
            stmt.addBatch("DELETE FROM inventory");
            stmt.addBatch("DELETE FROM media");
            stmt.addBatch("DELETE FROM request");
             */
            stmt.addBatch("DELETE FROM request");
            stmt.addBatch("DELETE FROM media");
            stmt.addBatch("DELETE FROM inventory");
            stmt.addBatch("DELETE FROM band");
            stmt.addBatch("DELETE FROM genre");
            stmt.addBatch("DELETE FROM genre_linker");
            stmt.executeBatch();

            //loading new database info
            BandSerializer.loadBandEntries(bandTable);
            GenreSerializer.loadGenreEntries(genreTable);
            MediaSerializer.loadMediaEntries(mediaTable);
            InventorySerializer.loadInventoryEntries(stockTable);
            GenreLinkerSerializer.loadGenreLinkerEntries(genreLinkerTable);
            RequestSerializer.loadRequestEntries(requestTable);
            return true;
        }
        catch(SQLException e)
        {
            Graphical.ErrorPopup("Database Load Error", e.getMessage());
        }
        return false;
    }
}
