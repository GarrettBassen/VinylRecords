package com.ags.vr.controllers;

import com.ags.vr.utils.Connector;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

public class PageBaseController
{
    // VARIABLES
    @FXML
    private AnchorPane content_anchor;

    @FXML
    private void initialize()
    {
        Connector.connect();
    }

    /**
     * Opens "Add" page to add media to inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessAdd(ActionEvent event)
    {
        // TODO LINK PAGE
        System.out.println("ADD PAGE");
    }

    /**
     * Opens "Browse" page to browse media in inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessBrowse(ActionEvent event)
    {
        // TODO LINK PAGE
        System.out.println("BROWSE PAGE");
    }

    /**
     * Opens "Inventory" page to edit media in inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessInventory(ActionEvent event)
    {
        // TODO LINK PAGE
        System.out.println("INVENTORY PAGE");
    }

    /**
     * Opens "Requests" page to view and modify requests when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessRequests(ActionEvent event)
    {
        // TODO LINK PAGE
        System.out.println("REQUESTS PAGE");
    }

}
