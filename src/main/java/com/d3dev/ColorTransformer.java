package com.d3dev;


import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
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
        Slider brightSlider = new Slider(-100,100,0);
        
        brightLabel.setOnMouseClicked(e->{
            brightSlider.setValue(0);
        });
        brightSlider.getStyleClass().add(Styles.SMALL);
        brightSlider.setShowTickMarks(true);
        brightSlider.setMajorTickUnit(.5);
        vbox.getChildren().addAll(brightLabel, brightSlider);

        Label conLabel = new Label("Contrast");
        Slider conSlider = new Slider(.1,3,1);
        conSlider.valueProperty().addListener(e->{
            dc.changeBrightnessContrast(conSlider.getValue(), brightSlider.getValue());
        });
        brightSlider.valueProperty().addListener(e->{
            dc.changeBrightnessContrast(conSlider.getValue(), brightSlider.getValue());
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

}
