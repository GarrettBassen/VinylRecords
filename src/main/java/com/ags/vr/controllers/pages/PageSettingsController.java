package com.ags.vr.controllers.pages;

import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.serialization.DatabaseSerializer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class PageSettingsController
{
    // Directory and file constants
    private final String BACKUP_DIRECTORY = "database_backups";
    private final String SETTINGS_FILE = "settings.config";

    // List view display
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

    // TODO IMPLEMENT
    @FXML
    void InitiateDBBackup()
    {
        String fileName = String.format(
                "%d-%d-%d.ser",
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue(),
                LocalDateTime.now().getDayOfMonth()
        );

        // Warn if file collision will occur and give the choice to overwrite file
        if(lv_files.getItems().contains(fileName))
        {
            boolean overwrite = Graphical.ConfirmationPopup("File Collision",
                    "It appears that a backup for the current date already exists. " +
                            "Would you like to overwrite this file with a new backup?");

            if (overwrite)
            {
                DeleteFile(fileName, true);
            }
            else
            {
                return;
            }
        }

        //creating new saver loader object which gets all tables from database
        DatabaseSerializer saver = new DatabaseSerializer(true);
        boolean successful = saver.save(BACKUP_DIRECTORY + File.separator + fileName);

        // Update display
        UpdateFileList();

        // Display message to user
        if (successful)
        {
            Graphical.InfoPopup("Save Successful", "Database backup successfully saved.");
        }
        else
        {
            Graphical.ErrorPopup("Unknown Error", "Unknown error serializing database in " +
                    "InitiateDBBackup() | PageSettingsController.java");
        }
    }

    /**
     * Deletes selected list view item when button is triggered.
     */
    @FXML
    void EventDeleteFile()
    {
        if(lv_files.getSelectionModel().isEmpty())
        {
            Graphical.ErrorPopup("No File Selected",
                    "No file has been selected for deletion. Please select a file and try again.");
            return;
        }

        DeleteFile(lv_files.getSelectionModel().getSelectedItem(), false);
    }

    /**
     * Deletes a saved database file. The file is removed from the databaseSaves directory.
     */
    void DeleteFile(String fileName, boolean permission)
    {
        if(fileName == null || fileName.isBlank())
        {
            Graphical.ErrorPopup("No File Selected",
                    "No file has been selected for deletion. Please select a file and try again.");
            return;
        }

        // TODO FIX VERBIAGE
        if(permission || Graphical.ConfirmationPopup("Deletion Confirmation", String.format(
                "Are you sure you want to delete the backup '%s'?", fileName))
        )
        {
            File file = new File(BACKUP_DIRECTORY + File.separator + fileName);
            boolean deletion = file.delete();
            UpdateFileList();

            if(deletion && !permission)
            {
                Graphical.InfoPopup("Delete Successful", String.format(
                        "The selected backup '%s' has been deleted successfully", fileName
                ));
            }
            else if (!deletion)
            {
                Graphical.ErrorPopup("Deletion Failed", fileName + " could not be deleted.");
            }
        }
    }

    /**
     * Gets database backup directory. If it does not exist, create directory.
     */
    private void GetDirectory()
    {
        directoryPath = new File(BACKUP_DIRECTORY);

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
        file_config = new File(BACKUP_DIRECTORY + File.separator + SETTINGS_FILE);
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
        // SKELETON TEMPLATE
        /**
         * ## CONFIG FILE
         *
         * # Frequency in Days
         * db.backup.frequency = 7
         *
         * # How Many Backups to Keep
         * db.backup.saves = 5
         *
         * # Last Backup Date
         * db.backup.last = 01/01
         */
        // TODO SEE ABOUT DELETING OLD BACKUPS WHEN NEW ONE IS MANUALLY CREATED?
    }

    /**
     * Updates file list view by displaying all files in backup directory.
     */
    private void UpdateFileList()
    {
        // Clear listview
        lv_files.getItems().clear();

        for(File file : directoryPath.listFiles())
        {
            // Don't show config file
            if (file.getName().equals("settings.config")) { continue; }

            lv_files.getItems().add(file.getName());
        }
    }


    /**
     * Loads a saved database. All current entries are deleted and replaced with the loaded entries.
     */
    @FXML
    void loadFile()
    {
        //check if a file has been selected
        if(lv_files.getSelectionModel().isEmpty())
        {
            Graphical.ErrorPopup("No Save Selected", "Please select a save.");
            return;
        }

        String filename = lv_files.getSelectionModel().getSelectedItem();
        if(Graphical.ConfirmationPopup("Load Save", "Are you sure you want to load " +
                filename + "? All currently stored data will be lost."))
        {
            DatabaseSerializer loader = new DatabaseSerializer(false);
            boolean getFileBool = loader.getFile(BACKUP_DIRECTORY + File.separator + filename);
            boolean loadBool = loader.load();

            if(getFileBool && loadBool)
            {
                Graphical.InfoPopup("Load Successful", filename + " loaded successfully.");
            }
        }
    }

}
