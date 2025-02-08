#include "renderer.h"

Renderer::Renderer(){

}

void Renderer::compile_shaders(){
    if(image_shader == nullptr){
        image_shader = new Shader("Image Shader","/Users/gabe/Documents/DEV/C/D3velop/src/shaders/image.glsl");
        image_shader->createShader();
    }
    for(auto& x : shader_list){
        x.createShader();
    }
}
