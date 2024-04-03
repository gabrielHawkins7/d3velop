package com.d3dev;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

@SuppressWarnings("exports")
public class ImageTransformer {
    public static Image rotateImage(Image i, int d){
        ImageView iv = new ImageView(i);
        iv.setRotate(d);

        
        return iv.snapshot(null, null);
    }
}
