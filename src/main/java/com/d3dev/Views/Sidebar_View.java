package com.d3dev.Views;

import java.util.HashMap;

import com.d3dev.Controller;
import com.d3dev.Model;

import atlantafx.base.theme.Styles;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Sidebar_View extends VBox{
    public HashMap<String, Node> controls = new HashMap<>();
    public String workspace_profile;
    public String outspace_profile;
    Model model;

    public Sidebar_View(Model model){
       this.model = model;

        

        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        this.getChildren().add(row);
        this.setVgrow(row, Priority.ALWAYS);



        Tab develop_tab = new Tab("Develop");
        Tab edit_tab = new Tab("Edit");
        TabPane tabs = new TabPane(develop_tab, edit_tab);
        tabs.getStyleClass().addAll(Styles.TABS_CLASSIC, Styles.DENSE);
        
        tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tabs.setMinWidth(200);
        row.getChildren().add(tabs);
        row.setHgrow(tabs, Priority.ALWAYS);

        createDevelopTab(develop_tab);
        createEditTab(edit_tab);


        



    }

    void createDevelopTab(Tab dev_tab){
        VBox col = new VBox();
        HBox row = new HBox();
        dev_tab.setContent(col);
        int width = 200;
        GridPane box1 = new GridPane(10, 5);
        col.getChildren().add(box1);

        Text workspace_label = new Text("Working Colorspace");
        ComboBox<String> workspace_combobox = new ComboBox<>();
        controls.put("workspace_combobox", workspace_combobox);
        workspace_combobox.getItems().addAll(controller_ref.profiles.keySet());
        workspace_combobox.getSelectionModel().selectFirst();
        workspace_combobox.getStyleClass().addAll(Styles.DENSE);
        workspace_combobox.setPrefWidth(width);
        box1.add(workspace_label, 0, 0); 
        box1.add(workspace_combobox,3,0);
        workspace_profile = controller_ref.profiles.get(workspace_combobox.getValue());
        workspace_combobox.valueProperty().addListener(e->{
            workspace_profile = controller_ref.profiles.get(workspace_combobox.getValue());
        });


        Text outspace_label = new Text("Output Colorspace");
        ComboBox<String> outspace_combobox = new ComboBox<>();
        controls.put("outspace_combobox", outspace_combobox);
        outspace_combobox.getItems().addAll(controller_ref.profiles.keySet());
        outspace_combobox.getSelectionModel().selectFirst();
        outspace_combobox.getStyleClass().addAll(Styles.DENSE);
        outspace_combobox.setPrefWidth(width);
        box1.add(outspace_label, 0, 1); 
        box1.add(outspace_combobox,3,1);

        outspace_profile = controller_ref.profiles.get(outspace_combobox.getValue());

        outspace_combobox.valueProperty().addListener(e->{
            outspace_profile = controller_ref.profiles.get(outspace_combobox.getValue());

        });

        

        Button dev = new Button("Develop");
        controls.put("dev_button", dev);
        col.getChildren().add(dev);


    }
    void createEditTab(Tab edit_tab){
        VBox col = new VBox();
        HBox row = new HBox();
        edit_tab.setContent(col);
        int width = 200;
        GridPane box1 = new GridPane(10, 5);
        col.getChildren().add(box1);

        Slider brightness_slider = new Slider(-100, 100, 0);
        controls.put("brightness_slider",brightness_slider);
        brightness_slider.setShowTickLabels(true);
        brightness_slider.setShowTickMarks(true);
        brightness_slider.setStyle(Styles.SMALL);
        box1.add(brightness_slider, 3, 0);
    }
    
}
