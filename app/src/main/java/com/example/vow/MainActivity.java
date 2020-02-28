package com.example.vow;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        TextView scoreView = findViewById(R.id.scores);
        TextView levelView = findViewById(R.id.level);
        TextView coinView = findViewById(R.id.coins);

        SharedPreferences sharedPreferences = getSharedPreferences("VOWGAME", MODE_PRIVATE);
        mLevel = sharedPreferences.getInt("currentLevel", 0);
        mScore = sharedPreferences.getInt("totalScore", 0);
        mCoins = sharedPreferences.getInt("totalCoin", 0);

        GameEvents gameEvents = ViewModelProviders.of(this).get(GameEvents.class);
        gameEvents.setCoins(mCoins);
        gameEvents.setScore(mScore);
        gameEvents.setLevels(mLevel);

        coinView.setText(String.valueOf(mCoins));
        levelView.setText(String.valueOf(mLevel));
        scoreView.setText(String.valueOf(mScore));
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
}
