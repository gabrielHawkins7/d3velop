#pragma once
#include <hello_imgui/runner_params.h>
#include "Params.h"
#include <iostream>



class D3velop {
    public:
        D3velop(const D3velop&) = delete;
        D3velop& operator=(const D3velop&) = delete;
        static D3velop& getInstance();




        HelloImGui::RunnerParams getRunParams();



    private:
        HelloImGui::RunnerParams runParams;
        bool running = false;
        D3velop();
        ~D3velop();
};
