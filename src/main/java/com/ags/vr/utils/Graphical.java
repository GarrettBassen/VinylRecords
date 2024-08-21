package com.ags.vr.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Class for graphical utilities.
 */
public class Graphical
{
    /**
     * Creates graphical popup to display error message to user.
     * @param title Error title
     * @param message Error message
     */
    public static void ErrorPopup(String title, String message)
    {
        Alert popup = new Alert(Alert.AlertType.ERROR);
        popup.setHeaderText(null);
        popup.setTitle(title);
        popup.setContentText(message);
        popup.showAndWait();
    }

    /**
     * Creates graphical confirmation popup returning True if user clicks the "Ok" button or False otherwise.
     * @param title Popup title
     * @param message Popup message
     * @return True if accepted; false otherwise.
     */
    public static boolean ConfirmationPopup(String title, String message)
    {
        Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
        popup.setHeaderText(null);
        popup.setTitle(title);
        popup.setContentText(message);
        Optional<ButtonType> selection = popup.showAndWait();
        return selection.isPresent() && selection.get() == ButtonType.OK;
    }

    /**
     * Creates graphical popup to display information to user.
     * @param title Popup title
     * @param message Popup message
     */
    public static void InfoPopup(String title, String message)
    {
        Alert popup = new Alert(Alert.AlertType.INFORMATION);
        popup.setHeaderText(null);
        popup.setTitle(title);
        popup.setContentText(message);
        popup.showAndWait();
    }
}
