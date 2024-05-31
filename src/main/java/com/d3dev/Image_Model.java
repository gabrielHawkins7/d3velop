package com.d3dev;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Image_Model
 */
public class Image_Model {
    public BufferedImage open_image;
    public ColorSpace original_cs;
    public ColorSpace current_cs;
    public File img_file;

    public Image_Model(BufferedImage in){
        open_image = in;
        original_cs = in.getColorModel().getColorSpace();
        current_cs = original_cs;
    }

    public BufferedImage get(){
        return open_image;
    }
}