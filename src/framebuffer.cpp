#include "framebuffer.h"

FrameBuffer::FrameBuffer() { this->created = false; };

void FrameBuffer::genFrameBuffer() {
  glGenFramebuffers(1, &fbo);
  glBindFramebuffer(GL_FRAMEBUFFER, fbo);
  glGenTextures(1, &fbid);
  glBindTexture(GL_TEXTURE_2D, fbid);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16F, 0, 0, 0, GL_RGB, GL_UNSIGNED_SHORT,
               NULL);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glBindTexture(GL_TEXTURE_2D, 0);
  glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                         fbid, 0);
  glBindFramebuffer(GL_FRAMEBUFFER, 0);
  this->created = true;
}
void FrameBuffer::size(int width, int height) {
  this->width = width;
  this->height = height;
  glBindTexture(GL_TEXTURE_2D, fbid);
  glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB16, width, height, 0, GL_RGB,
               GL_UNSIGNED_SHORT, NULL);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
  glBindTexture(GL_TEXTURE_2D, 0);
  glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D,
                         fbid, 0);
  glBindFramebuffer(GL_FRAMEBUFFER, 0);
}
void FrameBuffer::bind() {
  glBindFramebuffer(GL_FRAMEBUFFER, fbo);
  glViewport(0, 0, width, height);
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
}
void FrameBuffer::unbind() { glBindFramebuffer(GL_FRAMEBUFFER, 0); }
