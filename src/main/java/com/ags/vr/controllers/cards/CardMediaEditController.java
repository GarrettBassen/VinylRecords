package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.Hash;
import com.ags.vr.utils.database.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

//TODO REMOVE GENRE AND BAND EDITING FUNCTIONALITY
//TODO FIX CRASH THAT HAPPENS WHEN THE MEDIA HAS NO GENRES
//TODO FIX STOCK BUG
//TODO FIX UPDATE BUG (LOSES ALL GENRES AND CAUSES A CRASH)

public class CardMediaEditController implements CardBase
{
    // Text display
    @FXML private TextField nameDisplay;
    @FXML private TextField bandDisplay;
    @FXML private TextArea genresDisplay;
    @FXML private TextField yearDisplay;
    @FXML private TextField invTotal;

    // Inventory variables
    @FXML private Spinner<Integer> bgSpinner;
    @FXML private Spinner<Integer> bfSpinner;
    @FXML private Spinner<Integer> bpSpinner;
    @FXML private Spinner<Integer> gfSpinner;
    @FXML private Spinner<Integer> ffSpinner;
    @FXML private Spinner<Integer> pfSpinner;

    // Format display
    @FXML private RadioButton singleRB;
    @FXML private RadioButton epRB;
    @FXML private RadioButton lpRB;
    @FXML private RadioButton dlpRB;

    // Medium display
    @FXML private RadioButton vinylRB;
    @FXML private RadioButton cdRB;
    @FXML private RadioButton cassetteRB;

    @FXML private Button update;

    //used with radio buttons
    private String medium = "";
    private String format = "";

    @FXML private HBox pane_base;
    private CardMediaViewController card_base;
    private Media media;

    @FXML
    public void initialize()
    {
        SpinnerInitialize();
        this.setVisible(false);
    }

    /**
     * Updates a media entry based on the current inputted values.
     * @param event Update button push.
     */
    @FXML
    void applyUpdate(ActionEvent event)
    {
        try {
            // Do nothing if media is null
            if (this.media == null) { return; }

            //getting old media object
            Media oldMedia = this.media;
            //creating new media object
            Media newMedia = createUpdatedMedia();

            //insert the band if it's not in the db
            if (!DBBand.Contains(newMedia.getBand()))
            {
                DBBand.Insert(newMedia.getBand());
            }

            //update the media object
            DBMedia.Update(newMedia, oldMedia);

            //update genre object
            genreHelper(newMedia);

            //connect media and stock
            stockSave(newMedia);

            //set the new media and display
            setMedia(newMedia);

            Graphical.InfoPopup("Update", "Updated successfully");
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
        // Do nothing if media is null
        if (this.media == null) { return; }

        //ask the user for deletion conformation
        boolean delete = Graphical.ConfirmationPopup("Deletion", "Are you sure" +
                " you want to delete this item?");

        //deletion confermend
        if(delete)
        {
            DBMedia.Delete(this.media);
            Graphical.InfoPopup("Delete", "Deleted successfully");
        }
        //deletion averted
        else
        {
            Graphical.InfoPopup("Deletion", "Deletion Averted");
        }
    }


    // TODO COMMENT
    void setDisplay()
    {
        // Do nothing if media is null
        if (this.media == null) { return; }

        //setting the media display text fields
        nameDisplay.setText(this.media.getTitle());
        bandDisplay.setText(this.media.getBand());
        yearDisplay.setText(String.valueOf(this.media.getYear()));

        // Display correct format
        if(this.media.getFormat().equals("Single"))
        {
            format = "Single";
            singleRB.setSelected(true);
        }
        else if(this.media.getFormat().equals("EP"))
        {
            format = "EP";
            epRB.setSelected(true);
        }
        else if(this.media.getFormat().equals("LP"))
        {
            format = "LP";
            lpRB.setSelected(true);
        }
        else if(this.media.getFormat().equals("DLP"))
        {
            format = "DLP";
            dlpRB.setSelected(true);
        }

        //display correct medium
        if(this.media.getMedium().equals("vinyl"))
        {
            medium = "vinyl";
            vinylRB.setSelected(true);
        }
        else if(this.media.getMedium().equals("CD"))
        {
            medium = "CD";
            cdRB.setSelected(true);
        }
        else if(this.media.getMedium().equals("Cassette"))
        {
            medium = "Cassette";
            cassetteRB.setSelected(true);
        }

        //displaying genre
        int[] genreIDs = DBMedia.getGenres(this.media);
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
        Stock st = new Stock(this.media);
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


    //TODO REMOVE THE ABILITY TO INSERT A GENRE FROM MEDIA EDIT CONTROLLER
    /**
     * Method that connects a media object to its genres.
     * @param newMedia
     */
    public void genreHelper(Media newMedia)
    {
        //connect with genre linker
        String genres = genresDisplay.getText();
        String[] genresAr = genres.split("\n");

        for (String genre : genresAr) {
            //insert genres if they're not in the db
            if (!DBGenre.Contains(genre))
            {
                DBGenre.Insert(genre);
            }
            DBGenreLinker.Insert(newMedia.getID(), Hash.StringHash(genre));
        }
    }

    /**
     * Replaces the current stock entry with a new updated entry.
     */
    @FXML
    void stockSave(Media m)
    {
        try
        {
            //creating new stock object
            Stock st = createUpdatedStock(m);
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
    /* TODO CHECK IF NEEDED
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
     */

    /**
     * Creates a stock object based off the current stock data in the spinners and the current input name.
     * @return Stock object
     */
    public Stock createUpdatedStock(Media m)
    {
        byte[] ar = {
                (byte) gfSpinner.getValue().intValue(),
                (byte) ffSpinner.getValue().intValue(),
                (byte) pfSpinner.getValue().intValue(),
                (byte) bgSpinner.getValue().intValue(),
                (byte) bfSpinner.getValue().intValue(),
                (byte) bpSpinner.getValue().intValue(),
        };

        return new Stock(m.getID(), ar);
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

    @Override
    public void Close()
    {
        this.setVisible(false);
        card_base.setPageBlockVisibility(false);
    }

    @Override
    public void Back()
    {
        this.Close();
        this.card_base.setVisible(true);
    }

    @Override
    public void setMediaCard(CardMediaViewController controller)
    {
        this.card_base = controller;
    }

    @Override
    public void setVisible(boolean condition)
    {
        this.pane_base.setVisible(condition);
        if (condition) { card_base.setPageBlockVisibility(true); }
    }

    @Override
    public void setMedia(Media media)
    {
        this.media = media;
        setDisplay();
    }
}
