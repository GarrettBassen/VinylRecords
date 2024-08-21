package com.ags.vr.controllers;

import com.ags.vr.utils.Connector;
import com.ags.vr.utils.Graphical;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class PageBaseController
{
    // VARIABLES
    @FXML
    private BorderPane content_pane;

    @FXML
    private void initialize()
    {
        Connector.connect();
        LoadPage("page_browse.fxml");
    }

    /**
     * Switches pages by loading selected page in page_base center.
     * @param pageName Page fxml name
     */
    private void LoadPage(String pageName)
    {
        try
        {
            Node page = FXMLLoader.load(getClass().getResource("/com/ags/vr/pages/" + pageName));
            content_pane.setCenter(page);
        }
        catch (Exception e)
        {
            Graphical.ErrorPopup("Error Opening Page",String.format(
                    "Failed to open %s\n\nCode:%s",pageName,e.getMessage()
            ));
        }
    }

    /**
     * Opens "Add" page to add media to inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessAdd(ActionEvent event)
    {
        LoadPage("page_add.fxml");
    }

    /**
     * Opens "Browse" page to browse media in inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessBrowse(ActionEvent event)
    {
        LoadPage("page_browse.fxml");
    }

    /**
     * Opens "Inventory" page to edit media in inventory when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessInventory(ActionEvent event)
    {
        LoadPage("page_inventory.fxml");
    }

    /**
     * Opens "Requests" page to view and modify requests when button is pressed.
     * @param event Event handler
     */
    @FXML
    void PageAccessRequests(ActionEvent event)
    {
        LoadPage("page_requests.fxml");
    }

}
