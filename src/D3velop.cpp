#include "D3velop.h"

//PUBLIC:
D3velop& D3velop::getInstance() {
    static D3velop instance;
    return instance;
}

HelloImGui::RunnerParams D3velop::getRunParams(){
    return this->runParams;
}

//PRIVATE:
D3velop::D3velop() {
    std::cout << "D3velop Constructor Called\n";
    HelloImGui::SimpleRunnerParams params;
    params.windowTitle = "D3VELOP";
    params.windowSize[0] = 800;
    params.windowSize[1] = 600;
    params.enableIdling = true;
    params.guiFunction = guiFunction;

}
D3velop::~D3velop() {
    std::cout << "D3velop Destructor Called\n";
}
