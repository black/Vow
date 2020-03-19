package com.example.vow;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vow.DataModel.GameEvents;
import com.example.vow.GameViews.GameView;
import com.example.vow.Progress.ProgressView;

public class ProgressActivity extends AppCompatActivity {

    private int mScore = 0,mCoins=0,mLevel=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        TextView scoreView = findViewById(R.id.scores);
     //   TextView levelView = findViewById(R.id.level);
        TextView coinView = findViewById(R.id.coins);

        SharedPreferences sharedPreferences = getSharedPreferences("VOWGAME", MODE_PRIVATE);
     //   mLevel = sharedPreferences.getInt("currentLevel", 0);
        mScore = sharedPreferences.getInt("totalScore", 0);
        mCoins = sharedPreferences.getInt("totalCoin", 0);

        GameEvents gameEvents = ViewModelProviders.of(this).get(GameEvents.class);
        gameEvents.setCoins(mCoins);
        gameEvents.setScore(mScore);
    //    gameEvents.setLevels(mLevel);

        coinView.setText(String.valueOf(mCoins));
       // levelView.setText(String.valueOf(mLevel));
        scoreView.setText(String.valueOf(mScore));

        final RelativeLayout progressCanvas = findViewById(R.id.progressCanvas);
        progressCanvas.post(new Runnable() {
            @Override
            public void run() {
                ProgressView progressView = new ProgressView(ProgressActivity.this);
                progressCanvas.addView(progressView);
            }
        });
    }

    public void goHome(View view) {
        startActivity(new Intent(ProgressActivity.this, MainActivity.class));
        finish();
    }
}
