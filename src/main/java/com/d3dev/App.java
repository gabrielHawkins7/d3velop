package com.d3dev;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import atlantafx.base.theme.Dracula;
import atlantafx.base.theme.PrimerDark;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"));
        scene = new Scene(loader.load(), 640, 480);
        Dc dc = loader.getController();
        
        dc.loadImage("src/main/resources/com/d3dev/02_02_400d_ 23.png");

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case EQUALS:    dc.imgview.setFitWidth(dc.imgview.getFitWidth() + 20); break;
                    case MINUS:    dc.imgview.setFitWidth(dc.imgview.getFitWidth() - 20); break;
                }
            }
        });
        


    }
    public static void main(String[] args) {
        launch();
    }

}