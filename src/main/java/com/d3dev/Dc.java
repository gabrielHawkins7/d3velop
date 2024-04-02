package com.d3dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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

    @FXML
    private Rectangle colorPickerBox;

    @FXML
    private Button fileOpenButton;

    private Image ogImage;
    private Image curImage;

    
    void initialize(){
        imgview.minWidth(0);
        imgview.minHeight(0);
        imgview.setPreserveRatio(true);
        //imgview.setSmooth(false);
        initColorPicker();
    }

    void initColorPicker(){
        imgview.setOnMouseMoved(e->{
            Robot robot = new Robot();
            Color color = robot.getPixelColor(e.getScreenX(), e.getScreenY());
            colorPickerBox.setFill(color);
        });
    }


    void loadImage(String url) throws FileNotFoundException{
        ogImage = new Image(new FileInputStream(url));
        curImage = ogImage;
        imgview.setImage(curImage);
        imgview.setSmooth(false);
        imgview.setFitWidth(ap.getWidth());
        fileOpenButton.setVisible(false);
    }
    void fitImage(){
        if(curImage.getHeight() > curImage.getWidth()){
            imgview.setFitHeight(ap.getHeight());
        }else{
            imgview.setFitWidth(ap.getWidth());
        }
    }

    @FXML
    void openFile(ActionEvent event) {
        System.out.println("hello world!");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Images",new String[] {"*.png"}));
        
        try {
            File file = fc.showOpenDialog(fileOpenButton.getScene().getWindow());

            if(file != null){
                try {
                    loadImage(file.toString());
                } catch (FileNotFoundException e) {
                    System.out.println(e.toString());
                    fileOpenButton.setText("error try again");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
    @FXML
    void rotateRight(){
        curImage = ImageTransformer.rotateImage(curImage, 90);
        imgview.setImage(curImage);
    }
    @FXML
    void rotateLeft(){
        curImage = ImageTransformer.rotateImage(curImage, -90);
        imgview.setImage(curImage);
    }


}
