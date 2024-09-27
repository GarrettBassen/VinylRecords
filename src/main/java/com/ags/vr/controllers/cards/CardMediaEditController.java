package com.ags.vr.controllers.cards;

import com.ags.vr.controllers.utils.CardBase;
import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.time.Year;

//TODO FIX STOCK ERROR POPUP
//TODO FIX nameDisplay PRESELECTED BUG
public class CardMediaEditController implements CardBase
{
    // Text display
    @FXML private TextField nameDisplay;
    @FXML private TextField yearDisplay;
    @FXML private TextField invTotal;

    // Inventory variables
    @FXML private Spinner<Integer> gbSpinner;
    @FXML private Spinner<Integer> fbSpinner;
    @FXML private Spinner<Integer> pbSpinner;
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
    @FXML private Button delete;

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
        //return if there is invalid input
        if(invalidInput(event))
        {
            return;
        }
        try {
            // Do nothing if media is null
            if (this.media == null) { return; }

            //getting old media object
            Media oldMedia = this.media;
            //creating new media object
            Media newMedia = createUpdatedMedia();

            //update the media object
            DBMedia.Update(newMedia, oldMedia, DBGenreLinker.getGenres(oldMedia));

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
        //return if there is invalid input
        if(invalidInput(event))
        {
            return;
        }

        //ask the user for deletion conformation
        boolean delete = Graphical.ConfirmationPopup("Deletion", "Are you sure" +
                " you want to delete this item?");

        //deletion confirmed
        if(delete)
        {
            DBMedia.Delete(createUpdatedMedia());
            Graphical.InfoPopup("Delete", "Deleted successfully");
        }
        //deletion averted
        else
        {
            Graphical.InfoPopup("Deletion", "Deletion Averted");
        }
    }


    /**
     * Sets the display of CardMediaEditController. Uses the currently selected media as information for the display.
     * Is used when updating a media object to update the display.
     */
    void setDisplay()
    {
        //setting the media display text fields
        nameDisplay.setText(this.media.getTitle());
        yearDisplay.setText(String.valueOf(this.media.getYear()));

        //somehow this fixes the selection bug
        deselect(nameDisplay);

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
        else if(this.media.getMedium().equals("cassette"))
        {
            medium = "cassette";
            cassetteRB.setSelected(true);
        }

        //clear the spinners for update
        gfSpinner.decrement(gfSpinner.getValue());
        ffSpinner.decrement(ffSpinner.getValue());
        pfSpinner.decrement(pfSpinner.getValue());
        gbSpinner.decrement(gbSpinner.getValue());
        fbSpinner.decrement(fbSpinner.getValue());
        gbSpinner.decrement(gbSpinner.getValue());

        //stock object
        Stock st = new Stock(this.media);
        int[] data = st.getData();
        //setting the stock spinners
        gfSpinner.increment(data[1]);
        ffSpinner.increment(data[2]);
        pfSpinner.increment(data[3]);
        gbSpinner.increment(data[4]);
        fbSpinner.increment(data[5]);
        pbSpinner.increment(data[6]);

        //setting total stock value
        invTotal.setText(String.valueOf(st.getStockTotal()));
    }


    /**
     * Replaces the current stock entry with a new updated entry.
     * If the new stock entry is empty the stock table entry is deleted.
     */
    @FXML
    void stockSave(Media m)
    {
        try
        {
            //creating new stock object
            Stock st = createUpdatedStock(m);
            
            //if the current stock is empty
            if(st.isEmpty())
            {
                //remove the current entry from the table
                DBInventory.Delete(m);
                return;
            }

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
        //deselecting all buttons
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
                gbSpinner, fbSpinner, pbSpinner
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

        if(medium.equals(""))
        {
            Graphical.InfoPopup("Invalid Input", "Please select a medium.");
            return true;
        }
        else if(format.equals(""))
        {
            Graphical.InfoPopup("Invalid Input", "Please select a format.");
        }
        else if(nameDisplay.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Name Entry", "Please enter a valid name in the \"Name\" " +
                    "text field.");
            return true;
        }
        else if(yearDisplay.getText().isEmpty() || Short.parseShort(yearDisplay.getText()) > Year.now().getValue() || Short.parseShort(yearDisplay.getText()) < 1400)
        {
            Graphical.InfoPopup("Invalid Year Entry", "Please enter a valid year in the \"Year\" " +
                    "text field.");
            return true;
        }
        else if(event.getSource().equals(delete) && !DBMedia.Contains(createUpdatedMedia()))
        {
            Graphical.InfoPopup("Media Already Not In System", "Cannot delete the displayed media because it is already not in the system.");
            return true;
        }
        else if(nameDisplay.getText().length() > 255)
        {
            Graphical.ErrorPopup("Name Update Error", "The name is too long and cannot be entered into the system. Please use a shorter name");
            return true;
        }

        return false;
    }

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
                (byte) gbSpinner.getValue().intValue(),
                (byte) fbSpinner.getValue().intValue(),
                (byte) pbSpinner.getValue().intValue(),
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
                media.getBand()
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

    //fixes highlighting bug
    /*
    Source:
    profile https://stackoverflow.com/users/6154431/gearquicker
    Stack overflow question https://stackoverflow.com/questions/52304360/javafx-deselect-text-in-textfield-when-open-new-stage
    */
    private void deselect(TextField textField) {
        Platform.runLater(() -> {
            if (textField.getText().length() > 0 &&
                    textField.selectionProperty().get().getEnd() == 0) {
                deselect(textField);
            }else{
                textField.selectEnd();
                textField.deselect();
            }
        });
    }
}
