package com.d3dev;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.logging.Logger;

import com.d3dev.Utils.CMS;
import com.d3dev.Utils.Image;
import com.d3dev.Views.Image_View;
import com.d3dev.Views.Main_Window;
import com.d3dev.Views.Sidebar_View;
import com.d3dev.Views.Toolbar_View;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
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
        public HashMap<String, Property> props_ = new HashMap<>();
        public HashMap<String, ColorSpace> csprofiles_ = CMS.getProfiles();

        public BufferedImage open_image;
        public ColorSpace original_cs;
        public ObjectProperty<ColorSpace> current_cs = new SimpleObjectProperty<ColorSpace>();
        public File open_file;


        //Views
        Main_Window root = new Main_Window(this);
        Toolbar_View toolbar_View = new Toolbar_View(this);
        public Image_View image_View = new Image_View(this);
        Sidebar_View sidebar_View = new Sidebar_View(this);

        Model(){
                props_.put("current_cs", current_cs);
        }
        

}