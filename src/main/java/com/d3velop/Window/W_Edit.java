package com.d3velop.Window;
import static imgui.ImGui.*;

import com.d3velop.Controller.Edit_Props;
import com.d3velop.Controller.Levels_Props;

import imgui.ImGuiViewport;
import imgui.flag.ImGuiSliderFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;


public class W_Edit {
    
    
        public static void view(){

            int flags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoSavedSettings;
            ImGuiViewport viewport = getMainViewport();
            int imgviewSize = (int) ((int) viewport.getSizeX() - (viewport.getSizeX() * .3));
            setNextWindowPos(imgviewSize, viewport.getWorkPosY());

            setNextWindowSize(viewport.getWorkSizeX() - imgviewSize, viewport.getWorkSizeY());


            if(begin("Edit")){
                text("General");
                sliderFloat("Temp", Edit_Props._temp, -1, 1);
                sliderFloat("Tint", Edit_Props._tint, -1, 1);
                sliderFloat("Exposure", Edit_Props._exposure, -5, 5);
                sliderFloat("Contrast", Edit_Props._contrast, .5f, 1.5f);
                sliderFloat("Saturation", Edit_Props._saturation, 0.0f, 2.0f);
                sliderFloat("Highlights", Edit_Props._high, 0, 2);
                sliderFloat("Shadows", Edit_Props._low, -1, 1);
                text("Highlights");
                sliderFloat("H Temp", Levels_Props._Htint, -1, 1);
                sliderFloat("S Temp", Levels_Props._Ltint, -1, 1);
                sliderFloat("H Saturation", Levels_Props._Hsaturation, 0.0f, 2.0f);
                sliderFloat("L Saturation", Levels_Props._Lsaturation, 0.0f, 2.0f);


            }
        end();
    }
}
