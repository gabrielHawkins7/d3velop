package com.d3dev.Views;

import com.d3dev.Model;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Sidebar_View extends VBox{

    Model model;
    public Sidebar_View(Model model){
        this.model = model;
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER);
        this.getChildren().add(row);
        VBox.setVgrow(row, Priority.ALWAYS);



        Tab develop_tab = new Tab("Develop");
        TabPane tabs = new TabPane(develop_tab);
        tabs.getStyleClass().addAll(Styles.TABS_CLASSIC, Styles.DENSE);
        
        tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        tabs.setMinWidth(200);
        row.getChildren().add(tabs);
        HBox.setHgrow(tabs, Priority.ALWAYS);

        createDevelopTab(develop_tab);

    }

    void createDevelopTab(Tab dev_tab){
        VBox col = new VBox();
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
        workspace_combobox.getSelectionModel().select(0);



        Text outspace_label = new Text("Output Colorspace");
        ComboBox<String> outspace_combobox = new ComboBox<>();
        model.ui_.put("outspace_combobox", outspace_combobox);
        outspace_combobox.getItems().addAll(model.csprofiles_.keySet());
        outspace_combobox.getStyleClass().addAll(Styles.DENSE);
        outspace_combobox.setPrefWidth(width);
        box1.add(outspace_label, 0, 1); 
        box1.add(outspace_combobox,3,1);
        outspace_combobox.getSelectionModel().select(1);

        


        Button dev = new Button("Develop");
        model.ui_.put("dev_button", dev);
        HBox row1 = new HBox(dev);
        row1.setPadding(new Insets(5, 0, 5, 0));
        row1.setAlignment(Pos.CENTER);
        col.getChildren().add(row1);

    }
    
    
}
