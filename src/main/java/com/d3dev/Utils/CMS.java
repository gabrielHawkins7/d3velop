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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.d3dev.App;

import javafx.scene.control.Label;


public class CMS {

    public static HashMap<String, ColorSpace> getProfiles(){
        HashMap<String, ColorSpace> profiles = new HashMap<>();
        // if(System.getProperty("os.name").equals("Mac OS X")){
        //     URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
        //     Path path;
        //     try {
        //         path = Paths.get(url.toURI());
        //         Path o = path.getParent().getParent().getParent().resolve("Contents/Resources/ICC_PROFILES");
        //         File f = o.toFile();
        //         for(File x : f.listFiles()){
        //             profiles.put(x.getName().substring(0, x.getName().length() - 4), new ICC_ColorSpace(ICC_Profile.getInstance(x.getPath())));
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }else{
        //     URL url = App.class.getProtectionDomain().getCodeSource().getLocation();
        //     Path path;
        //     try {
        //         path = Paths.get(url.toURI());
        //         Path o = path.getParent();
        //         File f = new File(o.toString() + "\\ICC_PROFILES");
        //         for(File x : f.listFiles()){
        //             profiles.put(x.getName().substring(0, x.getName().length() - 4), new ICC_ColorSpace(ICC_Profile.getInstance(x.getPath())));
        //         }
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }

        File f = new File("src/main/resources/com/d3dev/ICC_PROFILES");
        for(File x : f.listFiles()){
            try {
                profiles.put(x.getName().substring(0, x.getName().length() - 4), new ICC_ColorSpace(ICC_Profile.getInstance(x.getPath())));
            } catch (IOException e) {
                // TODO Auto-generated catch block
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
    
    static BufferedImage equalizeHist16bit(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int histR[] = new int[65536];
        int histG[] = new int[65536];
        int histB[] = new int[65536];

        // Step 1: Create histogram in parallel
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = (height + numThreads - 1) / numThreads;
        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * chunkSize;
            final int endRow = Math.min(startRow + chunkSize, height);
            executor.submit(() -> {
                int[] localHistR = new int[65536];
                int[] localHistG = new int[65536];
                int[] localHistB = new int[65536];
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < width; x++) {
                        int[] pixel = new int[3];
                        image.getRaster().getPixel(x, y, pixel);
                        localHistR[pixel[0]]++;
                        localHistG[pixel[1]]++;
                        localHistB[pixel[2]]++;
                    }
                }
                synchronized (histR) {
                    for (int j = 0; j < 65536; j++) {
                        histR[j] += localHistR[j];
                        histG[j] += localHistG[j];
                        histB[j] += localHistB[j];
                    }
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Step 2: Create CDF in place
        for (int i = 1; i < 65536; ++i) {
            histR[i] += histR[i - 1];
            histG[i] += histG[i - 1];
            histB[i] += histB[i - 1];
        }
        // Step 3: Scale histogram back to 16-bit
        int max = histR[65535];
        for (int i = 0; i < 65536; i++) {
            histR[i] = (int) Math.round(histR[i] * (65536.0) / max);
            histG[i] = (int) Math.round(histG[i] * (65536.0) / max);
            histB[i] = (int) Math.round(histB[i] * (65536.0) / max);
        }
        // Step 4: Apply histogram in parallel
        executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            final int startRow = i * chunkSize;
            final int endRow = Math.min(startRow + chunkSize, height);

            executor.submit(() -> {
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < width; x++) {
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
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return image;
    }

    static BufferedImage invert(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int numThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int chunkHeight = height / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startY = i * chunkHeight;
            int endY = (i == numThreads - 1) ? height : startY + chunkHeight;

            executor.submit(() -> {
                for (int x = 0; x < width; x++) {
                    for (int y = startY; y < endY; y++) {
                        int[] tmp = new int[image.getColorModel().getNumComponents()];
                        image.getRaster().getPixel(x, y, tmp);
                        for (int j = 0; j < tmp.length; j++) {
                            tmp[j] = 65535 - tmp[j];
                        }
                        image.getRaster().setPixel(x, y, tmp);
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return image;
    }

    public static BufferedImage convertTo16Bit(BufferedImage image){
        ColorSpace cs = image.getColorModel().getColorSpace();
        ComponentColorModel cm = new ComponentColorModel(cs, false, false, BufferedImage.OPAQUE, DataBuffer.TYPE_USHORT);
        WritableRaster wr = Raster.createInterleavedRaster(DataBuffer.TYPE_USHORT, image.getWidth(), image.getHeight(), image.getWidth() * 3, 3, new int[]{0, 1, 2}, null);
        BufferedImage out = new BufferedImage(cm, wr, false, null);

        int width = image.getWidth();
        int height = image.getHeight();
        int chunkHeight = height / 4;

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            final int startY = i * chunkHeight;
            final int endY = (i == 4 - 1) ? height : startY + chunkHeight;

            Future<?> future = executor.submit(() -> {
                for (int x = 0; x < width; x++) {
                    for (int y = startY; y < endY; y++) {
                        int[] pixel = new int[image.getColorModel().getNumComponents()];
                        image.getRaster().getPixel(x, y, pixel);
                        if (image.getColorModel().getPixelSize() == 48) {
                            int[] outPix = {pixel[0], pixel[1], pixel[2]};
                            out.getRaster().setPixel(x, y, outPix);
                        } else {
                            int[] outPix = {pixel[0] << 8, pixel[1] << 8, pixel[2] << 8};
                            out.getRaster().setPixel(x, y, outPix);
                        }
                    }
                }
            });
            futures.add(future);
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return out;
    }
}
