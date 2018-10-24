package me.schattgen.topnotch;

import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DpiHelper {
    private WindowManager windowManager;
    private DisplayMetrics metrics;

    public DpiHelper(WindowManager receievedWindowManager)
    {
        windowManager = receievedWindowManager;
        metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }

    public float getDisplayXdpi(){
        return metrics.xdpi <= 320 ? 300 : metrics.xdpi;
    }

    public float getDisplayYdpi(){
        return metrics.xdpi <= 320 ? 320 : metrics.xdpi;
    }
}
