package com.d3dev;

import java.io.IOException;

import com.d3dev.Views.Main_Window;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.beans.property.Property;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
@SuppressWarnings("exports") 
public class App extends Application {

    private static Scene scene;

    

    @Override
   public void start(Stage stage) throws IOException, InterruptedException {


        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        VBox root = new VBox();
        scene = new Scene(root, 800, 600);
        
        Controller controller = new Controller(stage,scene);

        root.getChildren().add(controller.getRoot());
        VBox.setVgrow(controller.getRoot(), Priority.ALWAYS);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("D3velop");

        for(Property x : controller.model.props_.values()){
            System.out.println(x.getValue());
        }
    }
    public static void main(String[] args) {
        launch();
    }

}