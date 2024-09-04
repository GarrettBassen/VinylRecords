package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Stack;

public class InventoryController {

    @FXML
    private TextField bandDisplay;

    @FXML
    private Spinner<?> bfSpinner;

    @FXML
    private Spinner<?> bgSpinner;

    @FXML
    private Spinner<?> bpSpinner;

    @FXML
    private Button searchButton;

    @FXML
    private Button delete;

    @FXML
    private Spinner<?> ffSpinner;


    @FXML
    private TextArea genresDisplay;

    @FXML
    private Spinner<?> gfSpinner;

    @FXML
    private TextField input;

    @FXML
    private TextField invTotal;

    @FXML
    private TextField nameDisplay;

    @FXML
    private Spinner<?> pfSpinner;

    @FXML
    private Button update;

    @FXML
    private TextField yearDisplay;

    @FXML
    private RadioButton vinylRB;

    @FXML private RadioButton cdRB;

    @FXML private RadioButton cassetteRB;

    @FXML private RadioButton singleRB;

    @FXML private RadioButton epRB;

    @FXML private RadioButton lpRB;

    @FXML private RadioButton dlpRB;

    //used with radio buttons
    private String medium = "";
    private String format = "";


    public void initialize()
    {
        SpinnerInitialize();
    }

    /**
     * Updates a media entry based on the current inputted values.
     * @param event Update button push.
     */
    @FXML
    void applyUpdate(ActionEvent event)
    {
        try {
            if (!invalidInput(event))
            {
                //getting old media object
                Media oldMedia = DBMedia.getMedia(input.getText(), medium);
                //creating new media object
                Media newMedia = createUpdatedMedia();

                    //insert the band if its not in the db
                    if (!DBBand.Contains(newMedia.getBand()))
                    {
                        DBBand.Insert(newMedia.getBand());
                    }

                    //update the media object
                    DBMedia.Update(newMedia, oldMedia);

                    //connect with genre linker
                    String genres = genresDisplay.getText();
                    String[] genresAr = genres.split("\n");

                    for (String genre : genresAr) {
                        //insert genres if theyre not in the db
                        if (!DBGenre.Contains(genre))
                        {
                            DBGenre.Insert(genre);
                        }
                        DBGenreLinker.Insert(newMedia.getID(), Hash.StringHash(genre));
                    }

                    //connect media and stock
                    stockSave();

                    Graphical.InfoPopup("Update", "Updated successfully");
                }
        }
        catch (Exception e)
        {
            Graphical.ErrorPopup("Failed band update", e.getMessage());
        }

    }

    /**
     * Deletes a media entry.
     * @param event Delete button press.
     */
    @FXML
    void deleteEntry(ActionEvent event)
    {
        if(!invalidInput(event))
        {
            //ask the user for deletion conformation
            boolean delete = Graphical.ConfirmationPopup("Deletion", "Are you sure" +
                    " you want to delete this item?");

            //deletion confermend
            if(delete)
            {
                DBMedia.Delete(DBMedia.getMedia(input.getText(), medium));
                Graphical.InfoPopup("Delete", "Deleted successfully");
            }
            //deletion averted
            else
            {
                Graphical.InfoPopup("Deletion", "Deletion Averted");
            }
        }
    }


    /**
     * Takes the title of a media and searches the database for it. The results are
     * displayed in their corresponding text fields. If the media does not exist
     * the user is prompted to the add page. If the input box is empty, the user
     * is prompted to enter a valid input.
     * @param event action event from button press.
     */
    @FXML
    void mediaSearch(ActionEvent event)
    {
        String name = input.getText();

        if(!invalidInput(event))
        {
            //getting the media from the db
            Media m = DBMedia.getMedia(name, medium);
            //setting the media display text fields
            nameDisplay.setText(name);
            bandDisplay.setText(m.getBand());
            yearDisplay.setText(String.valueOf(m.getYear()));
            String dbFormat = m.getFormat();


            //for displaying correct format
            if(dbFormat.equals("Single"))
            {
                format = "Single";
                singleRB.setSelected(true);
            }
            else if(dbFormat.equals("EP"))
            {
                format = "EP";
                epRB.setSelected(true);
            }
            else if(dbFormat.equals("LP"))
            {
                format = "LP";
                lpRB.setSelected(true);
            }
            else if(dbFormat.equals("DLP"))
            {
                format = "DLP";
                dlpRB.setSelected(true);
            }

            //displaying genre
            int[] genreIDs = DBMedia.getGenres(m);
            String[] genresAr = DBGenre.getName(genreIDs);
            String display = "";

            for(int i = 0; i < genresAr.length-1; i++)
            {
                display += genresAr[i] + "\n";
            }
            display += genresAr[genresAr.length-1];
            genresDisplay.setText(display);

            //clear the spinners for update
            gfSpinner.decrement((int) gfSpinner.getValue());
            ffSpinner.decrement((int) ffSpinner.getValue());
            pfSpinner.decrement((int) pfSpinner.getValue());
            bgSpinner.decrement((int) bgSpinner.getValue());
            bfSpinner.decrement((int) bfSpinner.getValue());
            bgSpinner.decrement((int) bgSpinner.getValue());
            //stock object
            Stock st = new Stock(m);
            int[] data = st.getData();
            //setting the stock spinners
            gfSpinner.increment(data[1]);
            ffSpinner.increment(data[2]);
            pfSpinner.increment(data[3]);
            bgSpinner.increment(data[4]);
            bfSpinner.increment(data[5]);
            bpSpinner.increment(data[6]);
            //setting total stock value
            invTotal.setText(String.valueOf(st.getStockTotal()));
        }
    }

    /**
     * Replaces the current stock entry with a new updated entry.
     */
    @FXML
    void stockSave()
    {
        try
        {
            //creating new stock object
            Stock st = createUpdatedStock();
            DBInventory.Insert(st);
            invTotal.setText(String.valueOf(st.getStockTotal()));
        }
        catch (Exception e)
        {
            Graphical.ErrorPopup("Failed inventory update", e.getMessage());
        }
    }

    @FXML
    /**
     * Indicates which medium is selected.
     */
    private void mediumControl(ActionEvent event)
    {
        //deselct all at buttons
        vinylRB.setSelected(false);
        cdRB.setSelected(false);
        cassetteRB.setSelected(false);

        if(event.getSource().equals(vinylRB))
        {
            //select vinyl button
            vinylRB.setSelected(true);
            //update number for switch
            medium = "vinyl";
        }
        else if(event.getSource().equals(cdRB))
        {
            //select cd button
            cdRB.setSelected(true);
            //update number for switch
            medium = "CD";
        }
        else
        {
            //select cassett button
            cassetteRB.setSelected(true);
            //update number for switch
            medium = "cassette";
        }
    }

    @FXML
    /**
     * Indicates which format is selected.
     */
    private void formatControl(ActionEvent event)
    {
        //deselcting all buttons
        singleRB.setSelected(false);
        epRB.setSelected(false);
        lpRB.setSelected(false);
        dlpRB.setSelected(false);
        if(event.getSource().equals(singleRB))
        {
            //select single button
            singleRB.setSelected(true);
            //update number for switch
            format = "Single";
        }
        else if(event.getSource().equals(epRB))
        {
            //select ep button
            epRB.setSelected(true);
            //update number for switch
            format = "EP";
        }
        else if(event.getSource().equals(lpRB))
        {
            //select lp button
            lpRB.setSelected(true);
            //update number for switch
            format = "LP";
        }
        else
        {
            //select dlp button
            dlpRB.setSelected(true);
            //update number for switch
            format = "DLP";
        }
    }


    //thanks garret
    /**
     * Initializes spinners and sets value range.
     */
    private void SpinnerInitialize()
    {
        Spinner<Integer>[] spinners = new Spinner[] {
                gfSpinner, ffSpinner, pfSpinner,
                bgSpinner, bfSpinner, bpSpinner
        };

        // Initialize spinners
        for (Spinner<Integer> sp : spinners)
        {
            sp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,255));
        }
    }

    /**
     * Deals with all possible invalid input cases.
     * @param event action event of current method being run.
     * @return Returns true if an invalid input is detected. Returns false if there is no invalid input.
     */
    public boolean invalidInput(ActionEvent event)
    {
        //Search scenarios
        if(event.getSource().equals(searchButton) && input.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Input", "Please enter a valid name in the \"Media Name\" text field.");
            return true;
        }
        else if(medium.equals(""))
        {
            Graphical.InfoPopup("Invalid Input", "Please select a medium.");
            return true;
        }
        else if(event.getSource().equals(searchButton) && !DBMedia.Contains(input.getText(), medium))
        {
            boolean GotoPage = Graphical.ConfirmationPopup("Media Does not exist",String.format(
                    "'%s'is not in your system. Would you like to go to the add page to " +
                            "insert this item?",input.getText())
            );

            if (GotoPage)
            {
                // TODO BRING USER TO ADD PAGE FOR THE MEDIA ENTRY
                System.out.println("Media Search InventoryController.java");
            }
            return true;
        }
        //stock update scenarios
        else if(event.getSource().equals(update) && input.getText().isEmpty())
        {
            Graphical.InfoPopup("No Media Selected", "Please enter a valid name in the \"Media Name\" " +
                    "text field for update.");
            return true;
        }
        else if(event.getSource().equals(update) && nameDisplay.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Name Entry", "Please enter a valid name in the \"Name\" " +
                    "text field for update.");
            return true;
        }
        else if(event.getSource().equals(update) && bandDisplay.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Band Entry", "Please enter a valid band name in the " +
                    "\"Band\" text field for update.");
            return true;
        }
        else if(event.getSource().equals(update) && yearDisplay.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Year Entry", "Please enter a valid year in the \"Year\" " +
                    "text field for update.");
            return true;
        }
        else if(event.getSource().equals(update) && genresDisplay.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Genre Entry", "Please enter a valid Genre in the \"Genres\" " +
                    "text field for update.");
            return true;
        }
        else if(event.getSource().equals(delete) && input.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Input", "Please enter a valid media in the \"Media name\"" +
                    "text field for deletion");
            return true;
        }
        return false;
    }


    /**
     * Creates a stock object based off the current stock data in the spinners and the current input name.
     * @return
     */
    public Stock createUpdatedStock()
    {
        byte[] ar = {
                Byte.valueOf(gfSpinner.getValue().toString()),
                Byte.valueOf(ffSpinner.getValue().toString()),
                Byte.valueOf(pfSpinner.getValue().toString()),
                Byte.valueOf(bgSpinner.getValue().toString()),
                Byte.valueOf(bfSpinner.getValue().toString()),
                Byte.valueOf(bpSpinner.getValue().toString()),
        };

        return new Stock(DBMedia.getMedia(input.getText(), medium).getID(), ar);
    }

    /**
     * Creates a new media object based off the current values in the display fields.
     * @return updated media object based on user input.
     */
    public Media createUpdatedMedia()
    {
        return new Media(
                nameDisplay.getText(),
                medium,
                format,
                Short.parseShort(yearDisplay.getText()),
                bandDisplay.getText()
        );
    }
}
