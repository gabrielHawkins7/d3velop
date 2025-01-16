package com.d3velop.Shaders;

import org.lwjgl.opengl.GL20;

import com.d3velop.Controller.Edit_Props;

public class S_Edit extends Shader {

    
    public S_Edit() {
        name = "Edit Shader";
        
        String vertsource = "src/main/java/com/d3velop/Shader_src/image.vert";
        setVertSource(vertsource);
        String fragsource = "src/main/java/com/d3velop/Shader_src/edit.frag";
        setFragSource(fragsource);
       
    }


    @Override
    public void bindData() {

        int _temp = GL20.glGetUniformLocation(program, "_temp");
        int _tint = GL20.glGetUniformLocation(program, "_tint");
        int _exposure = GL20.glGetUniformLocation(program, "_exposure");
        int _contrast = GL20.glGetUniformLocation(program, "_contrast");
        int _low = GL20.glGetUniformLocation(program, "_low");
        int _high = GL20.glGetUniformLocation(program, "_high");
        int _saturation = GL20.glGetUniformLocation(program, "_saturation");





        GL20.glUniform1f(_temp, Edit_Props._temp[0]);
        GL20.glUniform1f(_tint, Edit_Props._tint [0]);
        GL20.glUniform1f(_exposure, Edit_Props._exposure[0]);
        GL20.glUniform1f(_contrast, Edit_Props._contrast[0]);
        GL20.glUniform1f(_low, Edit_Props._low[0]);
        GL20.glUniform1f(_high, Edit_Props._high[0]);
        GL20.glUniform1f(_saturation, Edit_Props._saturation[0]);


        
        
        
        
        

        super.bindData();
    }

}
