package com.example.vow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Permission Check
        int PERMISSIONS_ALL = 1;
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO
        };
        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this,permissions, PERMISSIONS_ALL);
        }
    }

    public void startGame(View view) {
        startActivity(new Intent(MainActivity.this,GameActivity.class));
        finish();
    }

    public static boolean hasPermissions(Context context, String... permissions){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null){
            for (String permission:permissions){
                if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
        }
        return true;
    }
}
