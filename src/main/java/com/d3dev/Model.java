package com.d3dev;

import java.util.HashMap;

import com.d3dev.Utils.CMS;
import com.d3dev.Views.Image_View;
import com.d3dev.Views.Main_Window;
import com.d3dev.Views.Sidebar_View;
import com.d3dev.Views.Toolbar_View;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;
import java.io.File;


public class Model {
        Scene scene_ref;
        Stage stage_ref;
        public HashMap<String, Node> ui_ = new HashMap<>();
        public HashMap<String, Property> props_ = new HashMap<>();
        public HashMap<String, String> csprofiles_ = CMS.getProfiles();
        public BufferedImage open_image;
        public File open_file;
        //Views
        Main_Window root = new Main_Window(this);
        Toolbar_View toolbar_View = new Toolbar_View(this);
        Image_View image_View = new Image_View(scene_ref);
        Sidebar_View sidebar_View = new Sidebar_View(this);


}
