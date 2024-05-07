package com.controlstest;

import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.indexer.UByteArrayIndexer;
import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.indexer.UShortRawIndexer;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameConverter;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_cudaarithm;
import org.bytedeco.opencv.global.opencv_cudabgsegm;
import org.bytedeco.opencv.global.opencv_cudacodec;
import org.bytedeco.opencv.global.opencv_cudaimgproc;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.global.opencv_ximgproc;
import org.bytedeco.opencv.global.opencv_xphoto;
import org.bytedeco.opencv.opencv_core.Algorithm;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_imgproc.CLAHE;
import org.bytedeco.opencv.opencv_xphoto.SimpleWB;
import org.bytedeco.opencv.opencv_xphoto.WhiteBalancer;

import com.controlstest.CONTROLS.COLOR;
import com.controlstest.CONTROLS.OUTPUT_LOOK;
import com.controlstest.CONTROLS.SCANNER;
import com.controlstest.CONTROLS.SHARPENING;
import com.controlstest.CONTROLS.SRC_GAMMA;
import com.controlstest.CONTROLS.WHITEBALANCE;


public class Developer {
    static BufferedImage develop(BufferedImage image) throws IOException{
        ColorSpace cs = new ICC_ColorSpace(ICC_Profile.getInstance("src/main/resources/com/controlstest/ICC_Profiles/LargeRGB-elle-V4-labl.icc"));
        ColorConvertOp cco = new ColorConvertOp(cs,null);
        image = cco.filter(image, null);

        Java2DFrameConverter jfc = new Java2DFrameConverter();
        Mat i = Java2DFrameUtils.toMat(jfc.getFrame(image, 1.0 , true));
        opencv_core.normalize(i,i,0,65535,opencv_core.NORM_MINMAX,opencv_core.CV_16UC3, null);

        //opencv_core.bitwise_not(i, i);

        int[] max = new int[3];
        int[] min = new int[3];

        


        return Java2DFrameUtils.toBufferedImage(i);
    }
   
}
