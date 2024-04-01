package com.d3dev;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Dc {

    @FXML
    public ScrollPane scrollPane;

    @FXML
    public SplitPane splitPane;

    @FXML
    public AnchorPane ap;

    @FXML
    public StackPane imgpane;

    @FXML
    public ImageView imgview;

    
    void initialize(){
        imgview.minWidth(0);
        imgview.setPreserveRatio(true);
        
    }


    void loadImage(String url) throws FileNotFoundException{
        Image image = new Image(new FileInputStream(url));
        imgview.setImage(image);
        imgview.setFitWidth(ap.getWidth());
    }

}
