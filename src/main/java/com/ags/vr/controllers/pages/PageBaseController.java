package com.ags.vr.controllers.pages;

import com.ags.vr.utils.Connector;
import com.ags.vr.utils.Graphical;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class PageBaseController
{
    // VARIABLES
    @FXML private BorderPane content_pane;

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
            Node page = FXMLLoader.load(getClass().getResource("/com/ags/vr/fxml/pages/" + pageName));
            content_pane.setCenter(page);
        }
        catch (IOException e)
        {
            Graphical.ErrorPopup("Error Opening Page",String.format(
                    "Failed to open %s in LoadPage | PageBaseController.java\n\nError:%s",pageName,e.getMessage()
            ));
        }
    }

    /**
     * Opens "Add" page to add media to inventory when button is pressed.
     */
    @FXML
    private void PageAccessAdd()
    {
        LoadPage("page_add.fxml");
    }

    /**
     * Opens "Browse" page to browse media in inventory when button is pressed.
     */
    @FXML
    private void PageAccessBrowse()
    {
        LoadPage("page_browse.fxml");
    }

    /**
     * Opens "Requests" page to view and modify requests when button is pressed.
     */
    @FXML
    private void PageAccessRequests()
    {
        LoadPage("page_requests.fxml");
    }
}
