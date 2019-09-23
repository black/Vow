package com.example.vow;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vow.DataModel.GameEvents;

public class MainActivity extends AppCompatActivity {

    GameEvents gameEvents;
    private int score = 0,coin=0;
    private double pitch = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));

     /*   gameEvents = ViewModelProviders.of(this).get(GameEvents.class);

        Button button = findViewById(R.id.scoregen);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pitch =  Math.random()*100;
                if((int)pitch>50)score++;
                if((int)pitch>40 && (int)pitch<60) coin++;
                gameEvents.setPitch(pitch);
                gameEvents.setScore(score);
                gameEvents.setCoins(coin);
            }
        });


        final TextView pitchView = findViewById(R.id.pitch);
        final TextView scoreView = findViewById(R.id.score);
        final TextView coinView = findViewById(R.id.coin);

        gameEvents.getPitch().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double val) {
                assert val != null;
                pitchView.setText("PITCH:"+val.toString());
            }
        });

        gameEvents.getScore().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer val) {
                assert val != null;
                scoreView.setText("SCORE:"+val.toString());
            }
        });

        gameEvents.getCoins().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer val) {
                assert val != null;
                coinView.setText("SCORE:"+val.toString());
            }
        });
*/
      /*  RelativeLayout gameCanvas = findViewById(R.id.gameCanvas);
        GameView gameView = new GameView(this);
        gameCanvas.addView(gameView);*/
    }
}
