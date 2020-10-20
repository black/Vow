package com.example.vow.Progress;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import static android.content.Context.MODE_PRIVATE;

public class ProgressView extends View {
    private int level;
    private int score;
    private int coins;

    public ProgressView(Context context) {
        super(context);
        SharedPreferences sharedPreferences = context.getSharedPreferences("VOWGAME", MODE_PRIVATE);
        level = sharedPreferences.getInt("currentLevel", 0);
        score = sharedPreferences.getInt("totalScore", 0);
        coins = sharedPreferences.getInt("totalCoin", 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawEllipse(canvas,getWidth()/2,getHeight()/2,100);
        invalidate();
    }

    // Draw Background
    private void drawBackGround(Canvas canvas) {
       /* background.setBounds(0, 0, getWidth(), getHeight());
        background.draw(canvas);*/
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
       /* canvas.save();
        canvas.translate(x,y);
        canvas.rotate((float) Math.cos(Math.toRadians(degrees)));

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        // RectF rect = new RectF(x - w/2, y - h/2, x + w/2, y + h/2);
        RectF rect = new RectF( - w/2, 0 - h/2, 0 + w/2, 0 + h/2);
        canvas.drawRect(rect, paint);

        canvas.restore();*/
    }
}
