package com.ags.vr.controllers;

import com.ags.vr.objects.Media;
import com.ags.vr.objects.Stock;
import com.ags.vr.utils.Graphical;
import com.ags.vr.utils.database.DBMedia;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

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
    private TextField upBand;

    @FXML
    private TextField upFormat;

    @FXML
    private TextField upMedium;

    @FXML
    private TextField upName;

    @FXML
    private TextField upYear;

    @FXML
    private Button update;

    @FXML
    private TextField yearDisplay;

    public void initialize()
    {
        SpinnerInitialize();
    }

    @FXML
    void applyUpdate(ActionEvent event) {

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
        }
    }

    @FXML
    void stockSave(ActionEvent event)
    {

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
        return false;
    }

}
