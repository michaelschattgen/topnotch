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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private FancyButton button;
    private Switch serviceSwitch;
    private ImageView notchImage;
    private TextView tvHeader;
    private TextView tvSubheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);

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

        serviceSwitch = findViewById(R.id.swActivateNotch);
        button.setVisibility(hasPermission() ? View.GONE : View.VISIBLE);

        tvHeader = findViewById(R.id.tvHeader);
        tvSubheader = findViewById(R.id.tvSubheader);
        if (hasPermission()) {
            serviceSwitch.setVisibility(View.VISIBLE);
            tvSubheader.setText("You have granted the right permissions to make TopNotch work on your device. Tap on the toggle below to activate the Pixel 3 XL notch.");
        }

        serviceSwitch.setChecked(isNotchServiceRunning(NotchService.class, this));
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    launchNotchService();
                }
                else
                {
                    stopNotchService();
                }
            }
        });

        notchImage = findViewById(R.id.imageView);
        if (!isNotchServiceRunning(NotchService.class, this))
        {
            startFadeInAnimation();
        }
    }

    private void stopNotchService() {
        Window window = getWindow();
        window.setStatusBarColor(Color.BLACK);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        Intent notchService = new Intent(this, NotchService.class);
        stopService(notchService);
    }

    private void launchNotchService() {
        notchImage.setVisibility(View.INVISIBLE);
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#f4f6f8"));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

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
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        Animation fastAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation_fast);

        tvHeader.startAnimation(fastAnimation);
        tvSubheader.startAnimation(fastAnimation);
        serviceSwitch.startAnimation(fastAnimation);

        notchImage.startAnimation(animation);
        notchImage.setVisibility(View.VISIBLE);
    }
}
