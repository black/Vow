package com.example.vow;

import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vow.DataModel.GameEvents;

public class MainActivity extends AppCompatActivity {

    private String TAG  = "GameEvents";
    private int mScore = 0,mCoins=0,mLevel=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Permission Check
        int PERMISSIONS_ALL = 1;
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
        };

        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_ALL);
        }

        TextView levelView = findViewById(R.id.level);
        TextView coinView = findViewById(R.id.coins);

        SharedPreferences sharedPreferences = getSharedPreferences("VOWGAME", MODE_PRIVATE);
        mLevel = sharedPreferences.getInt("currentLevel", 0);
        mCoins = sharedPreferences.getInt("totalCoin", 0);

        GameEvents gameEvents = ViewModelProviders.of(this).get(GameEvents.class);
        gameEvents.setCoins(mCoins);
        gameEvents.setLevels(mLevel);

        coinView.setText(String.valueOf("COINS "+mCoins));
        levelView.setText(String.valueOf("LEVEL "+mLevel));
    }

    public void startGame(View view) {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
        finish();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void showGameProgress(View view) {
        startActivity(new Intent(MainActivity.this, ProgressActivity.class));
        finish();
    }

    public void quitGame(View view) {
        finish();
    }
}
