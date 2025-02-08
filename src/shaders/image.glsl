#vert
#version 400 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;

out vec2 fragTexCoord;

void main() {
    fragTexCoord = texCoord;
    gl_Position = vec4(position, 1.0);
}
#endvert



#frag
#version 400 core

#include "BrightnessContrast_v3"


in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D tex;

void main() {
    vec4 col = texture(tex, fragTexCoord);
    fragColor = col;
}
#endfrag
