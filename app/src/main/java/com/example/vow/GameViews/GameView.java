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

public class GameView extends View {
    private boolean initialized = true;
    private boolean coining = false;
    private boolean selectdir = false;
    private int speed = 10;
    private int ang = 0;
    private Drawable road, car, coin, background, grass;
    private int bollspeed = 200;
    private int roadBoundV = 400;
    private int roadBoundH = 600;
    private int carBound = 200;
    private int dir = 1;
    private List<PositionHolder> coins = new ArrayList<>();
    private List<PositionHolder> roads = new ArrayList<>();
    private List<PositionHolder> grasses = new ArrayList<>();

    GameEvents gameEvents;
    private int carX, carY;

    public GameView(Context context) {
        super(context);
        gameEvents = ViewModelProviders.of((FragmentActivity) context).get(GameEvents.class);
        car = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_car, null);
        coin = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_coin, null);
        road = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_road, null);
        background = ResourcesCompat.getDrawable(getResources(), R.drawable.bgplain, null);
        grass = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bush, null);


        gameEvents.getMove().observe((LifecycleOwner) getContext(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                assert value != null;
                if(value==0){
                    if(ang<90)ang+=4;
                    else ang =90;
                    if(selectdir)dir = Math.random()*100<50?-1:1;
                    selectdir = false;
                }else{
                    if(ang>0)ang-=4;
                    else ang=0;
                    selectdir = true;
                }
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(initialized){
            initialized = false;
            initGame();
        }

        /*--draw backgroud---*/
        drawBackGround(canvas);

        /*--draw road---*/
        displayRoad(canvas);

        /*--draw car---*/
        carX = (int)(bollspeed*Math.cos(Math.toRadians(ang)*dir)) + getWidth() / 2;
        carY = getHeight() / 2;
        drawCar(canvas, carX, carY);

        /*-- add coins----*/
        addCoins();

        /*-- draw coins---*/
        displayCoins(canvas);
        gameEvents.setCoins(0);

        /*-- draw coins---*/
        displayGrass(canvas);

        invalidate();
    }

    private void initGame(){
        for (int i = 0; i < 10; i++) {
            roads.add(new PositionHolder(getWidth()/2,-roadBoundV+i*roadBoundV));
            int pos = (Math.random()*100<50)?randomNumGen(0,getWidth()/2-roadBoundH/2):randomNumGen(getWidth()/2+roadBoundH/2,getWidth());
            grasses.add(new PositionHolder(pos,-roadBoundV+i*roadBoundV));
        }
        coins.add(new PositionHolder(getWidth() / 2, 0));
    }


    // Draw Background
    private void drawBackGround(Canvas canvas) {
        background.setBounds(0, 0, getWidth(), getHeight());
        background.draw(canvas);
    }

    // Draw Road
    private void displayRoad(Canvas canvas) {
        for (int i = 0; i < roads.size(); i++) {
            PositionHolder street = roads.get(i);
            if (street.y < getHeight()+roadBoundV) street.y += speed;
            else {
                roads.remove(i);
                roads.add(new PositionHolder(getWidth()/2,-roadBoundV));
            }
            road.setBounds(street.x - roadBoundH / 2, street.y - roadBoundV / 2, street.x + roadBoundH / 2, street.y + roadBoundV / 2);
            road.draw(canvas);
        }
    }


    // Draw Car
    private void drawCar(Canvas canvas, int x, int y) {
        car.setBounds(x - carBound / 2, y - carBound, x + carBound / 2, y + carBound);
        car.draw(canvas);
    }

    // add Coins
    private int N = 3;
    private void addCoins() {
        PositionHolder coin = coins.get(0);
        if (coin.y > getHeight() / N && coining) {
            for(int i=0;i<N;i++){
                coins.add(new PositionHolder(getWidth() / 2, i*getHeight()/N-getHeight()/N));
            }
            coining = false;
        }
    }

    // Draw coin
    private void displayCoins(Canvas canvas) {
        for (int i = 0; i < coins.size(); i++) {
            PositionHolder c = coins.get(i);
            if (dist(c.x, c.y, carX, carY) < 100) {
                coins.remove(i);
                coins.add(new PositionHolder(getWidth() / 2, 0));
                gameEvents.setCoins(100);
            }else if(c.y > getHeight()){
                coins.remove(i);
                coins.add(new PositionHolder(getWidth() / 2, 0));
            }
            c.y += speed;
        }

        for (int i = 0; i < coins.size(); i++) {
            PositionHolder c = coins.get(i);
            drawCoins(canvas, c.x, c.y);
        }
    }

    // draw Coins
    private void drawCoins(Canvas canvas, int x, int y) {
        int r = 50;
        coin.setBounds(x - r, y - r, x + r, y + r);
        coin.draw(canvas);
    }

    // draw vegitation

    private void displayGrass(Canvas canvas){
        for (int i = 0; i < grasses.size(); i++) {
            PositionHolder g = grasses.get(i);
            if (g.y > getHeight()) {
                grasses.remove(i);
                int pos = (Math.random()*100<50)?randomNumGen(0,getWidth()/2-roadBoundH/2):randomNumGen(getWidth()/2+roadBoundH/2,getWidth());
                grasses.add(new PositionHolder(pos, 0));
            }
            g.y += speed;
        }

        for (int i = 0; i < grasses.size(); i++) {
            PositionHolder g = grasses.get(i);
            drawGrass(canvas, g.x, g.y);
        }
    }

    private void drawGrass(Canvas canvas, int x, int y) {
        int r = 50;
        grass.setBounds(x - r, y - r, x + r, y + r);
        grass.draw(canvas);
    }

    private void drawEllipse(Canvas canvas, int x, int y, int r) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        RectF circle = new RectF(x - r, y - r, x + r, y + r);
        canvas.drawOval(circle, paint);
    }

    private void drawRect(Canvas canvas, int x, int y, int w, int h) {

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate((float) Math.cos(Math.toRadians(90)));

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        // RectF rect = new RectF(x - w/2, y - h/2, x + w/2, y + h/2);
       /* RectF rect = new RectF(-w / 2, 0 - h / 2, 0 + w / 2, 0 + h / 2);
        canvas.drawRect(rect, paint);*/
        canvas.restore();
    }


    // Math Functions
    private double dist(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    private long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private int randomNumGen(int min, int max) {
        int num = (int) (Math.random() * (max - min) + min);
        Log.d("Number", num + "");
        return num;
    }
}
