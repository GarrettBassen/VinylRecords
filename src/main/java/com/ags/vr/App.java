package com.ags.vr;

import com.ags.vr.utils.Connector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        // Stage setup
        stage.getIcons().add(new Image(getClass().getResourceAsStream("artwork/icons/placeholder_icon.png")));
        stage.setTitle("Old Skool's Inventory Management System");
        stage.setMinWidth(600);
        stage.setMinHeight(600);

        // Page setup
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("pages/page_base.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches graphical application.
     * @param args args
     */
    public static void main(String[] args)
    {
        launch();
        Connector.disconnect();
    }
}