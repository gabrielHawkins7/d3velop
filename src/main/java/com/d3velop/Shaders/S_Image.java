package com.d3velop.Shaders;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL30.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import imgui.ImGui;

public class S_Image extends Shader {

    private int imageid;
    
    public S_Image() {
        name = "Image Shader";
        
        String vertsource = "src/main/java/com/d3velop/Shader_src/image.vert";
        setVertSource(vertsource);
        String fragsource = "src/main/java/com/d3velop/Shader_src/image.frag";
        setFragSource(fragsource);
    }

    public void setImageId(int imageid){
        this.imageid = imageid;
    }

    @Override
    public void bindData() {
        glBindTexture(GL_TEXTURE_2D, imageid);
        super.bindData();
    }

    @Override
    public void getControlPanel() {
        //super.getControlPanel();
        ImGui.text("Image Shader");
        ImGui.separator();
        ImGui.text("Image ID : " + imageid);
    }
}