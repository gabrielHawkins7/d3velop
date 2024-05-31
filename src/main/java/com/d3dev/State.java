package com.d3dev;

import java.awt.image.BufferedImage;
import java.io.File;

public class State {
    props_ props;
    BufferedImage openImage;
    File openFile;

    State(props_ props, BufferedImage openImage, File openFile){
        this.props = props;
        this.openImage = openImage;
        this.openFile = openFile;
    }
}
