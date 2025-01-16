#version 400 core

in vec2 fragTexCoord;
out vec4 fragColor;

uniform sampler2D tex;

uniform float _Ltint;
uniform float _Htint;
uniform float _Lsaturation;
uniform float _Hsaturation;



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

vec3 wbLevels(in vec3 rgb, in float Ltint, in float Htint) {
    float luminance = dot(rgb, vec3(0.299, 0.587, 0.114));
    
    float adjustment = 1.0;
     // Shadows
    if (luminance < 0.3) {
        adjustment = Ltint;
    } 
    // Highlights
    else if (luminance > 0.7) {
        adjustment = Htint;
    } 
    // Midtones
    else {
        float shadowWeight = (0.7 - luminance) / (0.7 - 0.3);
        float highlightWeight = (luminance - 0.3) / (0.7 - 0.3);
        adjustment = shadowWeight * Ltint + highlightWeight * Htint;
    }
    // Check if either Ltint or Htint is non-zero
    if (Ltint != 0.0 || Htint != 0.0) {
        return whiteBalance(rgb, 1.0, adjustment);
    }
    
    // If both are zero, return the original RGB without adjustment
    return rgb;
}


void main() {
    vec4 col = texture(tex, fragTexCoord);
    col.rgb = wbLevels(col.rgb, _Ltint, _Htint);
    fragColor = col;
}