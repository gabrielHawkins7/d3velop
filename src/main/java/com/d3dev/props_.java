package com.d3dev;

import java.awt.color.ColorSpace;
import java.util.HashMap;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class props_ {
    public StringProperty work_cs = new SimpleStringProperty("");
    public StringProperty out_cs = new SimpleStringProperty("");
    public StringProperty viewport_cs = new SimpleStringProperty("");

    public ObjectProperty<ColorSpace> original_cs = new SimpleObjectProperty<>();

    public BooleanProperty color_mgmt = new SimpleBooleanProperty(true);
    public BooleanProperty crop_mode = new SimpleBooleanProperty(false);




    @SuppressWarnings("rawtypes")
    HashMap<String, Property> asList(){
        HashMap<String, Property> list = new HashMap<>();
        list.put("work_cs", work_cs);
        list.put("out_cs", out_cs);
        list.put("viewport_cs", viewport_cs);
        list.put("original_cs", original_cs);
        list.put("color_mgmt", color_mgmt);
        list.put("crop_mode", crop_mode);
        return list;
    }
}
