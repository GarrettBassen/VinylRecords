package com.ags.vr.controllers.pages;

import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.serialization.DatabaseSerializer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;

public class PageSettingsController
{
    @FXML private ListView<String> lv_files;


    // Backup directory file location
    File directoryPath;
    File file_config;

    @FXML
    void initialize()
    {
        GetDirectory();
        if (!GetConfig())
        {
            CreateConfig();
        }

        // Check settings file to initialize display variables

        UpdateFileList();
    }

    /**
     * Gets database backup directory. If it does not exist, create directory.
     */
    private void GetDirectory()
    {
        directoryPath = new File("database_backups");

        if (!directoryPath.exists())
        {
            directoryPath.mkdirs();
        }
    }

    /**
     * Gets the settings config file and returns true if it exists or false otherwise.
     * @return True if file exists; false otherwise
     */
    private boolean GetConfig()
    {
        file_config = new File(directoryPath.getPath() + "/settings.config");
        return file_config.exists();
    }

    /**
     * Creates config file and populates with default data.
     */
    private void CreateConfig()
    {
        try
        {
            file_config.createNewFile();
            PopulateConfigSettings();
        }
        catch (IOException e)
        {
            Graphical.ErrorPopup("Error Creating Config File", String.format(
                    "There was an error creating the configuration file in " +
                            "CreateConfig() | PageSettingsController.java " +
                            "Please try again or contact support.\n\nError:%s",
                    e.getMessage()
            ));
        }
    }

    private void PopulateConfigSettings()
    {
        // TODO populate config file with settings
        /**
         * ## CONFIG FILE
         *
         * # Frequency in days
         * db.backup.frequency = 7
         *
         * # How many backups to save
         * db.backup.saves = 5
         */
    }

    /**
     * Updates file list view by displaying all files in backup directory.
     */
    private void UpdateFileList()
    {
        for(File file : directoryPath.listFiles())
        {
            // Don't show config file
            if (file.getName().equals("settings.config")) { continue; }

            lv_files.getItems().add(file.getName());
        }
    }

    /**
     * Saves the entirety of the current database into a .ser file.
     */
    /*
    @FXML
    void saveFile()
    {
        if(lv_files.getItems().contains(fileInput.getText() + ".ser"))
        {
            Graphical.ErrorPopup("Same Save Name", "Delete highlighted save before adding new save with same name.");
            lv_files.getSelectionModel().select(fileInput.getText() + ".ser");
            return;
        }

        //creating new saver loader object which getts all tables from database
        DatabaseSerializer saver = new DatabaseSerializer(true);
        boolean bool = saver.save("databaseSaves/" + fileInput.getText() + ".ser");

        if(bool)
        {
            lv_files.getItems().add(fileInput.getText() + ".ser");
            Graphical.InfoPopup("Save Successful", fileInput.getText() + "saved.");
        }
    }
     */

    /**
     * Loads a saved database. All current entries are deleted and replaced with the loaded entries.
     */
    /*
    @FXML
    void loadFile()
    {
        //check if a file has been selected
        if(fileListView.getSelectionModel().isEmpty())
        {
            Graphical.ErrorPopup("No Save Selected", "Please select a save.");
            return;
        }

        String filename = fileListView.getSelectionModel().getSelectedItem();
        if(Graphical.ConfirmationPopup("Load Save", "Are you sure you want to load " +
                filename + "? All currently stored data will be lost."))
        {
            DatabaseSerializer loader = new DatabaseSerializer(false);
            boolean getFileBool = loader.getFile("databaseSaves/" + filename);
            boolean loadBool = loader.load();

            if(getFileBool && loadBool)
            {
                Graphical.InfoPopup("Load Successful", filename + " loaded successfully.");
            }
        }
    }
     */

    /**
     * Deletes a saved database file. The file is removed from the databaseSaves directory.
     */
    /*
    @FXML
    void deleteFile()
    {
        if(fileListView.getSelectionModel().isEmpty())
        {
            Graphical.ErrorPopup("No file selected", "Please select a file.");
            return;
        }

        String filename = fileListView.getSelectionModel().getSelectedItem();

        if(Graphical.ConfirmationPopup("Save Deletion", "Are you sure you want to delete this save?"))
        {
            File file = new File("databaseSaves/" + filename);
            boolean deletion = file.delete();

            if(deletion)
            {
                Graphical.InfoPopup("Delete Successful", filename + " deleted successfully.");
                fileListView.getItems().remove(filename);
            }
            else
            {
                Graphical.ErrorPopup("Deletion Failed", filename + " could not be deleted.");
            }
        }
    }
     */
}
