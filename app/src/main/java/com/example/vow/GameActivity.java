package com.example.vow;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vow.DataModel.GameEvents;
import com.example.vow.GameViews.GameView;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class GameActivity extends AppCompatActivity {

    private String TAG  = "GameEvents";

    private GameEvents gameEvents;
    private int score = 0,coin=0,level=0;
    private double pitch = 0;
    private String chrod="Chord A";
    private RadioGroup radioGroup;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);

        final RelativeLayout gameCanvas = findViewById(R.id.gameCanvas);
        gameCanvas.post(new Runnable() {
            @Override
            public void run() {
                GameView gameView = new GameView(GameActivity.this);
                gameCanvas.addView(gameView);
            }
        });

        gameEvents = ViewModelProviders.of(this).get(GameEvents.class);
        mic();
        radioGroup = findViewById(R.id.musicselector);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checked) {
                RadioButton radioButton = findViewById(checked);
                chrod = (String)radioButton.getText();
            }
        });

        final TextView scoreView = findViewById(R.id.score);
        gameEvents.getMove().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer val) {
                assert val != null;
                if(val<3)score++;
                scoreView.setText("SCORE "+score);
                if(score%50==0)gameEvents.setCoins(coin++);
            }
        });

        final TextView coinView = findViewById(R.id.coin);
        gameEvents.getCoins().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer val) {
                assert val != null;
                coinView.setText("COINS "+score);
            }
        });


        final TextView pitchView = findViewById(R.id.pitch);
        gameEvents.getPitch().observe(this, new Observer<Double>() {
            @Override
            public void onChanged(@Nullable Double pitch) {
                pitchView.setText("PITCH "+pitch);
            }
        });
    }

    public void mic() {
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult res, AudioEvent e) {
                final float pitchInHz = res.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processPitch(pitchInHz);
                    }
                });
            }
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(pitchProcessor);
        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }


    public void processPitch(float pitchInHz) {
        switch (chrod){
            case "Chord A":
                if (pitchInHz >= 110 && pitchInHz < 123.47) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord B":
                if (pitchInHz >= 123.47 && pitchInHz < 130.81) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord C":
                if (pitchInHz >= 130.81 && pitchInHz < 146.83) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord D":
                if (pitchInHz >= 146.83 && pitchInHz < 164.81) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord E":
                if (pitchInHz >= 164.81 && pitchInHz <= 174.61) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord F":
                if (pitchInHz >= 174.61 && pitchInHz < 185) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;
            case "Chord G":
                if (pitchInHz >= 185 && pitchInHz < 196) {
                    gameEvents.setMove(2);
                }else{
                    gameEvents.setMove(30);
                }
                break;

        }
        gameEvents.setPitch(pitchInHz);
    }

    public void showHideChords(View view) {
        setVisibility(radioGroup);
    }

    /* Map Function */
    private long map(long x, long in_min, long in_max, long out_min, long out_max){
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    /*
     * Custom Functions
     */
    /* Toggle Visibility of any View */
    public void setVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.INVISIBLE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void goHome(View view) {
        saveGame();
        startActivity(new Intent(GameActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGame();
    }

    private void saveGame(){
        sharedPreferences = getSharedPreferences("VOWGAME",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("data_0",level);
        editor.putInt("data_1",score);
        editor.putInt("data_2",coin);
        boolean status = editor.commit();
        if(status) {
            Log.d(TAG, "saved L:" + level + " \t C: " + coin + " \t S:" + score);
            editor.apply();
        }else {
            Log.d(TAG,"Failed");
        }
    }
}
