#include "D3velop.h"
#include "Params.h"
#include "gl.h"
#include "tinyfiledialogs.h"
#include "util.h"
#include "vips/vips.h"
#include "vips/vips8"
#include <exception>
#include <filesystem>
#include <stdexcept>
#include <string>

// PUBLIC:
D3velop &D3velop::getInstance() {
  static D3velop instance;
  return instance;
}

void D3velop::preRun() {
  UTIL::log(UTIL::SUCCESS, "D3VELOP RUNNING");
  check_vips();
  get_glconfig();
  init_renderer();
  newImage("/home/gabe/dev/d3velop/assets/portra400.tiff");
}

void D3velop::run() {
  if (!running) {
    running = true;
    D3velop::preRun();
  }
}

void D3velop::newImage(PATH fn) {
  std::initializer_list<std::string> exts = {".png", ".jpg", ".tiff"};
  bool supported_ext = false;

  for (const auto &e : exts) {
    if (fn.extension().string() == e)
      supported_ext = true;
  }

  if (!supported_ext) {
    UTIL::log(UTIL::ERROR, std::string("Unsupported Extension: ")
                               .append(fn.extension().string())
                               .c_str());
    return;
  }

  if (app_state.cur_texture.loaded == true) {
    glDeleteTextures(1, &app_state.cur_texture.tex_id);
    app_state.cur_texture.loaded = false;
    app_state.cur_texture.fn = "";
    app_state.cur_texture.tex_id = 0;
  }
  app_state.cur_texture.fn = fn;
  try {
    getImageData(app_state.cur_texture);
  } catch (vips::VError e) {
    UTIL::log(UTIL::ERROR,
              std::string("UNABLE TO OPEN FILE : ").append(e.what()).c_str());
    return;
  }
  app_state.cur_texture.loaded = true;
}

void D3velop::newImageFromFile() {
  char *fn = tinyfd_openFileDialog(NULL, NULL, 0, NULL, NULL, 0);
  if (fn) {
    newImage(fn);
    return;
  }
}

// PRIVATE:
void D3velop::check_vips() {
  if (VIPS_INIT("D3VELOP")) {
    app_state.vips_enabled = false;
    UTIL::log(UTIL::ERROR, std::string("Unable to load vips...")
                               .append(vips_error_buffer_copy())
                               .c_str());
    vips_error_exit(NULL);
  } else {
    app_state.vips_enabled = true;
    UTIL::log(
        UTIL::SUCCESS,
        std::string("VIPS ENABLED : ").append(vips_version_string()).c_str());
  }
}

void D3velop::get_glconfig() { D3GL::getGlConfigData(glconfig); }

void D3velop::init_renderer() {
  try {
    renderer.fb.genFrameBuffer();
    renderer.compile_shaders();
  } catch (const std::runtime_error e) {

    UTIL::print(e.what());
  }
};

D3velop::D3velop() { std::cout << "D3velop Constructor Called\n"; }
D3velop::~D3velop() { std::cout << "D3velop Destructor Called\n"; }
