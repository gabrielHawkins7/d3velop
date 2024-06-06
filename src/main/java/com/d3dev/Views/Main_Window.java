package com.d3dev.Views;

import com.d3dev.Model;

import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Main_Window extends VBox{
    public SplitPane splitpane;
    Model model;


    public Main_Window(Model model){
        this.model = model;
        
    }

    public void addPanels(Node left, Node right){
        splitpane = new SplitPane(left, right);
        splitpane.setDividerPositions(.62);
        this.getChildren().add(splitpane);
        VBox.setVgrow(splitpane, Priority.ALWAYS);
        


    }

}