package com.d3velop.Window;

import imgui.ImVec2;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import static imgui.ImGui.*;

import com.d3velop.Controller.Data;
import com.d3velop.Controller.Loading;

public class W_Loading {
    public static void view(){
        setNextWindowPos(new ImVec2(Data.io.getDisplaySizeX() * 0.5f, Data.io.getDisplaySizeY() * 0.5f), ImGuiCond.Always, new ImVec2(0.5f,0.5f));
        int flags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoSavedSettings;
        if(begin("Loading....", flags)){
            text("Loading...");
            progressBar(Loading._loading_progress / 100.0f, new ImVec2(600, 20));
        }
        end();
    }
}
