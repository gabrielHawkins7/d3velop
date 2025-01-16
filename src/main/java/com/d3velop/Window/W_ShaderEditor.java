package com.d3velop.Window;

import static imgui.ImGui.*;

import java.io.IOException;
import java.lang.ModuleLayer.Controller;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.d3velop.Controller.Data;
import com.d3velop.Shaders.Shader;
import com.d3velop.Shaders.Shader.InvalidShaderException;

import imgui.ImVec2;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

public class W_ShaderEditor {
    private static ImString shader_src = new ImString(10000);
    private static boolean modified = false;
    private static int shader_idx;
    private static Shader open_shader;

   
    public static void view(){
        if(modified == false){
            open_shader = Data.renderer.shader_list.get(shader_idx);
            shader_src.set(open_shader.frag_content);
            
        }
       if(begin("Shader Editor")){
        if(beginCombo("Select Shader", Data.renderer.shader_list.get(shader_idx).name)){
            
            for (int i = 0; i < Data.renderer.shader_list.size(); i++) {
                boolean isSelected = (shader_idx == i);
                if (selectable(Data.renderer.shader_list.get(i).name, isSelected)) {
                    shader_idx = i; // Update the current shader index
                    open_shader = Data.renderer.shader_list.get(shader_idx);
                    shader_src.set(open_shader.frag_content);
                }
                if (isSelected) {
                    setItemDefaultFocus();
                }
            }
            endCombo();
        }

        ImVec2 size = getWindowContentRegionMax();

        if(inputTextMultiline("GLSL Editor",shader_src,size.x, size.y - 100, ImGuiInputTextFlags.AllowTabInput)){
            modified = true;
        }
        

        if(button("Compile")){
            open_shader.frag_content = shader_src.get();
            modified = false;
            try {
                open_shader.compile();
            } catch (InvalidShaderException | IOException e) {
                com.d3velop.Controller.logError(e.getMessage());
            }
        }
        if(button("Save To File")){
            open_shader.frag_content = shader_src.get();
            modified = false;
            try {
                open_shader.compile();
                Files.write(Paths.get(open_shader.fragsource), shader_src.get().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                com.d3velop.Controller.logInfo("Saved " + open_shader.name + " to file ");
            } catch (InvalidShaderException | IOException e) {
                com.d3velop.Controller.logError(e.getMessage());
            }
        }


       }
       end();
    }
    
}
