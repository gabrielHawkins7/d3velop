package com.d3dev;

import java.util.HashMap;

import com.d3dev.Views.Image_View;
import com.d3dev.Views.Main_Window;
import com.d3dev.Views.Toolbar_View;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Model {
        Scene scene_ref;
        Stage stage_ref;
        public HashMap<String, Node> ui_ = new HashMap<>();
    
        //Views
        Main_Window root = new Main_Window(this);
        Toolbar_View toolbar_View = new Toolbar_View(this);
        Image_View image_View = new Image_View(scene_ref);


}
