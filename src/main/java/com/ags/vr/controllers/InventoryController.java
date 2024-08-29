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
    private Button button;

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

    @FXML
    void applyUpdate(ActionEvent event) {

    }

    @FXML
    void deleteEntry(ActionEvent event) {

    }

    //TODO COMPRESS WITH DIFFERENT METHODS FOR INPUT VALIDATION
    //TODO GET RID OF UNNEEDED ERROR POPUP
    @FXML
    void mediaSearch(ActionEvent event)
    {
        String name = input.getText();

        if(name.isEmpty())
        {
            Graphical.InfoPopup("Invalid Input", "Please enter a valid name in the \"name\" text field.");
        }
        else if(DBMedia.Contains(name))
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
            SpinnerInitialize();
            gfSpinner.increment(data[1]);
            ffSpinner.increment(data[2]);
            pfSpinner.increment(data[3]);
            bgSpinner.increment(data[4]);
            bfSpinner.increment(data[5]);
            bpSpinner.increment(data[6]);
        }
        else
        {
            boolean GotoPage = Graphical.ConfirmationPopup("Media Already Exists",String.format(
                    "'%s'is not in your system. Would you like to go to the add page to " +
                            "insert this item?",name)
            );

            if (GotoPage)
            {
                // TODO BRING USER TO ADD PAGE FOR THE MEDIA ENTRY
                System.out.println("Media Search InventoryController.java");
            }
        }
    }

    @FXML
    void stockSave(ActionEvent event) {

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

}
