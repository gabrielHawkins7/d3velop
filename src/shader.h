#pragma once
#include "Params.h"
#include <cstddef>
#include <filesystem>
#include <fstream>
#include <string>

#include <string>
#include <unordered_map>

#include <vector>

struct SHADER_DATA {
  std::string vert_data;
  std::string frag_data;
  bool null;
};

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
struct shader_err : std::runtime_error {
  using std::runtime_error::runtime_error;
};

const std::unordered_map<std::string, std::string> shader_includes = {
    {"BrightnessContrast_v3", "vec3 brightnessContrast( vec3 v, float b, float "
                              "c ) { return ( v - 0.5 ) * c + 0.5 + b; }"},
    {"Exposure_v3",
     "vec3 exposure(vec3 v, float a) { return v * pow(2., a); }"}};

static std::vector<std::string> get_shader_include_keys() {
  std::vector<std::string> keys;
  for (const auto &pair : shader_includes) {
    keys.push_back(pair.first);
  }
  return keys;
}

static std::string parse_includes(std::string glsl) {
  size_t pos = 0;
  while ((pos = glsl.find("#include", pos)) != std::string::npos) {
    size_t startQuote = glsl.find('"', pos);
    size_t endQuote = glsl.find('"', startQuote + 1);
    std::string includeKey =
        glsl.substr(startQuote + 1, endQuote - startQuote - 1);

    if (shader_includes.find(includeKey) != shader_includes.end()) {
      glsl.replace(pos, endQuote - pos + 1, shader_includes.at(includeKey));
    } else {
      pos = endQuote + 1;
    }
  }
  return glsl;
}

static SHADER_DATA getShaderData(std::filesystem::path path) {
  int vert_pos;
  int frag_pos;
  std::ifstream file(path);
  std::string data((std::istreambuf_iterator<char>(file)),
                   std::istreambuf_iterator<char>());
  SHADER_DATA out_data;

  size_t vert_start = data.find("#vert");
  size_t vert_end = data.find("#endvert");
  size_t frag_start = data.find("#frag");
  size_t frag_end = data.find("#endfrag");

  if (vert_start != std::string::npos && vert_end != std::string::npos) {
    out_data.vert_data =
        data.substr(vert_start + 6, vert_end - (vert_start + 6));
  } else {
    out_data.vert_data = "";
  }

  if (frag_start != std::string::npos && frag_end != std::string::npos) {
    out_data.frag_data =
        data.substr(frag_start + 6, frag_end - (frag_start + 6));
    out_data.frag_data = parse_includes(out_data.frag_data);
  } else {
    out_data.frag_data = "";
  }

  return out_data;
}
