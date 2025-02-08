#include "gui.h"
#include "util.h"
#include <imgui.h>


using namespace ImGui;


void GUI::begin(D3velop &d3velop){
    HelloImGui::DockingSplit splitMainMisc;
    splitMainMisc.initialDock = "MainDockSpace";
    splitMainMisc.newDock = "MiscSpace";
    splitMainMisc.direction = ImGuiDir_Down;
    splitMainMisc.ratio = 0.1f;

    HelloImGui::DockingSplit splitImage;
    splitImage.initialDock = "MainDockSpace";
    splitImage.newDock = "ImageSpace";
    splitImage.direction = ImGuiDir_Left;
    splitImage.ratio = .70f;
    splitImage.nodeFlags = ImGuiDockNodeFlags_AutoHideTabBar;

    std::vector<HelloImGui::DockingSplit> splits {splitMainMisc, splitImage};

    HelloImGui::DockableWindow imageWindow;
    imageWindow.label = "Image";
    imageWindow.dockSpaceName = "ImageSpace";
    imageWindow.GuiFunction = [&] { image_view(d3velop); };
    imageWindow.canBeClosed = false;


    HelloImGui::DockableWindow devWindow;
    devWindow.label = "Dev";
    devWindow.dockSpaceName = "MainDockSpace";
    devWindow.GuiFunction = [&] { dev_view(d3velop); };

    HelloImGui::DockableWindow controlsWindow;
    controlsWindow.label = "Controls";
    controlsWindow.dockSpaceName = "MainDockSpace";
    controlsWindow.GuiFunction = [&] { controls_view(d3velop); };

    HelloImGui::DockableWindow logsWindow;
    logsWindow.label = "Logs";
    logsWindow.dockSpaceName = "MiscSpace";
    logsWindow.GuiFunction = [&] { log_view(d3velop); };
    std::vector<HelloImGui::DockableWindow> windows {imageWindow, devWindow,logsWindow, controlsWindow };

    d3velop.gui_params.docking_splits = splits;
    d3velop.gui_params.docking_windows = windows;
}

void GUI::base_edit_controls_view(BASE_EDIT_CONTROLS &base_edit_controls){

    SliderFloat("Temp", &base_edit_controls._temp, -1.0f, 1.0f);
    SliderFloat("Tint", &base_edit_controls._tint, -1.0f, 1.0f);
    SliderFloat("Exposure", &base_edit_controls._exposure, -1.0f, 1.0f);
    SliderFloat("Contrast", &base_edit_controls._contrast, 0.0f, 2.0f);
    SliderFloat("Saturation", &base_edit_controls._saturation, 0.0f, 2.0f);
    SliderFloat("Highs", &base_edit_controls._high, 0.0f, 1.0f);
    SliderFloat("Lows", &base_edit_controls._low, 0.0f, 1.0f);


}

void GUI::controls_view(D3velop &d3velop){
    SeparatorText("Base Edit");
    base_edit_controls_view(d3velop.app_state.base_edit_controls);
}



void GUI::dev_view(D3velop &d3velop){
    if(!IsItemVisible()){
        return;
    }
    Text("Vips: %s", (d3velop.app_state.vips_enabled) ? "ENABLED": "DISABLED");

    if(Button("Test Image 1")){
        d3velop.newImage("/Users/gabe/Documents/DEV/C/D3velop/assets/img1.jpg");
    }
    if(Button("Test Image 2")){
        d3velop.newImage("/Users/gabe/Documents/DEV/C/D3velop/assets/img2.jpg");
    }

    if(Button("New From File")){
        d3velop.newImageFromFile();
    }

    if(CollapsingHeader("Renderer")){
        SeparatorText("FrameBuffer");
        if(d3velop.renderer.fb.created){
            Text("Width: %d", d3velop.renderer.fb.width);
            Text("Height: %d", d3velop.renderer.fb.height);
            Text("ID: %d", d3velop.renderer.fb.fbid);
        }else {
            Text("No Framebuffer Created!");
        }
        SeparatorText("Shader:");
        if(d3velop.renderer.image_shader != nullptr){
            Text("Name: %s", d3velop.renderer.image_shader->name.c_str());
        }
    }



    if(CollapsingHeader("Image Stats")){
        Text("filename: %s", d3velop.app_state.cur_texture.fn.filename().c_str());
        Text("width: %i",d3velop.app_state.cur_texture.width);
        Text("height: %i",d3velop.app_state.cur_texture.height);
        Text("channels: %i",d3velop.app_state.cur_texture.channels);
        Text("GLID: %i", d3velop.app_state.cur_texture.tex_id);
        Text("AR: %2f", d3velop.app_state.cur_texture.ar);
    }
    if(CollapsingHeader("OpenGL Config")){
        Text("renderer: %s", d3velop.glconfig.renderer.c_str());
        Text("version: %s", d3velop.glconfig.version.c_str());
        Text("vendor: %s", d3velop.glconfig.vendor.c_str());
        Text("shadingLangVersion: %s", d3velop.glconfig.shadingLangVersion.c_str());
        Text("maxTextureSize: %i", d3velop.glconfig.maxTextureSize);
        Text("maxTextureUnits: %i", d3velop.glconfig.maxTextureUnits);
        Text("maxVertexAttribs: %i", d3velop.glconfig.maxVertexAttribs);
        Text("maxUniformBufferBindings: %i", d3velop.glconfig.maxUniformBufferBindings);
        Text("numExtensions: %i", d3velop.glconfig.numExtensions);
        if(CollapsingHeader("Supported Extensnions")){
            for(std::string x : d3velop.glconfig.supportedExtensions){
                Text("%s",x.c_str());
            }
        }
    }
}
void GUI::log_view(D3velop &d3velop){
    if(!IsItemVisible()){
        return;
    }
    for(std::string x : d3velop.log.log){
        ImU32 col;
        if(x.find("[MESSAGE]") != std::string::npos){
            col = LOG_COL_MESSAGE;
        }
        if(x.find("[ERROR]") != std::string::npos){
            col = LOG_COL_ERROR;
        }
        if(x.find("[SUCCESS]") != std::string::npos){
            col = LOG_COL_SUCCESS;
        }
         if(x.find("[INFO]") != std::string::npos){
            col = LOG_COL_INFO;
        }
        const char* m = x.c_str();
        PushStyleColor(ImGuiCol_Text, col);
        Text("%s", m);
        PopStyleColor();
    }
    if(d3velop.log.log_scroll){
        ImGui::SetScrollHereY(1.0f);
        d3velop.log.log_scroll = false;
    }
}
void GUI::image_view(D3velop &d3velop){
    if(d3velop.app_state.cur_texture.loaded == false){
        Text("No Image Loaded");
        return;
    }

    ImVec2 winMax = GetContentRegionMax();
    float ar = d3velop.app_state.cur_texture.ar;
    ImVec2 imSize;
    imSize.x = winMax.x ;
    imSize.y = (winMax.x / ar);
    Image(d3velop.app_state.cur_texture.tex_id,imSize);
}
