package me.schattgen.topnotch;

import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DpiHelper {
    private WindowManager windowManager = null;

    public DpiHelper(WindowManager receievedWindowManager)
    {
        windowManager = receievedWindowManager;
    }

    public float getDefaultXPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float xdpi = metrics.xdpi;
        if(xdpi<=320){
            return 300;
        }
        return xdpi;
    }

    public float getDefaultYPPI(){
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        float ydpi = metrics.ydpi;
        if(ydpi <= 320){
            return 320;
        }
        return ydpi;
    }
}
