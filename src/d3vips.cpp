#include "d3vips.h"
#include "gl.h"
#include "util.h"
#include "vips/vips8"
#include <cstddef>
#include <string>

using namespace vips;
void getImageData(TEXTURE_DATA &tex) {
  UTIL::log(UTIL::INFO, std::string("Loading file: ").append(tex.fn).c_str());

  VImage vi = VImage::new_from_file(tex.fn.c_str(), NULL);

  vi = vi.rot90();
  vi = vi.crop(0, 0, vi.width() - 1, vi.height());

  tex.width = vi.width();
  tex.height = vi.height();
  tex.channels = vi.bands();
  tex.ar = tex.width * 1.0f / tex.height * 1.0f;
  vi = vi.cast(VIPS_FORMAT_USHORT, VImage::option()->set("shift", true));

  VIPSBUFFERDATA vbd;

  vi.write_to_buffer(".raw", &vbd.buf, &vbd.len);

  tex.tex_id = D3GL::uploadTexture(vbd, tex);
  g_free(vbd.buf);
}
