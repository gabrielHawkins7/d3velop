
#include "D3velop.h"
#include "Params.h"
#include "gui.h"
#include "hello_imgui/hello_imgui.h"
#include "hello_imgui/hello_imgui_assets.h"
#include <iostream>
#include <stdio.h>
#include <string>
#include <vector>

int main() {

  D3velop &d3velop = D3velop::getInstance();

  auto guiFunction = [&d3velop]() {
    ImGui::GetStyle() =
        ImGuiTheme::ThemeToStyle(ImGuiTheme::ImGuiTheme_DarculaDarker);
    d3velop.run();
  };
  HelloImGui::SetAssetsFolder("assets/");

  HelloImGui::SimpleRunnerParams params;
  params.windowTitle = "D3VELOP";
  params.windowSize[0] = 800;
  params.windowSize[1] = 600;
  params.enableIdling = true;
  params.guiFunction = guiFunction;

  HelloImGui::RunnerParams runParams = params.ToRunnerParams();
  runParams.appWindowParams.windowGeometry.positionMode =
      HelloImGui::WindowPositionMode::MonitorCenter;
  runParams.imGuiWindowParams.defaultImGuiWindowType =
      HelloImGui::DefaultImGuiWindowType::ProvideFullScreenDockSpace;
  runParams.imGuiWindowParams.showMenuBar = true;

  GUI::begin(d3velop);
  runParams.dockingParams.dockingSplits = d3velop.gui_params.docking_splits;
  runParams.dockingParams.dockableWindows = d3velop.gui_params.docking_windows;
  runParams.dockingParams.layoutCondition =
      HelloImGui::DockingLayoutCondition::ApplicationStart;

  auto fontFunction = [&d3velop]() {
    HelloImGui::FontLoadingParams fontParams;
    fontParams.insideAssets = false;
    HelloImGui::LoadFontDpiResponsive(
        HelloImGui::AssetFileFullPath("Roboto-Regular.ttf", true), 18.0f,
        fontParams);
    // HelloImGui::LoadFontDpiResponsive("../assets/Roboto-Regular.ttf", 14.0f,
    // fontParams);
  };

  runParams.callbacks.LoadAdditionalFonts = fontFunction;

  HelloImGui::Run(runParams);

  return 0;
}
