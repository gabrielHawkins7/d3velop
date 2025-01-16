#version 400 core

in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D tex;
uniform float _ev;

void main() {
    vec4 col = texture(tex, fragTexCoord) * _ev;
    
    vec3 rgb = vec3(col.r, col.g, col.b);

    rgb = pow(rgb, vec3(2.2));

    float gain = exp2(_ev);
    rgb *= gain;
    rgb = rgb / (rgb + 1.0);
    // post-scale so white stays white
    rgb *= (gain + 1.0) / gain;

    float minRGB = min(min(rgb.r, rgb.g), rgb.b);
    float maxRGB = max(max(rgb.r, rgb.g), rgb.b);
    if ((maxRGB > 1.0) && (minRGB < maxRGB)) {
        rgb = mix(rgb, vec3(minRGB) + (rgb - vec3(minRGB)) * vec3((1.0 - minRGB) / (maxRGB - minRGB)), 0.5);
    }

    rgb = pow(rgb, vec3(1.0 / 2.2));

    fragColor = vec4(rgb, 1.0);

}