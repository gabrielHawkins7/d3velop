package com.d3dev.Utils;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class CMS {

    public static HashMap<String, ColorSpace> getProfiles(){
        HashMap<String, ColorSpace> profiles = new HashMap<>();
        File icc_directory = new File("src/main/resources/com/d3dev/ICC_PROFILES");
        for(File x : icc_directory.listFiles()){
            try {
                profiles.put(x.getName().substring(0, x.getName().length() - 4), new ICC_ColorSpace(ICC_Profile.getInstance(x.getPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return profiles;
    }

    public static BufferedImage convert_space(BufferedImage image, String profile) throws IOException{
        ColorSpace cs = new ICC_ColorSpace(ICC_Profile.getInstance(profile));
        if(image.getColorModel().getPixelSize() == 48){
            ComponentColorModel cm = new ComponentColorModel(cs, new int[] {16, 16, 16},false, false, Transparency.OPAQUE,DataBuffer.TYPE_USHORT);
            return new BufferedImage(cm, image.getRaster(), cm.isAlphaPremultiplied(), null);
        }else if(image.getColorModel().getPixelSize() == 64){
            ComponentColorModel cm = new ComponentColorModel(cs, new int[] {16, 16, 16,16},true, false, Transparency.OPAQUE,DataBuffer.TYPE_USHORT);
            return new BufferedImage(cm, image.getRaster(), cm.isAlphaPremultiplied(), null);
        }else {
            ColorConvertOp cco = new ColorConvertOp(cs,null);
            return cco.filter(image, null);
        }
    }
    public static BufferedImage convert_space(BufferedImage image, ColorSpace cs) throws IOException{
        if(image.getColorModel().getPixelSize() == 48){
            ComponentColorModel cm = new ComponentColorModel(cs, new int[] {16, 16, 16},false, false, Transparency.OPAQUE,DataBuffer.TYPE_USHORT);
            return new BufferedImage(cm, image.getRaster(), cm.isAlphaPremultiplied(), null);
        }else if(image.getColorModel().getPixelSize() == 64){
            ComponentColorModel cm = new ComponentColorModel(cs, new int[] {16, 16, 16,16},true, false, Transparency.OPAQUE,DataBuffer.TYPE_USHORT);
            return new BufferedImage(cm, image.getRaster(), cm.isAlphaPremultiplied(), null);
        }else {
            ColorConvertOp cco = new ColorConvertOp(cs,null);
            return cco.filter(image, null);
        }
    }
    
    static BufferedImage equalizeHist16bit(BufferedImage image){
        int histR[] = new int[65536];
        int histG[] = new int[65536];
        int histB[] = new int[65536];
        //create histogram
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int[] pixel = new int[3];
                image.getRaster().getPixel(x, y, pixel);
                histR[pixel[0]] = histR[pixel[0]] +1; 
                histG[pixel[1]] = histG[pixel[1]] +1; 
                histB[pixel[2]] = histB[pixel[2]] +1; 
            }
        }
        //create CDF in place
       for ( int i = 1; i < 65536; ++i ){
                histR[i] =  histR[i-1] + histR[i];
                histG[i] =  histG[i-1] + histG[i];
                histB[i] =  histB[i-1] + histB[i];
        }
        //scale hist back to 16bit
        int max = histR[histR.length -1];
        for(int i = 0; i < histR.length; i++){
            histR[i] = (int) Math.round(histR[i] * (65536.0) / max);
            histG[i] = (int) Math.round(histG[i] * (65536.0) / max);
            histB[i] = (int) Math.round(histB[i] * (65536.0) / max);
        }
        //apply hist
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int[] oldPixel = new int[3];
                image.getRaster().getPixel(x, y, oldPixel);

                int[] newPixel = {
                    histR[oldPixel[0]],
                    histG[oldPixel[1]],
                    histB[oldPixel[2]]
                };

                image.getRaster().setPixel(x, y, newPixel);
            }   
        }
        histR = null;
        histG = null;
        histB = null;
        return image;
    }

    static BufferedImage invert(BufferedImage image){
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int[] tmp = new int[image.getColorModel().getNumComponents()];
                image.getRaster().getPixel(x ,y,tmp);
                for(int i = 0; i < tmp.length; i++){
                    tmp[i] = 65535 - tmp[i];
                }
                image.getRaster().setPixel(x, y, tmp);
            }
        }
        return image;
    }

    public static BufferedImage convertTo16Bit(BufferedImage image){
        
        ColorSpace cs = image.getColorModel().getColorSpace();
        ComponentColorModel cm = new ComponentColorModel(cs, false, false, BufferedImage.OPAQUE, DataBuffer.TYPE_USHORT);
        WritableRaster wr = Raster.createInterleavedRaster(DataBuffer.TYPE_USHORT, image.getWidth(), image.getHeight(), image.getWidth() * 3,3,new int[]{0,1,2}, null);
        BufferedImage out = new BufferedImage(cm, wr, false, null);
        for(int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int[] pixel = new int[image.getColorModel().getNumComponents()];
                image.getRaster().getPixel(x, y, pixel);
                
                if(image.getColorModel().getPixelSize() == 48){
                    int[] outPix = {pixel[0] , pixel[1] , pixel[2] };
                    out.getRaster().setPixel(x, y, outPix);

                }else{
                    int[] outPix = {pixel[0] << 8, pixel[1] << 8, pixel[2] << 8};
                    out.getRaster().setPixel(x, y, outPix);

                }
                
            }
        }

        return out;
    }
}
