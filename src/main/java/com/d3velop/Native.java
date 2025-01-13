package com.d3velop;

import java.nio.file.Paths;

import app.photofox.vipsffm.VipsHelper;

public class Native {
    public static OS OS;
    
    
        Native(){
            String osName = System.getProperty("os.name");
            if(osName.equals("Mac OS X")){
                Native.OS = com.d3velop.OS.OSX;
                Controller.logInfo("OS : " + osName);
            }
            Vips_Lib.init();
        }
    
        public class Vips_Lib {
            public static Boolean vips_loaded = false;
            public static boolean vips_enabled = false;
            public static String vips_path = "";
            public static String glib_path = "";
            public static String gobject_path = "";
            public static String version = "";
    
            
            public static void init(){
                if(OS == com.d3velop.OS.OSX){
                //Vips_Lib.vips_path = "/Users/gabe/Documents/DEV/Java/gluitest/src/main/resources/OSX/LibVPS/lib/libvips.dylib";
                Vips_Lib.vips_path = Paths.get(Native.class.getResource("/OSX/LibVPS/lib/libvips.dylib").getPath()).toAbsolutePath().toString();
                Vips_Lib.glib_path = Paths.get(Native.class.getResource("/OSX/GLib/libglib-2.0.dylib").getPath()).toAbsolutePath().toString();
                Vips_Lib.gobject_path = Paths.get(Native.class.getResource("/OSX/GLib/libgobject-2.0.dylib").getPath()).toAbsolutePath().toString();

                vips_loaded = true;
                Controller.logInfo("VIPS Loaded");

            }else{
                vips_loaded = false;
                Controller.logError("Unable To Load VIPS");

                return;
            }

            System.setProperty("vipsffm.libpath.vips.override", Vips_Lib.vips_path);
            System.setProperty("vipsffm.libpath.glib.override", Vips_Lib.glib_path);
            System.setProperty("vipsffm.libpath.gobject.override", Vips_Lib.gobject_path);

            Vips_Lib.version = VipsHelper.version_string();
            if (!version.isEmpty()) {
                Vips_Lib.vips_enabled = true;
                Controller.Props._vipsready = true;
                Controller.logInfo("VIPS Enabled  v." + version);
            }else{
                Controller.Props._vipsready = false;
                Vips_Lib.vips_enabled = false;
                Controller.logError("Can't connect to VIPS");

            }


        }
    }


}

enum OS{
    OSX,
    WIN,
    LINUX
}
