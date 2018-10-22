package me.schattgen.topnotch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import mehdi.sakout.fancybuttons.FancyButton;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private FancyButton button;
    private Switch serviceSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);

        startFadeInAnimation();

        button = findViewById(R.id.btnCheckPermission);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPermission())
                {
                    getDrawOverlayPermission();
                }
                else
                {
                    serviceSwitch.setChecked(true);
                    launchNotchService();
                }
            }
        });
        button.setVisibility(hasPermission() ? View.GONE : View.VISIBLE);

        serviceSwitch = findViewById(R.id.swActivateNotch);
        serviceSwitch.setChecked(isNotchServiceRunning(NotchService.class, this));
    }

    private void launchNotchService() {
        Intent notchService = new Intent(this, NotchService.class);
        startService(notchService);
    }

    public boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }

        return true;
    }

    public void getDrawOverlayPermission() {

        if(!hasPermission())
        {
            Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(permissionIntent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        if (requestCode == REQUEST_CODE) {
            if(hasPermission()){
                serviceSwitch.setChecked(true);
                launchNotchService();
            }
            else {
                Toast.makeText(this, "Before you can activate the notch you need to grant permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNotchServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startFadeInAnimation() {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        imageView.startAnimation(startAnimation);
    }
}
