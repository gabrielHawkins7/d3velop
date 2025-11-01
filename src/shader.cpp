#include "shader.h"

Shader::Shader(std::string name, PATH fn) {
  this->name = name;
  this->fn = fn;
  shader_data = getShaderData(fn);
}

GLuint Shader::compileShader(GLuint type, const std::string &source) {
  // creates an empty shader obj, ready to accept source-code and be compiled
  GLuint hShader = glCreateShader(type);

  // hands the shader source code to the shader object so that it can keep a
  // copy of it
  const char *src = source.c_str();
  glShaderSource(hShader, 1, &src, nullptr);

  // compiles whatever source code is contained in the shader object
  glCompileShader(hShader);

  // Error Handling: Check whether the shader has been compiled
  GLint result;
  glGetShaderiv(hShader, GL_COMPILE_STATUS,
                &result); // assigns result with compile operation status
  if (result == GL_FALSE) {
    int length;
    glGetShaderiv(hShader, GL_INFO_LOG_LENGTH,
                  &length); // assigns length with length of information log
    char *infoLog = (char *)alloca(
        length * sizeof(char)); // allocate on stack frame of caller
    glGetShaderInfoLog(
        hShader, length, &length,
        infoLog); // returns the information log for a shader object
    throw shader_err(
        std::string("Failed to compile shader ")
            .append(name)
            .append(" type : ")
            .append((type == GL_VERTEX_SHADER) ? "vertex " : "fragment \n")
            .append(infoLog));
    glDeleteShader(hShader);
    return static_cast<GLuint>(0);
  }
  return hShader;
}

void Shader::createShader() {
  // compile the two shaders given as string reference
  GLuint vs = compileShader(GL_VERTEX_SHADER, shader_data.vert_data);
  GLuint fs = compileShader(GL_FRAGMENT_SHADER, shader_data.frag_data);
  if (vs == 0 || fs == 0) {
    return;
  }

  // create a container for the program-object to which you can attach shader
  // objects
  program = glCreateProgram();

  // attaches the shader objects to the program object
  glAttachShader(program, vs);
  glAttachShader(program, fs);

  // links all the shader objects, that are attached to a program object,
  // together
  glLinkProgram(program);

  // Error Handling: Check whether program has been linked successfully
  GLint result;
  glGetShaderiv(program, GL_COMPILE_STATUS,
                &result); // assigns result with compile operation status
  if (result == GL_FALSE) {
    int length;
    glGetShaderiv(program, GL_INFO_LOG_LENGTH,
                  &length); // assigns length with length of information log
    char *infoLog = (char *)alloca(
        length * sizeof(char)); // allocate on stack frame of caller
    glGetShaderInfoLog(
        program, length, &length,
        infoLog); // returns the information log for a shader object
    // std::cout << "Failed to link vertex and fragment shader!"
    // 	<< std::endl;
    // std::cout << infoLog << std::endl;

    throw shader_err(
        std::string("Failed to link vertex and fragment shader! \n")
            .append(infoLog));

    glDeleteProgram(program);
    return;
  }

  glValidateProgram(program);

  // deletes intermediate objects
  glDeleteShader(vs);
  glDeleteShader(fs);

  // activate the program into the state machine of opengl
  // glUseProgram(program);
}
