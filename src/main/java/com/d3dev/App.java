package com.d3dev;

import java.io.IOException;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
@SuppressWarnings("exports") 
public class App extends Application {

    private static Scene scene;

    

    @Override
    public void start(Stage stage) throws IOException {
        //Loader.load(opencv_java.class);

        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"));
        scene = new Scene(loader.load(), 640, 480);
        Dc dc = loader.getController();
        dc.initialize();
        
        //dc.loadImage("src/main/resources/com/d3dev/02_02_400d_ 23.png");

        stage.setScene(scene);
        stage.show();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SuppressWarnings("incomplete-switch")
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case EQUALS:    dc.imgview.setFitWidth(dc.imgview.getFitWidth() + 20); break;
                    case MINUS:    dc.imgview.setFitWidth(dc.imgview.getFitWidth() - 20); break;
                }
            }
        });

        System.out.println();
        


    }
    public static void main(String[] args) {
        launch();
    }

}