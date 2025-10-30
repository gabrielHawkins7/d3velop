#pragma once
#include "Params.h"
#include "d3vips.h"

namespace D3GL {
// Uploads texture data from vips an uploads it to the gpu
int uploadTexture(VIPSBUFFERDATA vbd, TEXTURE_DATA tex);

// Gets the current OpenGL configuration data
void getGlConfigData(GLCONFIG &gl_config);
} // namespace D3GL
