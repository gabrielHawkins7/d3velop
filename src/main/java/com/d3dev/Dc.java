package com.d3dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.text.AttributeSet.ColorAttribute;

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
import org.bytedeco.opencv.opencv_photo.Tonemap;
import org.bytedeco.opencv.opencv_photo.TonemapReinhard;
import org.bytedeco.opencv.opencv_xphoto.TonemapDurand;
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


    @FXML
    private Rectangle cropRec;

    @SuppressWarnings("unused")
    private File ogImage;
    private Image curImage;
    static public Mat curMat;
    static public Mat outMat;
    int brightness = 0;



    double mouseDownY;
    double mouseDownX;
    
    void initialize(){
        outMat = new Mat();
        cropRec.setVisible(false);
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
            //R:0.7843137383460999 G:0.5960784554481506 B:0.47843137383461
            //System.out.println("R:" + color.getRed() + " G:" + color.getGreen() + " B:" + color.getBlue());
        });
    }


    void loadImage(String url) throws FileNotFoundException{
        ogImage = new File(url);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            curMat = opencv_imgcodecs.imread(url , opencv_imgcodecs.IMREAD_UNCHANGED);
            System.out.println(curMat.type());
            Platform.runLater(() -> {
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
        // // curMat = ColorTransformer.balanceWhite(curMat);
        // curMat.convertTo(curMat, -1, .2 ,0);
        WhiteBalancer wb = new WhiteBalancer(opencv_xphoto.createSimpleWB());
        wb.balanceWhite(curMat, curMat);
        opencv_core.bitwise_not(curMat, curMat);
        // curMat = ColorTransformer.invert(curMat);

        imgview.setImage(ImageTransformer.toFXImage(curMat));
    }

    void changeBrightness(double b){
        curMat.convertTo(outMat, -1);
        outMat = ColorTransformer.changeBrightness(outMat, b);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
    }
    void changeContrast(Double g){
        TonemapReinhard tr = new TonemapReinhard(opencv_photo.createTonemapReinhard(g.floatValue(), 0, 0, 1));
        curMat.convertTo(outMat, opencv_core.CV_32FC3);
        tr.process(outMat, outMat);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
        tr.close();
    }
    void changeBrightnessContrast(Double c , Double b){
        curMat.convertTo(outMat, -1,c ,b);
        imgview.setImage(ImageTransformer.toFXImage(outMat));
    }

    @FXML
    void onCrop(){
        cropRec.setVisible(true);
        cropRec.setViewOrder(0.0);
        imgpane.setOnMousePressed(e->{
            mouseDownX = e.getSceneX();
            mouseDownY = e.getSceneY();
            cropRec.setX(mouseDownX);
            cropRec.setY(mouseDownY);

        });
    }


}
