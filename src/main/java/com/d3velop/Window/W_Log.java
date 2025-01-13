package com.d3velop.Window;

import com.d3velop.Controller.Data;
import com.d3velop.Controller.Props;

import imgui.ImGui;

public class W_Log {
    public static void view(){
        if(ImGui.begin("Logs")){
            Data.log.get().forEach(i -> {
                ImGui.text(i);
            });
            if(Props._logscroll){
                ImGui.setScrollHereY(1.0f);
                Props._logscroll = false;
            }
        }
        ImGui.end();
    }
}
