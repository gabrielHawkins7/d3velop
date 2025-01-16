package com.d3velop.Renderer;

import java.io.IOException;
import java.util.ArrayList;

import com.d3velop.Shaders.S_Edit;
import com.d3velop.Shaders.S_Image;
import com.d3velop.Shaders.Shader;
import com.d3velop.Shaders.Shader.InvalidShaderException;

public class Renderer {
    public FrameBuffer fb;
    public ArrayList<Shader> shader_list;
    private S_Image image_shader;
    private S_Edit edit_shader;

    public Renderer(){
        this.fb = new FrameBuffer();
        shader_list = new ArrayList<>();
        image_shader = new S_Image();
        edit_shader = new S_Edit();
        shader_list.add(image_shader);
        shader_list.add(edit_shader);

    }

    public void render(Texture tex){
        fb.size(tex.width,tex.height);
        fb.bind();
        image_shader.setImageId(tex.texture_id);
        image_shader.render();
        edit_shader.render();
        fb.unbind();

    }

    public void compile_shaders() throws InvalidShaderException, IOException{
        for(Shader i : shader_list){
            i.compile();
        }
    }
}
