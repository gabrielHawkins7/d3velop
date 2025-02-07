cmake_minimum_required(VERSION 3.31.5)
project(d3velop VERSION 0.1.0 LANGUAGES C CXX)
set(CMAKE_TOOLCHAIN_FILE /Library/Dev/vcpkg/scripts/buildsystems/vcpkg.cmake CACHE FILEPATH "Path to toolchain")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)

find_package(OpenGL REQUIRED)
find_package(hello-imgui CONFIG REQUIRED)
find_package(glfw3 CONFIG REQUIRED)

include_directories(
    external/libvips/include
    external/libvips/lib/glib-2.0/include
    external/libvips/include/glib-2.0
)
find_library(VIPS NAMES vips.42 PATHS external/libvips/lib)

find_library(VIPSCPP NAMES vips-cpp.42 PATHS external/libvips/lib)


add_executable(d3velop
    src/main.cpp
    src/D3velop.cpp
)

add_custom_command(
    TARGET d3velop
    POST_BUILD
    COMMAND install_name_tool -change @loader_path/libvips.42.dylib @loader_path/../external/libvips/lib/libvips.42.dylib $<TARGET_FILE:d3velop>
    COMMAND install_name_tool -change @loader_path/libvips-cpp.42.dylib @loader_path/../external/libvips/lib/libvips-cpp.42.dylib $<TARGET_FILE:d3velop>

)

include_directories(build/vcpkg/vcpkg_installed/x64-osx/include)
target_link_libraries(d3velop ${OPENGL_LIBRARIES} glfw hello-imgui::hello_imgui ${VIPS} ${VIPSCPP})
