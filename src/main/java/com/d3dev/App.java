package com.d3dev;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.scene.Scene;
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

       

        
        for(String x : controller.model.props_.keySet()){
            System.out.println(x + " : " + controller.model.props_.get(x).getValue());
        }
        for(Property x : controller.model.props_.values()){
            x.addListener(e->{
                for(String k : controller.model.props_.keySet()){
                    if(x.equals(controller.model.props_.get(k))){
                        System.out.println(k + " : " + x);
                    }
                }
            });
        }
        

        stage.setOnCloseRequest(e->{
            Platform.exit();
            System.exit(0);
        });

        // new Timer().schedule(
        //     new TimerTask() {
        //         @Override
        //         public void run() {
        //             System.gc();
        //         }
        //     },5000, 5000);
    }
    public static void main(String[] args) {
        launch();
    }

}