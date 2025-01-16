package com.d3velop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import com.d3velop.Renderer.Renderer;
import com.d3velop.Renderer.Texture;
import com.d3velop.Shaders.Shader;
import com.d3velop.Shaders.Shader.InvalidShaderException;

import imgui.ImGui;
import imgui.ImGuiIO;

public class Controller {
    public static Native Native = new Native();

    
    

    public class Data {
        public static AtomicReference<ArrayList<String>> log = new AtomicReference<>(new ArrayList<>());
        public static long glfw_win;
        public static ImGuiIO io;
        public static Texture current_texture;
        public static Renderer renderer;

    }

    public class Edit_Props{
        public static float[] _temp = new float[1];   
        public static float[] _tint = new float[1];
        public static float[] _exposure = {0.0f};
        public static float[] _contrast = {1.0f};
        public static float[] _high = {1.0f};
        public static float[] _low = {0.0f};
        public static float[] _saturation = {1.0f};


    }

    public class Props{
        public static boolean  _logscroll;
        public static boolean  _devmode;
        public static boolean  _vipsready;
        public static boolean  _invert;
        public static boolean  _cmm_enabled;
        public static String  _display_icc;
        public static boolean  _log;
        public static float _zoom = 1.0f;
    }

    public class Loading{
        public static boolean _is_loading;
        public static int _loading_progress;
    }


    Controller(long glfw_win){
        Data.glfw_win = glfw_win;
        Data.io = ImGui.getIO();
        Data.renderer = new Renderer();
        compile_shaders();
    }


    public static void compile_shaders(){
        for(Shader i : Data.renderer.shader_list){
            try {
                i.compile();
            } catch (InvalidShaderException | IOException e) {
                Controller.logError(e.getMessage());
            }
            if(i._compiled){
                logInfo(i.name + " : Compiled");
            }
        }
    }

    public static void load_texture(File fn){
        if(Data.current_texture != null){
            Data.current_texture.loadImage(fn);
        }else{
            Data.current_texture = new Texture(fn);
        }
    }

    public static void openFile(){
        File f = NFD.openFile();
        if(f != null){
            load_texture(f);
        }
    }



    public static void logError(String err){
        Props._logscroll = true;
        Data.log.get().add("[D3_ERROR] : " + err);
    }

    public static void logMessage(String msg){
        Props._logscroll = true;
        Data.log.get().add("[D3_MESSAGE] : " + msg);
    }
    public static void logInfo(String info){
        Props._logscroll = true;
        Data.log.get().add("[D3_INFO] : " + info);
    }
    public static void logDev(String info){
        Props._logscroll = true;
        if(Props._devmode){Data.log.get().add("[D3_DEV] : " + info);};
    }
}
