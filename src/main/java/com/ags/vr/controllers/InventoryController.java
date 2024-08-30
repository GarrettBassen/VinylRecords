package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBBands;
import com.ags.vr.utils.database.DBInventory;
import com.ags.vr.utils.database.DBMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class InventoryController {

    @FXML
    private TextField addGenre;

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
    private TextField formatDisplay;

    @FXML
    private TextField genresDisplay;

    @FXML
    private Spinner<?> gfSpinner;

    @FXML
    private TextField input;

    @FXML
    private TextField invTotal;

    @FXML
    private TextField mediumDisplay;

    @FXML
    private TextField nameDisplay;

    @FXML
    private Spinner<?> pfSpinner;

    @FXML
    private TextField removeGenre;

    @FXML
    private Button stockSave;

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
    private int medNum;

    private int formatNum;


    public void initialize()
    {
        SpinnerInitialize();
    }

    //TODO FIX
    @FXML
    void applyUpdate(ActionEvent event)
    {
        try
        {
            if (!invalidInput(event))
            {
                Media oldMedia = DBMedia.getMedia(input.getText());
                Media newMedia = createUpdatedMedia();

                if (!DBBands.Contains(newMedia.getBand()))
                {
                    DBBands.Insert(newMedia.getBand());
                }

                DBMedia.Update(newMedia, oldMedia.getID());
            }
        }
        catch (Exception e)
        {
            Graphical.ErrorPopup("Failed band update", e.getMessage());
        }
    }

    @FXML
    void deleteEntry(ActionEvent event) {

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
            Media m = DBMedia.getMedia(name);
            //setting the media display text fields
            nameDisplay.setText(name);
            bandDisplay.setText(m.getBand());
            genresDisplay.setText("work in progress");
            yearDisplay.setText(String.valueOf(m.getYear()));
            mediumDisplay.setText(m.getMedium());
            formatDisplay.setText(m.getFormat());

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
     * Updates the current media object with new stock values gathered from spinner objects.
     * @param event Update save button press.
     */
    @FXML
    void stockSave(ActionEvent event)
    {
        if(!invalidInput(event))
        {
            try
            {
                //creating new stock object
                Stock st = createUpdatedStock();
                DBInventory.Modify(st);
                invTotal.setText(String.valueOf(st.getStockTotal()));
            }
            catch(Exception e)
            {
                Graphical.ErrorPopup("Failed inventory update" ,e.getMessage());
            }
        }
    }

    @FXML
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
            medNum = 1;
        }
        else if(event.getSource().equals(cdRB))
        {
            //select cd button
            cdRB.setSelected(true);
            //update number for switch
            medNum = 2;
        }
        else
        {
            //select cassett button
            cassetteRB.setSelected(true);
            //update number for switch
            medNum = 3;
        }
    }

    //TODO FIX
    @FXML
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
            formatNum = 1;
        }
        else if(event.getSource().equals(epRB))
        {
            //select ep button
            epRB.setSelected(true);
            //update number for switch
            formatNum = 2;
        }
        else if(event.getSource().equals(lpRB))
        {
            //select lp button
            lpRB.setSelected(true);
            //update number for switch
            formatNum = 3;
        }
        else
        {
            //select dlp button
            dlpRB.setSelected(true);
            //update number for switch
            formatNum = 4;
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

    //TODO UPDATE WITH MORE INPUT SCENARIOS AS THEY COME UP
    //TODO ADD REMOVE GENRE AND ADD GENRE INPUT SCENARIOS
    /**
     * Deals with all possible invalid input cases.
     * @param event action event of current method being run.
     * @return Returns true if an invalid input is detected. Returns false if there is no invalid input.
     */
    public boolean invalidInput(ActionEvent event)
    {
        if(event.getSource().equals(searchButton) && input.getText().isEmpty())
        {
            Graphical.InfoPopup("Invalid Input", "Please enter a valid name in the \"Media Name\" text field.");
            return true;
        }
        else if(event.getSource().equals(searchButton) && !DBMedia.Contains(input.getText()))
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
        else if((event.getSource().equals(stockSave) || event.getSource().equals(update) ) && input.getText().isEmpty())
        {
            Graphical.InfoPopup("No Media Selected", "Please enter a valid name in the \"Media Name\" " +
                    "text field for update.");
            return true;
        }
        else if(event.getSource().equals(stockSave) && !DBMedia.Contains(input.getText()))
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
        return new Stock(DBMedia.getMedia(input.getText()).getID(), ar);
    }

    /**
     * Creates a new media object based off the current values in the display fields.
     * @return
     */
    public Media createUpdatedMedia()
    {
        String medium = "";
        String format = "";

        switch(medNum)
        {
            case 1:
                medium = "vinyl";
                break;
            case 2:
                medium = "CD";
                break;
            case 3:
                medium = "cassette";
        }

        switch(formatNum)
        {
            case 1:
                format = "Single";
                break;
            case 2:
                format = "EP";
                break;
            case 3:
                format = "LP";
                break;
            case 4:
                format = "DLP";
        }

        return new Media(
                nameDisplay.getText(),
                medium,
                format,
                Short.parseShort(yearDisplay.getText()),
                bandDisplay.getText()
        );
    }
}
