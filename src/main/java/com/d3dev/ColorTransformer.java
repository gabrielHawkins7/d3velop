package com.d3dev;


import java.util.Arrays;

import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.DoubleIndexer;
import org.bytedeco.javacpp.indexer.ShortIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_xphoto;
import org.bytedeco.opencv.opencv_core.Buffer;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_imgproc.CLAHE;
import org.bytedeco.opencv.opencv_xphoto.WhiteBalancer;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ColorTransformer {
    static void buildColorControlBox(VBox vbox, ImageView imgview, Dc dc){
        Label brightLabel = new Label("Brightness");
        Slider brightSlider = new Slider(-.9,.9,0);
        
        brightLabel.setOnMouseClicked(e->{
            brightSlider.setValue(0);
        });
        brightSlider.getStyleClass().add(Styles.SMALL);
        brightSlider.setShowTickMarks(true);
        brightSlider.setMajorTickUnit(10);
        vbox.getChildren().addAll(brightLabel, brightSlider);

        Label conLabel = new Label("Contrast");
        Slider conSlider = new Slider(.1,2,1);
        conLabel.setOnMouseClicked(e->{
            conSlider.setValue(1);
        });
        conSlider.valueProperty().addListener(e->{
            dc.changeBrightnessContrast(conSlider.getValue(), brightSlider.getValue());
        });
        brightSlider.valueProperty().addListener(e->{
            dc.changeBrightness(brightSlider.getValue());
        });
    
        conSlider.getStyleClass().add(Styles.SMALL);
        conSlider.setShowTickMarks(true);
        conSlider.setMajorTickUnit(.5);
        vbox.getChildren().addAll(conLabel, conSlider);
    }

    static Color[] getWhiteBlackPoint(Image i){
        Color whitepoint;
        Color blackpoint;

        PixelReader r = i.getPixelReader();
        int w = (int) i.getWidth();
        int h = (int) i.getHeight();
        int wh = w*h;

        double wR = 0;
        double wG = 0;
        double wB = 0;
        double bR = 0;
        double bG = 0;
        double bB = 0;
        int max = 255;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color color = r.getColor(x, y);
                wR += color.getRed();
                bR += max - color.getRed();
                wG += color.getGreen();
                bG += max - color.getGreen();
                wB += color.getBlue();
                bB += max - color.getGreen();
            }
        }

        whitepoint = new Color(1, 1, 1, 1);
        blackpoint = new Color(1,1,1,1);

        return new Color[] {whitepoint,blackpoint};
    }

    static Image invertImage(Image i){
        WritableImage invertedImage = new WritableImage((int) i.getWidth(),(int) i.getHeight());
        PixelReader pixelReader = i.getPixelReader();
        PixelWriter pixelWriter = invertedImage.getPixelWriter();
        Color[] wpbp = getWhiteBlackPoint(i);

        for (int y = 0; y < i.getHeight(); y++) {
            for (int x = 0; x < i.getWidth(); x++) {
                // Get the color of the current pixel
                Color color = pixelReader.getColor(x, y);

                // Invert the color using the formula
                double red = 1.0 - color.getRed();
                double green = 1.0 - color.getGreen();
                double blue = 1.0 - color.getBlue();

                // Write the inverted color to the corresponding pixel in the writable image
                pixelWriter.setColor(x, y, Color.color(red, green, blue));
            }
        }

        return invertedImage;
    }

    static Mat balanceWhite(Mat i){
        UByteIndexer srcIndexer = i.createIndexer();
        int[] blackPoint = {0,0,0};
        int[] whitepoint = {0,0,0};
        double[] wp = {0.0,0.0,0.0};
        srcIndexer.get(0,0,blackPoint);
        srcIndexer.get(0,0,whitepoint);
        

        System.out.println(i.type() +" "+ i.channels());
        System.out.println(srcIndexer.getDouble());

        System.out.print("\nOg BlackPoint : ");

        for(int n : blackPoint){
            System.out.print(n  + " ");
        }
        System.out.println("");
        System.out.print("\nOg WhitePoint : ");

        for(int n : whitepoint){
            System.out.print(n  + " ");
        }
        System.out.println("");

        for (int x = 0; x < srcIndexer.size(0); x++) {
            for (int y = 0; y < srcIndexer.size(1); y++) {
                int[] values = new int[3];
                srcIndexer.get(x, y, values);
                if(values[0] < blackPoint[0] && values[1] < blackPoint[1] && values[2] < blackPoint[2]){
                    blackPoint[0] = values[0];
                    blackPoint[1] = values[1];
                    blackPoint[2] = values[2];
                }
                if(values[0] > whitepoint[0] && values[1] > whitepoint[1] && values[2] > whitepoint[2]){
                    whitepoint[0] = values[0];
                    whitepoint[1] = values[1];
                    whitepoint[2] = values[2];
                }
            }
        }
        System.out.print("\nNew BlackPoint : ");

        for(int n : blackPoint){
            System.out.print(n  + " ");
        }
        System.out.println("");
        System.out.print("\nNew WhitePoint : ");

        for(int n : whitepoint){
            System.out.print(n  + " ");
        }
        System.out.println("");

        Mat o = new Mat();
        opencv_imgproc.cvtColor(i, i, opencv_imgproc.COLOR_BGR2HSV);

        UByteIndexer oIndexer = i.createIndexer();
        for (int x = 0; x < oIndexer.size(0); x++) {
            for (int y = 0; y < oIndexer.size(1); y++) {
                int[] values = new int[3];
                oIndexer.get(x, y, values);
                values[2] += (int) (values[2] * -.3);
                oIndexer.put(x,y, values);
            }
        }
        opencv_imgproc.cvtColor(i, i, opencv_imgproc.COLOR_HSV2BGR);


        return i;
    }

    static Mat changeBrightness(Mat i, double n){
        opencv_imgproc.cvtColor(i, i, opencv_imgproc.COLOR_BGR2HSV);
        UByteIndexer indexer = i.createIndexer();
        for (int x = 0; x < indexer.size(0); x++) {
            for (int y = 0; y < indexer.size(1); y++) {
                int[] values = new int[3];
                indexer.get(x,y,values);
                values[2] += (int) (values[2] * n);
                values[2] = Math.clamp(values[2], 0, 255);
                indexer.put(x,y, values);
            }
        }
        
        opencv_imgproc.cvtColor(i, i, opencv_imgproc.COLOR_HSV2BGR);
        return i;
    }
    static Mat invert(Mat i){
        WhiteBalancer wb = new WhiteBalancer(opencv_xphoto.createSimpleWB());
        wb.balanceWhite(i, i);
        opencv_core.bitwise_not(i, i);
        return i;
    }

}
