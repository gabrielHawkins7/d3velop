package com.d3velop.Window;
import static imgui.ImGui.*;

import java.io.File;
import java.util.UUID;

import com.d3velop.Controller;
import com.d3velop.Controller.Data;
import com.d3velop.Controller.Loading;
import com.d3velop.Controller.Props;
import com.d3velop.Native.Vips_Lib;
import com.d3velop.Shaders.Shader;
import com.d3velop.Shaders.Shader.InvalidShaderException;

import imgui.ImGui;


public class W_Dev{
    public static void view(){
        if(ImGui.begin("Dev")){
                ImGui.text("Color Managment : " + (Props._cmm_enabled ? "Enabled" : "Disabled"));
                ImGui.text("Display ICC : " + Props._display_icc);
                ImGui.text("Vips : " + (Vips_Lib.vips_loaded ? "Loaded" : "UnLoaded"));
                ImGui.text("Vips : " + (Vips_Lib.vips_enabled ? "Enabled" : "Disabled"));
                //ImGui.text("Image Texture ID : " + (Controller.current_texture == null ? "Empty" : Controller.current_texture.texture_id));
                if(ImGui.button("Open")){
                    Controller.openFile();
                }

                if(ImGui.button("Test Loading")){
                    testLoading();
                }
                if(ImGui.button("Show Color Chart")){
                    Controller.load_texture(new File("/Users/gabe/Documents/DEV/Java/d3velop/src/main/resources/colorchecker_adobe22.png"));
                }
                if(ImGui.button("Invert")){
                    if(Data.current_texture != null){
                        Props._invert = (Props._invert ? false : true);
                        Controller.load_texture(Data.current_texture.fn);
                    }
                }
                if(ImGui.button("Test Crash")){
                    W_Crash.setCrash("This is a crash!!!!");
                }

                text("Open Image: " + (Data.current_texture == null? "Empty" : Data.current_texture.fn.getName()));
                text("_logscroll: " + Props._logscroll);
                text("_devmode: " + Props._devmode);
                text("_vipsready: " + Props._vipsready);
                text("_invert: " + Props._invert);
                text("_cmm_enabled: " + Props._cmm_enabled);
                text("_display_icc: " + Props._display_icc);
                text("_log: " + Props._log);
                text("_is_loading: " + Loading._is_loading);
                text("_loading_progress: " + Loading._loading_progress);
                text("_zoom: " + Props._zoom);

                if(beginTable("Shaders",3)){
                    tableSetupColumn("Shader");
                    tableSetupColumn("Compiled");
                    tableSetupColumn("ID");

                    tableHeadersRow();

                    for(Shader i : Data.renderer.shader_list){
                        tableNextRow();
                        tableNextColumn();
                        text(i.name);
                        tableNextColumn();
                        text(i._compiled ? "YES":"NO");
                        tableNextColumn();
                        text(i.id.toString());
                        
                    }
                    endTable();
                }
                if(button("Compile Shaders")){
                    Controller.compile_shaders();
                }
            }
            ImGui.end();
    }

    private static void testLoading(){
        //Load_Status._is_loading = true;
        Loading._is_loading = true;
        new Thread(()->{
            int prog = 0;
            while(prog < 100){
                try {
                    Thread.sleep(100);
                    prog += 10;
                    Loading._loading_progress = prog;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
            Loading._is_loading = false;
        }).start();
    }
}
