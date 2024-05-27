package com.d3dev;

import com.d3dev.Views.Main_Window;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    Model model = new Model();

    Controller(Stage stage_ref, Scene scene_ref){
        model.stage_ref = stage_ref;
        model.scene_ref = scene_ref;
        model.root.getChildren().add(model.toolbar_View.root);
    }

    VBox getRoot(){
        return model.root;
    }


}
