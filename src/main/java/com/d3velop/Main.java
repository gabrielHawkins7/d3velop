package com.d3velop;

import java.io.File;

import org.lwjgl.glfw.GLFWNativeCocoa;
import org.lwjgl.opengl.GLUtil;

import com.d3velop.Controller.Loading;
import com.d3velop.Controller.Props;
import com.d3velop.GL.GLUtils;
import com.d3velop.Window.W_Crash;
import com.d3velop.Window.W_Dev;
import com.d3velop.Window.W_ImageView;
import com.d3velop.Window.W_Loading;
import com.d3velop.Window.W_Log;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;

public class Main extends Application {
    @Override
    protected void configure(Configuration config) {
        config.setTitle("D3VELOP");
    }

    @Override
    protected void preRun() {
        Controller controller = new Controller(handle);
        Props._devmode = true;
        Props._log = true;
        

    }

    @Override
    public void process() {
        GLUtils.processTasks();


        W_ImageView.view();

        

        if(Props._devmode){
            W_Dev.view();
        }

        if(Props._log){
            W_Log.view();
        }



        if(Loading._is_loading){
            W_Loading.view();
        }

        if(W_Crash.error != null){
            W_Crash.view();
        }


        


    }

    public static void main(String[] args) {
        launch(new Main());
    }
}