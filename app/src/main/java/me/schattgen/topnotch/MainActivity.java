package me.schattgen.topnotch;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!hasPermission())
        {
            getDrawOverlayPermission();
        } else {

        }

        button = findViewById(R.id.buttonNotch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNotchService();
            }
        });
    }

    private void launchNotchService() {
        Intent notchService = new Intent(this, NotchService.class);
        startService(notchService);

        finish();
    }

    public boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                return false;
            }

            return true;
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
                launchNotchService();
            }
            else {
                // No permission
            }
        }
    }
}
