package com.example.vow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class GameView extends View {
    private int speed = 5;
    private int[] posVerticle = new int[10];
    private Drawable road, car, background;
    private int bollspeed = 30;
    private int degrees;

    public GameView(Context context) {
        super(context);
        for (int i = 0; i < posVerticle.length; i++) {
            posVerticle[i] = i * 100;
        }
        car = context.getResources().getDrawable(R.drawable.car_straight, null);
        road = context.getResources().getDrawable(R.drawable.ic_road, null);
        background = context.getResources().getDrawable(R.drawable.ic_junglebg, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /* Drawing Vector Drawable on Canvas*/

        /*--draw backgroud---*/
        drawBackGround(canvas);
        /*--draw road---*/
        drawRoad(canvas);
        /*--draw roadside vegetation---*/
        drawVegetation(canvas);
        /*--draw car---*/

        int carX = (int) (bollspeed * Math.sin(Math.toRadians(degrees)));//rand.nextInt(75) * (rand .nextBoolean() ? -1 : 1);
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

    }

    // Draw Road
    private void drawRoad(Canvas canvas) {
        for (int i = 0; i < 9; i++) {
            int roadX = getWidth() / 2 - 75;
            if (posVerticle[i] < getHeight()) posVerticle[i] += speed;
            else posVerticle[i] = -100 + speed;
            road.setBounds(roadX, posVerticle[i], roadX + 150, posVerticle[i] + 100);
            road.draw(canvas);
        }
    }

    // Draw Car 
    private void drawCar(Canvas canvas, int pos) {
        int carBound = 40;
        canvas.save();
        canvas.translate(getWidth()/2,getHeight() / 2);
        canvas.translate(pos-carBound/2, 0);
        car.setBounds(0,0,carBound,carBound);
        canvas.rotate((float) Math.cos(Math.toRadians(degrees))*bollspeed);
        car.draw(canvas);
        canvas.restore();
    }

}
