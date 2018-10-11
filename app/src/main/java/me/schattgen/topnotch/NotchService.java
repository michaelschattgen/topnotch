package me.schattgen.topnotch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class NotchService extends Service {

    private DpiHelper dpiHelper;
    private WindowManager windowManager;
    private View notchView;

    final double widthParam = 2.6;
    final double heightParam = 0.315;

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        dpiHelper = new DpiHelper(windowManager);

        addNotchView();
    }

    private void addNotchView() {
        int overlayType = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            overlayType = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        } else {
            overlayType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        notchView = View.inflate(getApplicationContext(), R.layout.notch, null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                (int)(widthParam* dpiHelper.getDefaultXPPI()),
                (int)(heightParam* dpiHelper.getDefaultYPPI()),
                overlayType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity =  Gravity.TOP | Gravity.START | Gravity.END;
        windowManager.addView(notchView, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(notchView != null)
        {
            windowManager.removeView(notchView);
            notchView = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
