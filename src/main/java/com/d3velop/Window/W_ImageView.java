package com.d3velop.Window;

import com.d3velop.Controller.Data;
import com.d3velop.Controller.Props;

import static imgui.ImGui.*;


import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

public class W_ImageView {
    public static void view(){
        int flags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoSavedSettings;
        ImGuiViewport viewport = getMainViewport();
        setNextWindowPos(viewport.getWorkPosX(), viewport.getWorkPosY());
        int imgviewSize = (int) ((int) viewport.getSizeX() - (viewport.getSizeX() * .3));
        setNextWindowSize(imgviewSize, viewport.getWorkSizeY());
        
        begin("Image View", flags);
        if(Data.current_texture == null){
            float width = getWindowSizeX();
            float textWidth = calcTextSize("No Image Loaded").x;
            setCursorPosX((width - textWidth) * 0.5f);
            float height = getWindowSizeY();
            float textheight = calcTextSize("No Image Loaded").y;
            setCursorPosY((height - textheight) * 0.5f);
            text("No Image Loaded");
            end();
            return;
        }

        int width = Data.current_texture.width;
        int height = Data.current_texture.height;
        float ar = width *1.0f  / height * 1.0f;
        float zoom = Props._zoom;


        ImVec2 image_size = new ImVec2(imgviewSize * zoom, (imgviewSize / ar) * zoom);

        setCursorPosY((viewport.getWorkSizeY() / 2.0f) - (image_size.y / 2.0f));

        image(Data.current_texture.texture_id, image_size);

        end();
    }
}
