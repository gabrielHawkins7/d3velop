#pragma once
#include "Params.h"

struct VIPSBUFFERDATA {
  void *buf;
  size_t len;
};

/*
Reads the pixels from the image and sends that data to opengl
*/
void getImageData(TEXTURE_DATA &tex);
