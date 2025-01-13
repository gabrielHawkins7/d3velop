package com.d3velop;


import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;
import org.lwjgl.util.nfd.*;

import com.d3velop.Controller.Data;

import java.io.File;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeCocoa.*;
import static org.lwjgl.glfw.GLFWNativeWin32.*;
import static org.lwjgl.glfw.GLFWNativeX11.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.util.nfd.NativeFileDialog.*;


public class NFD {



    public static File openFile(){
        int  handleType;
        long handleWindow;
        switch (Platform.get()) {
            case FREEBSD:
            case LINUX:
                handleType = NFD_WINDOW_HANDLE_TYPE_X11;
                handleWindow = glfwGetX11Window(Data.glfw_win);
                break;
            case MACOSX:
                handleType = NFD_WINDOW_HANDLE_TYPE_COCOA;
                handleWindow = glfwGetCocoaWindow(Data.glfw_win);
                break;
            case WINDOWS:
                handleType = NFD_WINDOW_HANDLE_TYPE_WINDOWS;
                handleWindow = glfwGetWin32Window(Data.glfw_win);
                break;
            default:
                handleType = NFD_WINDOW_HANDLE_TYPE_UNSET;
                handleWindow = NULL;
        }

        return openSingle(handleType, handleWindow);
    }

    private static File openSingle(int handleType, long handleWindow) {
        try (MemoryStack stack = stackPush()) {
            NFDFilterItem.Buffer filters = NFDFilterItem.malloc(1);
            filters.get(0)
                .name(stack.UTF8("Images"))
                .spec(stack.UTF8("png,jpg,tif,tiff"));
            PointerBuffer pp = stack.mallocPointer(1);
            return checkResult(
                NFD_OpenDialog_With(pp, NFDOpenDialogArgs.calloc(stack)
                    .filterList(filters)
                    .parentWindow(it -> it
                        .type(handleType)
                        .handle(handleWindow))),
                pp
            );
        }
    }
    
    private static File checkResult(int result, PointerBuffer path) {
        switch (result) {
            case NFD_OKAY:
                File o =  new File(path.getStringUTF8(0));
                NFD_FreePath(path.get(0));
                return o;
            case NFD_CANCEL:
                Controller.logInfo("User pressed cancel.");
                return null;
            default: // NFD_ERROR
                Controller.logError("Error: " + NFD_GetError());
                return null;
        }
    }
}
