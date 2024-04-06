package com.d3dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.indexer.UByteArrayIndexer;
import org.bytedeco.javacpp.indexer.UByteBufferIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_photo;
import org.bytedeco.opencv.global.opencv_xphoto;
import org.bytedeco.opencv.opencv_core.Algorithm;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_xphoto.WhiteBalancer;
import org.opencv.core.Core;

import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.ColorAdjust;
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

@SuppressWarnings("exports")
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


    @FXML
    private VBox colorControlBox;

    @SuppressWarnings("unused")
    private File ogImage;
    private Image curImage;
    static public Mat curMat;
    static public Mat outMat;
    int brightness = 0;
    
    void initialize(){
        outMat = new Mat();
        imgview.minWidth(0);
        imgview.minHeight(0);
        imgview.setPreserveRatio(true);
        //imgview.setSmooth(false);
        initColorPicker();
        ColorTransformer.buildColorControlBox(colorControlBox, imgview, this);
    }

    void initColorPicker(){
        imgview.setOnMouseMoved(e->{
            Robot robot = new Robot();
            Color color = robot.getPixelColor(e.getScreenX(), e.getScreenY());
            colorPickerBox.setFill(color);
        });
    }


    void loadImage(String url) throws FileNotFoundException{
        ogImage = new File(url);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            // Load the image
            curMat = opencv_imgcodecs.imread(url);
            // Update the UI on the JavaFX Application Thread
            Platform.runLater(() -> {
                // Display the loaded image in the ImageView
                imgview.setImage(ImageTransformer.toFXImage(curMat));
            });
        });
        executor.shutdown();
        imgview.setSmooth(false);
        imgview.setFitWidth(ap.getWidth());
        fileOpenButton.setVisible(false);
    }
    void update(){
        imgview.setImage(ImageTransformer.toFXImage(curMat));
    }



    @FXML
    void openFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Images",new String[] {"*.png", "*.tiff"}));
        
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
        if(curMat != null){
            opencv_core.rotate(curMat, curMat, opencv_core.ROTATE_90_CLOCKWISE);
            imgview.setImage(ImageTransformer.toFXImage(curMat));
        }
    }
    @FXML
    void rotateLeft(){
        if(curMat != null){
            opencv_core.rotate(curMat, curMat, opencv_core.ROTATE_90_COUNTERCLOCKWISE);
            imgview.setImage(ImageTransformer.toFXImage(curMat));
        }
    }
    @FXML
    void invert(){
        WhiteBalancer wb = new WhiteBalancer(opencv_xphoto.createSimpleWB());
        wb.balanceWhite(curMat, curMat);
        opencv_core.bitwise_not(curMat, curMat);
        imgview.setImage(ImageTransformer.toFXImage(curMat));
    }

    void changeBrightness(Double g){
        curMat.convertTo(outMat, -1,1,g);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
    }
    void changeContrast(Double g){
        curMat.convertTo(outMat, -1,g,0);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
    }
    void changeBrightnessContrast(Double c , Double b){
        curMat.convertTo(outMat, -1,c ,b);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
    }


}
