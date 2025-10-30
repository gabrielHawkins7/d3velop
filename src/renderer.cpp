#include "renderer.h"

Renderer::Renderer(){

}

void Renderer::compile_shaders(){
    if(image_shader == nullptr){
        image_shader = new Shader("Image Shader","/home/gabe/dev/test/htest/src/shaders/image.glsl");
        image_shader->createShader();
    }
    for(auto& x : shader_list){
        x.createShader();
    }
}
