package com.example.vow.DataModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameEvents extends ViewModel {

    private MutableLiveData<Integer> score;
    private MutableLiveData<Integer> coins;
    private MutableLiveData<Double> pitch;
    private MutableLiveData<Integer> move;
    private MutableLiveData<Integer> levels;

    public GameEvents(){
        score = new MutableLiveData<>();
        coins = new MutableLiveData<>();
        levels = new MutableLiveData<>();
        pitch = new MutableLiveData<>();
        move = new MutableLiveData<>();
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

    //coins
    public void setLevels(int level){
        this.levels.setValue(level);
    }
    public LiveData<Integer> getLevels(){
        return levels;
    }

    //move
    public void setMove(int move){
        this.move.setValue(move);
    }
    public LiveData<Integer> getMove(){
        return move;
    }
}
