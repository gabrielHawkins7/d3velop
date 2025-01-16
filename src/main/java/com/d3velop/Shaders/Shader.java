package com.d3velop.Shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public abstract class Shader {
    public String name = "empty shader";
    public int program;
    public boolean _compiled;
    private int vao;
    public String fragsource;
    public String frag_content;
    public String vertsource;
    public String vert_content;



    private float[] vertices = {
        -1f, -1f, 0.0f,  0.0f, 0.0f, // Bottom left
         1f, -1f, 0.0f,  1.0f, 0.0f, // Bottom right
        -1f,  1f, 0.0f,  0.0f, 1.0f, // Top left
         1f,  1f, 0.0f,  1.0f, 1.0f  // Top right
    };

    public void setFragSource(String frag){
        this.fragsource  = frag;
    }
    public void setVertSource(String vert){
        this.vertsource = vert;
    }

    public void compile() throws InvalidShaderException, IOException{
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);

        if(vert_content == null){
            vert_content = new String(Files.readAllBytes(Paths.get(vertsource)));
        }


        glShaderSource(vertexShader, vert_content);
        glCompileShader(vertexShader);

       // System.out.println(glGetShaderInfoLog(vertexShader));

        if(glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE){
            _compiled = false;
            throw new InvalidShaderException("Unable to compile Vertex Shader for " + name + ": " + glGetShaderInfoLog(vertexShader));
        }

        
        
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

        if(frag_content == null){
            frag_content = new String(Files.readAllBytes(Paths.get(fragsource)));
        }


        glShaderSource(fragmentShader, frag_content);
        glCompileShader(fragmentShader);

        //System.out.println(glGetShaderInfoLog(fragmentShader));
        if(glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE){
            _compiled = false;
            throw new InvalidShaderException("Unable to compile Frag Shader for  " + name + ": " + glGetShaderInfoLog(fragmentShader));
        }
        

        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glLinkProgram(program);
        _compiled = true;
    }
    public void render(){
            glUseProgram(program);
            glBindVertexArray(vao);
            //glBindTexture(GL_TEXTURE_2D, imageid);
            bindData();
            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }
    public void bindData(){

    }

    public void getControlPanel(){
        
    }
    public class InvalidShaderException extends Exception { 
        public InvalidShaderException(String errorMessage) {
            super(errorMessage);
        }
    }

}


