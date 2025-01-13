package com.d3velop.Renderer;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.io.File;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import com.d3velop.Controller;
import com.d3velop.Controller.Loading;
import com.d3velop.GL.GLUtils;

import app.photofox.vipsffm.VBlob;
import app.photofox.vipsffm.VImage;
import app.photofox.vipsffm.VSource;
import app.photofox.vipsffm.Vips;
import app.photofox.vipsffm.VipsError;
import app.photofox.vipsffm.VipsHelper;
import app.photofox.vipsffm.VipsOption;
import app.photofox.vipsffm.VipsRunnable;
import app.photofox.vipsffm.enums.VipsAccess;
import app.photofox.vipsffm.enums.VipsBandFormat;
import app.photofox.vipsffm.enums.VipsCoding;
import app.photofox.vipsffm.enums.VipsInterpretation;

public class Texture {
    public int width;
    public int height;
    public int texture_id;
    ShortBuffer rgb_16_buffer;
    public File fn;

    private String icc_path;

    public Texture(File fn){
        this.fn = fn;
        loadImage(fn);

        URL resourceUrl = Texture.class.getClassLoader().getResource("ProPhotoRGB_g10.icc");
        icc_path = Paths.get(resourceUrl.getPath()).toAbsolutePath().toString();

    }

    public void loadImage(File fn){
        this.fn = fn;
        String file_name = fn.getAbsolutePath();
        if(!Controller.Props._vipsready){
            Controller.logError("Vips Is Not Enabled !!!! ");
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Controller.logInfo("Lodaing file: " + fn.getName());
                Controller.Loading._is_loading = true;

                Vips.init();
                Vips.run(new VipsRunnable() {
                    @Override
                    public void run(Arena arg0) {
                        try{
                        VImage image = VImage.newFromFile(arg0, file_name);
                        Controller.Loading._loading_progress += 10;

                        if(image.hasAlpha()){
                            VImage r = image.extractBand(0);
                            VImage g = image.extractBand(1);
                            VImage b = image.extractBand(2);
                            image = VImage.bandjoin(arg0, Arrays.asList(new VImage[]{r,g,b}));
                        }
                        Controller.Loading._loading_progress += 10;


                        if(Controller.Props._invert){
                            image = image.histEqual().invert();

                        }
                        Controller.Loading._loading_progress += 10;


                        image = image.addalpha();
                        Controller.Loading._loading_progress += 10;


                        image = image.autorot();

                        
                        
                        image = image.cast(VipsBandFormat.FORMAT_USHORT, VipsOption.Boolean("shift", true));
                        Controller.Loading._loading_progress += 10;

                        

                        

                        image.iccTransform(icc_path, VipsOption.Boolean("embedded", true));
                        Controller.Loading._loading_progress += 10;
                        

                        
                        
                        VBlob blob = image.rawsaveBuffer();
                        Controller.Loading._loading_progress += 10;

                        
                        short[] img = blob.getUnsafeDataAddress().toArray(ValueLayout.JAVA_SHORT);
                        Controller.Loading._loading_progress += 10;

                        
                        
                        
                        width = image.getWidth();
                        height = image.getHeight();

                        rgb_16_buffer = BufferUtils.createShortBuffer(img.length);
                        rgb_16_buffer.put(img);
                        rgb_16_buffer.flip();
                        Controller.Loading._loading_progress += 10;


                        GLUtils.invokeLater(()->{

                            if(texture_id != 0){
                                GL33.glDeleteTextures(texture_id);
                            }

                            // Generate a new OpenGL texture ID
                            texture_id = GL33.glGenTextures();
                            // Bind the texture
                            GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture_id);
                            // Set texture parameters
                            GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR);
                            GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR);
                            
                            // Upload texture data to OpenGL
                            GL33.glTexImage2D(
                                GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA16, width, height, 0,
                                GL33.GL_RGBA , GL33.GL_UNSIGNED_SHORT, rgb_16_buffer
                            );
                            Controller.Loading._loading_progress = 0;
                            
                        });
                        Controller.Loading._is_loading = false;
                        }catch(Exception e){
                            Controller.logError("Unable to load file : " + e.getMessage());
                        }
                    }
                });
            }
        }).start();
        

        // Vips.init();
        // Vips.run(new VipsRunnable() {
        //     @Override
        //     public void run(Arena arg0) {

                

        //         VImage image = VImage.newFromFile(arg0, fn);

        //         image = image.addalpha();
                
                
        //         image = image.cast(VipsBandFormat.FORMAT_USHORT, VipsOption.Boolean("shift", true));
                

                

        //         image.iccTransform("/Users/gabe/Documents/DEV/Java/gluitest/src/main/resources/AdobeRGB_g10.icc", VipsOption.Boolean("embedded", true));
                
                
                
        //         VBlob blob = image.rawsaveBuffer();
                
        //         short[] img = blob.getUnsafeDataAddress().toArray(ValueLayout.JAVA_SHORT);
                
                
                
        //         width = image.getWidth();
        //         height = image.getHeight();

        //         rgb_16_buffer = BufferUtils.createShortBuffer(img.length);
        //         rgb_16_buffer.put(img);
        //         rgb_16_buffer.flip();
                


        //         // rgb_16_buffer = BufferUtils.createShortBuffer(img.length);
        //         // rgb_16_buffer.put(img);
        //         // rgb_16_buffer.flip();

        //         // Generate a new OpenGL texture ID
        //         texture_id = GL33.glGenTextures();
        //         // Bind the texture
        //         GL33.glBindTexture(GL33.GL_TEXTURE_2D, texture_id);
        //         // Set texture parameters
        //         GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MIN_FILTER, GL33.GL_LINEAR);
        //         GL33.glTexParameteri(GL33.GL_TEXTURE_2D, GL33.GL_TEXTURE_MAG_FILTER, GL33.GL_LINEAR);
                
        //         // Upload texture data to OpenGL
        //         GL33.glTexImage2D(
        //             GL33.GL_TEXTURE_2D, 0, GL33.GL_RGBA16, width, height, 0,
        //             GL33.GL_RGBA , GL33.GL_UNSIGNED_SHORT, rgb_16_buffer
        //         );
                
        //     }
        // });
        // Vips.shutdown();
        
        
        
    }

}

