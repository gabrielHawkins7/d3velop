package com.d3dev.Utils;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;

public class Image {
    public BufferedImage open_image;
    public ColorSpace original_cs;
    public ColorSpace current_cs;
    public File img_file;

    public Image(BufferedImage in){
        open_image = in;
        original_cs = in.getColorModel().getColorSpace();
        current_cs = original_cs;
    }

    public BufferedImage get(){
        return open_image;
    }
}   
