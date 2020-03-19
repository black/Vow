package com.example.vow.GameViews;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;

import com.example.vow.DataModel.GameEvents;
import com.example.vow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private int speed = 10;
    private int[] posVerticle = new int[9];
    private Drawable road, car, coin, background, tree, grass, cactus;
    private int bollspeed = 500;
    private int degrees;
    private Random random = new Random();
    private int roadBoundV = 300;
    private int roadBoundH = 400;
    private int carBound = 200;
    private int[] treeX;
    private boolean[] visibility;
    private int coinst = 0, coinX = 0;
    private List<Coins> coins = new ArrayList<>();

    GameEvents gameEvents;
    private int carX,carY;

    public GameView(Context context) {
        super(context);
        gameEvents = ViewModelProviders.of((FragmentActivity) context).get(GameEvents.class);
        for (int i = 0; i < posVerticle.length; i++) {
            posVerticle[i] = i * roadBoundV;
        }
        car = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_car, null);
        coin = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_coin, null);
        road = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_road, null);
        background = ResourcesCompat.getDrawable(getResources(), R.drawable.bgplain, null);
        tree = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_trees, null);
        grass = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_grass, null);
        cactus = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_cactus, null);

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
        /*--draw backgroud---*/
        drawBackGround(canvas);

        /*--draw road---*/
        drawRoad(canvas);


        /*--draw roadside vegetation---*/
       // drawVegetation(canvas);

        /*--draw car---*/
       // int carX = (int) (bollspeed * Math.sin(Math.toRadians(degrees)));//rand.nextInt(75) * (rand .nextBoolean() ? -1 : 1);
        //if(Math.abs(carX-coinX)<2)gameEvents.setCoins(coin+1); // add coins
       // drawCar(canvas, carX);
        degrees += speed;

        /*-- add coins----*/
        addCoins();
        /*--drwa coins---*/
        int displacement = (int) (bollspeed * Math.sin(Math.toRadians(degrees)));
        carX = displacement +getWidth()/2;//rand.nextInt(75) * (rand .nextBoolean() ? -1 : 1);
        carY = getHeight()/2;

        displayCoins(canvas);
        drawRect(canvas,carX,carY,100,200);

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
                int numRight = randomNumGen(getWidth() / 2 + carBound / 2, getWidth());
                Log.d("Number RL", numLeft + "," + numRight);
                treeX[i] = (visibility[i] ? numLeft : numRight);
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


    private int randomNumGen(int min, int max) {
        int num = (int) (Math.random() * (max - min) + min);
        Log.d("Number", num + "");
        return num;
    }


    // Draw Car
    private void drawCar(Canvas canvas, int pos) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.translate(8 * pos - carBound / 2, carBound);
        car.setBounds(0, 0, carBound, carBound * 2);
        canvas.rotate((float) Math.cos(Math.toRadians(degrees)) * bollspeed);
        car.draw(canvas);
        canvas.restore();
    }

    // Draw coin
    private void displayCoins(Canvas canvas) {
        for (int i = 0; i < coins.size(); i++) {
            Coins c = coins.get(i);
            if (dist(c.x,c.y,carX,carY)<100){
                coins.remove(i);
                Log.d("Close","TRUE COLLISION");
            }else if(c.y > getHeight()){
                coins.remove(i);
                Log.d("Close","TRUE END");
            }
            c.y += speed;
            Log.d("Coin",c.y + " \t "+carY);
        }

        for(int i = 0; i < coins.size(); i++) {
            Coins c = coins.get(i);
            drawCoins(canvas, c.x, c.y);
        }
    }

    // add Coins
    private void addCoins() {
       /* Coins coin = coins.get(0);
        if (coin.x==getHeight()/2) coins.add(new Coins(getWidth() / 2, 0));*/
    }

    // draw Coins
    private void drawCoins(Canvas canvas, int x, int y) {
        int r = 50;
        coin.setBounds(x - r, y - r, x + r, y + r);
        coin.draw(canvas);
    }

    private void drawEllipse(Canvas canvas, int x, int y,int r) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        RectF circle = new RectF(x - r, y - r, x + r, y + r);
        canvas.drawOval(circle, paint);
    }

    private void drawRect(Canvas canvas, int x, int y,int w,int h) {
        canvas.save();
        canvas.translate(x,y);
        canvas.rotate((float) Math.cos(Math.toRadians(degrees)));

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
       // RectF rect = new RectF(x - w/2, y - h/2, x + w/2, y + h/2);
        RectF rect = new RectF( - w/2, 0 - h/2, 0 + w/2, 0 + h/2);
        canvas.drawRect(rect, paint);

        canvas.restore();
    }

   private double dist(int x1,int y1,int x2,int y2){
       return Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
   }
}
