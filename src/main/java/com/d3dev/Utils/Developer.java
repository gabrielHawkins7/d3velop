package com.d3dev.Utils;

import java.awt.image.BufferedImage;

public class Developer {


    public static BufferedImage develop(BufferedImage image){
        image = CMS.equalizeHist16bit(image);
        image = CMS.invert(image);
        return image;
        
    }
}