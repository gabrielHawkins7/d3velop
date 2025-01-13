package com.d3velop.Window;

import org.lwjgl.glfw.GLFW;

import com.d3velop.Controller.Data;

import static imgui.ImGui.*;
import imgui.flag.ImGuiWindowFlags;

public class W_Crash {
    public static String error;

    public static void setCrash(String error){
        W_Crash.error = error;
    }
    public static void view(){
        openPopup("Hard-Crash");
        int flags = ImGuiWindowFlags.NoDecoration;
        if(beginPopupModal("Hard-Crash", flags)){
            text(error);
            if(button("Exit")){
                GLFW.glfwSetWindowShouldClose(Data.glfw_win, true);
            }
        }
        endPopup();
    }
}
