package com.d3dev.Utils;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Image {
    public BufferedImage open_image;
    public ColorSpace original_cs;
    public ObjectProperty<ColorSpace> current_cs = new SimpleObjectProperty<ColorSpace>();
    public File img_file;

    Image(BufferedImage in){
        open_image = in;
        original_cs = in.getColorModel().getColorSpace();
        current_cs.setValue(original_cs);
    }

    public BufferedImage get(){
        return open_image;
    }
}   
