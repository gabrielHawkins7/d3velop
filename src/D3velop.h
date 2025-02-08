#pragma once
#include <hello_imgui/runner_params.h>
#include "Params.h"
#include "renderer.h"



class D3velop {
    public:
        D3velop(const D3velop&) = delete;
        D3velop& operator=(const D3velop&) = delete;
        static D3velop& getInstance();


        void preRun();
        void run();
        void newImage(PATH fn);

        void newImageFromFile();

        Renderer renderer;

        GUI_PARAMS gui_params;
        GLCONFIG glconfig;
        APP_STATE app_state;
        Log log;

    private:
        bool running = false;

        void check_vips();
        void get_glconfig();

        D3velop();
        ~D3velop();
};
