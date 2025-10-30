#pragma once
#include "framebuffer.h"
#include "shader.h"
#include <vector>


class Renderer{
    public:
        FrameBuffer fb;
        std::vector<Shader> shader_list;
        Shader* image_shader = nullptr;

        Renderer();
        void compile_shaders();
    private:
};
