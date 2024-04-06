package com.d3dev;

import java.io.IOException;
import java.io.InputStream;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.JavaFXFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

@SuppressWarnings("exports")
public class ImageTransformer {
    public static Image rotateImage(Image i, int d){
        ImageView iv = new ImageView(i);
        iv.setRotate(d);

        
        return iv.snapshot(null, null);
    }

    public static javafx.scene.image.Image toFXImage(Mat mat) {
        try(OpenCVFrameConverter.ToMat openCVConverter = new OpenCVFrameConverter.ToMat()) {
            try(Frame frame = openCVConverter.convert(mat)){
                try(JavaFXFrameConverter javaFXConverter  = new JavaFXFrameConverter()) {
                    return javaFXConverter.convert(frame);
                }
            }
        }
    }

    
}
