#include "renderer.h"
#include "hello_imgui/hello_imgui_assets.h"

Renderer::Renderer() {}

void Renderer::compile_shaders() {
  if (image_shader == nullptr) {
    image_shader = new Shader(
        "Image Shader", HelloImGui::assetFileFullPath("image.glsl", false));
    image_shader->createShader();
  }
  for (auto &x : shader_list) {
    x.createShader();
  }
}
