#version 400 core

in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D tex;

void main() {
    vec4 col = texture(tex, fragTexCoord);
    fragColor = col;
}