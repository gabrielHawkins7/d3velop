#version 400 core

in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D tex;

uniform float _temp;
uniform float _tint;
uniform float _exposure;
uniform float _contrast;
uniform float _high;
uniform float _low;
uniform float _saturation;




const mat3 RGB2YIQ = mat3(
    0.300,  0.5900,  0.1100, 
    0.599, -0.2773, -0.3217, 
    0.213, -0.5251,  0.3121);
vec3 rgb2yiq(const in vec3 rgb) { return RGB2YIQ * rgb; }

const mat3 YIQ2RGB = mat3(  
    1.0,  0.9469,  0.6235, 
    1.0, -0.2747, -0.6357, 
    1.0, -1.1085,  1.7020 );
vec3 yiq2rgb(const in vec3 yiq) { return YIQ2RGB * yiq; }


vec3 whiteBalance(in vec3 rgb, in float temp, in float tint) {
// Get the CIE xy chromaticity of the reference white point.
    // Note: 0.31271 = x value on the D65 white point
    float x = 0.31271 - temp * (temp < 0.0 ? 0.1 : 0.05);
    float standardIlluminantY = 2.87 * x - 3.0 * x * x - 0.27509507;
    float y = standardIlluminantY + tint * 0.05;

    // CIExyToLMS
    float Y = 1.0;
    float X = Y * x / y;
    float Z = Y * (1.0 - x - y) / y;
    float L = 0.7328 * X + 0.4296 * Y - 0.1624 * Z;
    float M = -0.7036 * X + 1.6975 * Y + 0.0061 * Z;
    float S = 0.0030 * X + 0.0136 * Y + 0.9834 * Z;

    // Calculate the coefficients in the LMS space.
    const vec3 w = vec3(0.949237, 1.03542, 1.08728); // D65 white poin
    vec3 balance = w/vec3(L, M, S);

    // TODO: use our own rgb to lms to rgb
    const mat3 lin2lms_mat = mat3(
        3.90405e-1, 5.49941e-1, 8.92632e-3,
        7.08416e-2, 9.63172e-1, 1.35775e-3,
        2.31082e-2, 1.28021e-1, 9.36245e-1
    );

    const mat3 lms2lin_mat = mat3(
        2.85847e+0, -1.62879e+0, -2.48910e-2,
        -2.10182e-1,  1.15820e+0,  3.24281e-4,
        -4.18120e-2, -1.18169e-1,  1.06867e+0
    );

    vec3 lms = lin2lms_mat * rgb;
    lms *= balance;
    return lms2lin_mat * lms;
}





vec3 exposure(vec3 v, float a) { return v * pow(2., a); }
vec3 contrast(in vec3 v, in float a) { return (v - 0.5 ) * a + 0.5; }

vec3 levelsOutputRange(in vec3 v, in vec3 oMin, in vec3 oMax) { return mix(oMin, oMax, v); }
vec3 levelsOutputRange(in vec3 v, in float oMin, in float oMax) { return levelsOutputRange(v, vec3(oMin), vec3(oMax)); }


vec3 highLow(vec3 color, float l, float h){
    return (color + vec3(l)) * h;
}

vec3 saturation(vec3 color, float s){
    // Algorithm from Chapter 16 of OpenGL Shading Language
    const vec3 W = vec3(0.2125, 0.7154, 0.0721);
    vec3 intensity = vec3(dot(color, W));
    return mix(intensity, color, s);
}





void main() {
    vec4 col = texture(tex, fragTexCoord);
    col.rgb = exposure(col.rgb, _exposure);
    col.rgb = contrast(col.rgb, _contrast);
    col.rgb = whiteBalance(col.rgb, _temp, _tint);
    col.rgb = highLow(col.rgb, _low, _high);
	col.rgb = saturation(col.rgb, _saturation);
    col.rgb = levelsOutputRange(col.rgb, 0.0, 1.0);

    
    fragColor = col;
}