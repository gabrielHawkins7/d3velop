package com.d3dev;

import java.util.HashMap;
import java.util.Stack;
import com.d3dev.Utils.CMS;
import com.d3dev.Views.Image_View;
import com.d3dev.Views.Main_Window;
import com.d3dev.Views.Sidebar_View;
import com.d3dev.Views.Toolbar_View;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.color.ColorSpace;



public class Model {
        Scene scene_ref;
        Stage stage_ref;
        public HashMap<String, Node> ui_ = new HashMap<>();

        public props_ props_ = new props_();

        public HashMap<String, ColorSpace> csprofiles_ = CMS.getProfiles();

        //Properties
        public BufferedImage open_image;
        public BufferedImage original_image;
        File open_file;

        Stack<State> state_stack = new Stack<>();



        //Views
        Main_Window root = new Main_Window(this);
        Toolbar_View toolbar_View = new Toolbar_View(this);
        @SuppressWarnings("exports")
        public Image_View image_View = new Image_View(this);
        Sidebar_View sidebar_View = new Sidebar_View(this);

        Model(){
                state_stack.push(new State(props_,open_image,open_file));
        }
       

}
