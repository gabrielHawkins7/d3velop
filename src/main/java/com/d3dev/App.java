package com.d3dev;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Scene scene;
    Controller controller;
    

    @Override
   public void start(Stage stage) throws IOException, InterruptedException, URISyntaxException {

        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        
        VBox root = new VBox();
        scene = new Scene(root, 800, 600);
        
        controller = new Controller(stage,scene, this);

        root.getChildren().add(controller.getRoot());
        VBox.setVgrow(controller.getRoot(), Priority.ALWAYS);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("D3velop");

        
        
        stage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });

        new Timer().schedule(
            new TimerTask() {
                @Override
                public void run() {
                    System.gc();
                }
            },5000, 5000);
    }

    
    

    public static void main(String[] args) {
        
        launch();
    }

}