package com.d3velop.Shaders;
import org.lwjgl.opengl.GL20;

import com.d3velop.Controller.Edit_Props;
import com.d3velop.Controller.Levels_Props;

public class S_Levels extends Shader {

    
    public S_Levels() {
        name = "Levels Shader";
        
        String vertsource = "src/main/java/com/d3velop/Shader_src/image.vert";
        setVertSource(vertsource);
        String fragsource = "src/main/java/com/d3velop/Shader_src/levels.frag";
        setFragSource(fragsource);
       
    }


    @Override
    public void bindData() {
        int _Ltint = GL20.glGetUniformLocation(program, "_Ltint");
        int _Htint = GL20.glGetUniformLocation(program, "_Htint");
        int _Lsaturation = GL20.glGetUniformLocation(program, "_Lsaturation");
        int _Hsaturation = GL20.glGetUniformLocation(program, "_Hsaturation");
        GL20.glUniform1f(_Ltint, Levels_Props._Ltint[0]);
        GL20.glUniform1f(_Htint, Levels_Props._Htint[0]);
        GL20.glUniform1f(_Lsaturation, Levels_Props._Lsaturation[0]);
        GL20.glUniform1f(_Hsaturation, Levels_Props._Hsaturation[0]);





        super.bindData();
    }

}
