#pragma once
#include "Params.h"
#include "shaders/shader_util.h"
#include <string>

static float vertices[] = {
    -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, // Bottom left
    1.0f,  -1.0f, 0.0f, 1.0f, 0.0f, // Bottom right
    -1.0f, 1.0f,  0.0f, 0.0f, 1.0f, // Top left
    1.0f,  1.0f,  0.0f, 1.0f, 1.0f  // Top right
};

class Shader {
public:
  std::string name;
  PATH fn;
  SHADER_DATA shader_data;
  bool compiled;
  int program;

  Shader(std::string name, PATH fn);

  GLuint compileShader(GLuint type, const std::string &source);
  void createShader();
  void render();
  void bindData();

private:
  GLuint vao;
};
