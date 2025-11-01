#include "gl.h"
#include "util.h"

int D3GL::uploadTexture(VIPSBUFFERDATA vbd, TEXTURE_DATA tex) {
  GLuint textureID;
  glGenTextures(1, &textureID);
  glBindTexture(GL_TEXTURE_2D, textureID);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

  int internal_format = (tex.channels == 3) ? GL_RGB16 : GL_RGBA16;
  int pixel_format = (tex.channels == 3) ? GL_RGB : GL_RGBA;

  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16, tex.width, tex.height, 0, GL_RGB,
               GL_UNSIGNED_SHORT, vbd.buf);
  return textureID;
};

void D3GL::getGlConfigData(GLCONFIG &gl_config) {
  gl_config.renderer = reinterpret_cast<const char *>(glGetString(GL_RENDERER));
  gl_config.version = reinterpret_cast<const char *>(glGetString(GL_VERSION));
  gl_config.vendor = reinterpret_cast<const char *>(glGetString(GL_VENDOR));
  gl_config.shadingLangVersion =
      reinterpret_cast<const char *>(glGetString(GL_SHADING_LANGUAGE_VERSION));
  // Query maximum texture size
  GLint maxTextureSize;
  glGetIntegerv(GL_MAX_TEXTURE_SIZE, &maxTextureSize);
  // Query maximum combined texture units
  GLint maxTextureUnits;
  glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, &maxTextureUnits);
  // Query maximum vertex attributes
  GLint maxVertexAttribs;
  glGetIntegerv(GL_MAX_VERTEX_ATTRIBS, &maxVertexAttribs);
  // Query maximum uniform buffer bindings
  GLint maxUniformBufferBindings;
  glGetIntegerv(GL_MAX_UNIFORM_BUFFER_BINDINGS, &maxUniformBufferBindings);
  // Check if certain extensions are supported
  GLint numExtensions;
  glGetIntegerv(GL_NUM_EXTENSIONS, &numExtensions);
  for (GLint i = 0; i < numExtensions; ++i) {
    gl_config.supportedExtensions.push_back(
        reinterpret_cast<const char *>(glGetStringi(GL_EXTENSIONS, i)));
  }
}
