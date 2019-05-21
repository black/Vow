package com.example.vow.DataModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class GameEvents extends ViewModel {

    private MutableLiveData<Integer> score;
    private MutableLiveData<Integer> coins;
    private MutableLiveData<Double> pitch;

    public GameEvents(){
        score = new MutableLiveData<>();
        coins = new MutableLiveData<>();
        pitch = new MutableLiveData<>();
    }

    // game score
    public void setScore(int score){
        this.score.setValue(score);
    }
    public LiveData<Integer> getScore(){
        return score;
    }

    //pitch
    public void setPitch(double pitch){
        this.pitch.setValue(pitch);
    }
    public LiveData<Double> getPitch(){
        return pitch;
    }

    //coins
    public void setCoins(int coin){
        this.coins.setValue(coin);
    }
    public LiveData<Integer> getCoins(){
        return coins;
    }
}
