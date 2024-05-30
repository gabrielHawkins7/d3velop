package com.d3dev.Views;

import java.util.HashMap;

import com.d3dev.Controller;
import com.d3dev.Model;

import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
    StringProperty work_profile = new SimpleStringProperty();
    StringProperty out_profile = new SimpleStringProperty();

    Model model;
    public Sidebar_View(Model model){
        this.model = model;
        model.props_.put("work_profile", work_profile);
        model.props_.put("out_profile", out_profile);
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
        model.ui_.put("workspace_combobox", workspace_combobox);
        workspace_combobox.getItems().addAll(model.csprofiles_.keySet());
        workspace_combobox.getStyleClass().addAll(Styles.DENSE);
        workspace_combobox.setPrefWidth(width);
        box1.add(workspace_label, 0, 0); 
        box1.add(workspace_combobox,3,0);
        workspace_combobox.valueProperty().bindBidirectional(work_profile);
        workspace_combobox.getSelectionModel().select(0);


        Text outspace_label = new Text("Output Colorspace");
        ComboBox<String> outspace_combobox = new ComboBox<>();
        model.ui_.put("outspace_combobox", outspace_combobox);
        outspace_combobox.getItems().addAll(model.csprofiles_.keySet());
        outspace_combobox.getStyleClass().addAll(Styles.DENSE);
        outspace_combobox.setPrefWidth(width);
        box1.add(outspace_label, 0, 1); 
        box1.add(outspace_combobox,3,1);
        outspace_combobox.valueProperty().bindBidirectional(out_profile);
        outspace_combobox.getSelectionModel().select(1);

        

        Button dev = new Button("Develop");
        model.ui_.put("dev_button", dev);
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
        model.ui_.put("brightness_slider",brightness_slider);
        brightness_slider.setShowTickLabels(true);
        brightness_slider.setShowTickMarks(true);
        brightness_slider.setStyle(Styles.SMALL);
        box1.add(brightness_slider, 3, 0);
    }
    
}
