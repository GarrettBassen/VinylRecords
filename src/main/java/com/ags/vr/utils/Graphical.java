package com.ags.vr.utils;

import javafx.scene.control.Alert;

public class Graphical
{
    // TODO CREATE POPUP WINDOWS AND OTHER GRAPHICAL UI UTILS

    /**
     * Creates graphical popup to display error message to user.
     * @param title Error title
     * @param message Error message
     */
    public static void ErrorPopup(String title, String message)
    {
        Alert popup = new Alert(Alert.AlertType.ERROR);
        popup.setTitle(title);
        popup.setHeaderText(null);
        popup.setContentText(message);
        popup.showAndWait();
    }
}
