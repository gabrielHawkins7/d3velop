package com.d3dev.Views;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.d3dev.Model;

import atlantafx.base.controls.ToggleSwitch;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.text.Text;

public class Toolbar_View{
    public ToolBar root;

    public Toolbar_View(Model model){
        Button openfile_button = new Button("Open", new FontIcon(Feather.FILE));
        Button savefile_button = new Button("Save", new FontIcon(Feather.SAVE));
        Button undo_button = new Button("Undo",new FontIcon(Feather.CORNER_UP_LEFT));
        ToggleButton crop_button = new ToggleButton("Crop",new FontIcon(Feather.CROP));
        
        
        ComboBox<String> viewportCombobox = new ComboBox<>();
        viewportCombobox.getItems().addAll("Output", "Working");
        viewportCombobox.getSelectionModel().selectFirst();


        ToggleSwitch colormgmt_switch = new ToggleSwitch("Color Management");
        colormgmt_switch.setSelected(true);
        


        root = new ToolBar( openfile_button, 
                            savefile_button,undo_button, 
                            new Text("Viewport Colorspace"), 
                            viewportCombobox, 
                            crop_button,
                            colormgmt_switch);
        
        model.ui_.put("openfile_button", openfile_button);
        model.ui_.put("savefile_button", savefile_button);
        model.ui_.put("undo_button", undo_button);
        model.ui_.put("viewport_cs", viewportCombobox);
        model.ui_.put("crop_button", crop_button);
        model.ui_.put("colormgmt_switch", colormgmt_switch);

    }
}
