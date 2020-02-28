package com.example.vow.GameViews;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import com.example.vow.DataModel.GameEvents;
import com.example.vow.R;

import java.util.Random;

public class GameView extends View {
    private int speed = 2;
    private int[] posVerticle = new int[9];
    private Drawable road, car, background, tree, grass, cactus;
    private int bollspeed;
    private int degrees;
    private Random random = new Random();
    private int roadBoundV = 300;
    private int roadBoundH = 400;
    private int carBound = 200;
    private int[] treeX;
    private boolean[] visibility;
    private int coins = 0,coinX=0;

    GameEvents gameEvents;

    public GameView(Context context) {
        super(context);
        gameEvents = ViewModelProviders.of((FragmentActivity) context).get(GameEvents.class);
        for (int i = 0; i < posVerticle.length; i++) {
            posVerticle[i] = i * roadBoundV;
        }
        car = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_car, null);
        road = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_road, null);
        background = ResourcesCompat.getDrawable(getResources(),R.drawable.bgplain, null);
        tree = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_trees, null);
        grass = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_grass, null);
        cactus = ResourcesCompat.getDrawable(getResources(),R.drawable.ic_cactus, null);

        treeX = new int[posVerticle.length];
        visibility = new boolean[posVerticle.length];
        gameEvents.getMove().observe((LifecycleOwner) getContext(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                bollspeed = value;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* Drawing Vector Drawable on Canvas*/
        roadBoundH = 2*canvas.getWidth()/3;
        /*--draw backgroud---*/
        drawBackGround(canvas);
        /*--draw road---*/
        drawRoad(canvas);
        /*--draw roadside vegetation---*/
        drawVegetation(canvas);
        /*--draw car---*/

        int carX = (int) (bollspeed * Math.sin(Math.toRadians(degrees)));//rand.nextInt(75) * (rand .nextBoolean() ? -1 : 1);
        if(Math.abs(carX-coinX)<2)gameEvents.setCoins(coins+1); // add coins
        drawCar(canvas, carX);
        degrees += speed;
        invalidate();
    }

    // Draw Background
    private void drawBackGround(Canvas canvas) {
        background.setBounds(0, 0, getWidth(), getHeight());
        background.draw(canvas);
    }

    // Draw Vegetation
    private void drawVegetation(Canvas canvas) {
        for (int i = 0; i < posVerticle.length; i++) {
            if (posVerticle[i] < getHeight()) posVerticle[i] += speed;
            else {
                posVerticle[i] = -roadBoundV + speed;
                visibility[i] = random.nextBoolean();
                int numLeft = randomNumGen(0, getWidth() / 2);
                int numRight = randomNumGen(getWidth() / 2 + carBound/2, getWidth());
                Log.d("Number RL",numLeft +","+numRight);
                treeX[i] = (visibility[i] ? numLeft : numRight );
            }
            /*if (!visibility[i]) {
                tree.setBounds(treeX[i], posVerticle[i], treeX[i] + 50, posVerticle[i] + 50);
                tree.draw(canvas);
            } else {
                cactus.setBounds(treeX[i], posVerticle[i], treeX[i] + 40, posVerticle[i] + 40);
                cactus.draw(canvas);
                int dist = randomNumGen(50,-50);
                grass.setBounds(treeX[i] + dist, posVerticle[i], treeX[i] + dist + 40, posVerticle[i] + 40);
                grass.draw(canvas);
            }*/
        }
    }

    // Draw Road
    private void drawRoad(Canvas canvas) {
        for (int i = 0; i < posVerticle.length; i++) {
            int roadX = getWidth() / 2 - roadBoundH / 2;
            if (posVerticle[i] < getHeight()) posVerticle[i] += speed;
            else posVerticle[i] = -roadBoundV + speed;
            road.setBounds(roadX, posVerticle[i], roadX + roadBoundH, posVerticle[i] + roadBoundV);
            road.draw(canvas);
        }
    }

    // Draw Car
    private void drawCar(Canvas canvas, int pos) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.translate(8*pos-carBound/2, carBound);
        car.setBounds(0, 0, carBound, carBound*2);
        canvas.rotate((float) Math.cos(Math.toRadians(degrees)) * bollspeed );
        car.draw(canvas);
        canvas.restore();
    }

    private int randomNumGen(int min, int max) {
        int num = (int) (Math.random() * (max - min) + min);
        Log.d("Number", num + "");
        return num;
    }

    /*
     *  Display coins on the road
     *  Display them on random interval
     *  Make the collectible only when the card hit them with 0 degree
     */
    private void generateCoins(){

    }

}
