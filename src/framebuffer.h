#pragma once
#include "Params.h"

class FrameBuffer {
public:
  GLuint fbid;
  int width;
  int height;
  GLuint fbo;
  bool created;
  FrameBuffer();
  void genFrameBuffer();
  void size(int width, int height);
  void bind();
  void unbind();
};
