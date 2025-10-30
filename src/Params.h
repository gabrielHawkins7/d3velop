#pragma once
#include <filesystem>
#include <hello_imgui/docking_params.h>
#include <hello_imgui/hello_imgui_include_opengl.h>
#include <vector>
#include "glad/glad.h"

using PATH = std::filesystem::path;


struct Log{
    std::vector<std::string> log;
    bool log_scroll;
};

struct GUI_PARAMS{
    std::vector<HelloImGui::DockingSplit> docking_splits;
    std::vector<HelloImGui::DockableWindow> docking_windows;
};


struct GLCONFIG{
    std::string renderer;
    std::string version;
    std::string vendor;
    std::string shadingLangVersion;
    GLint       maxTextureSize;
    GLint       maxTextureUnits;
    GLint       maxVertexAttribs;
    GLint       maxUniformBufferBindings;
    GLint       numExtensions;
    std::vector<std::string> supportedExtensions;
};

struct TEXTURE_DATA{
    int width;
    int height;
    int channels;
    GLuint tex_id;
    PATH fn;
    float ar;
    bool loaded;
};

struct BASE_EDIT_CONTROLS{
    float _temp = 0.0f;
    float _tint = 0.0f;
    float _exposure = 0.0f;
    float _contrast = 1.0f;
    float _high = 1.0f;
    float _low = 0.0f;
    float _saturation = 1.0f;
};

struct LEVEL_EDIT_CONTROLS{
    float _Ltint = 0.0f;
    float _Htint = 0.0f;
    float _Lsaturation = 1.0f;
    float _Hsaturation = 1.0f;
};

struct APP_STATE{
    bool vips_enabled;
    TEXTURE_DATA cur_texture;
    BASE_EDIT_CONTROLS base_edit_controls;
    LEVEL_EDIT_CONTROLS level_edit_controls;

};
