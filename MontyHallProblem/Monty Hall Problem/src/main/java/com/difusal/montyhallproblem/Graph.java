package com.difusal.montyhallproblem;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Graph extends View {
    float current, winWidth;
    double winPer;
    Paint paint;

    public Graph(Context context, long lhs, long rhs) {
        super(context);
        start(lhs, rhs);
    }

    private void start(long lhs, long rhs) {
        paint = new Paint();

        current = 0;

        long total = lhs + rhs;

        if (total == 0)
            winPer = 50;
        else
            winPer = 100.0 * lhs / total;
    }

    public void onDraw(Canvas canvas) {
        winWidth = (float) (getWidth() * winPer / 100);
        /*
        current++;
        if (winWidth < current)
            current = winWidth;
        */

        // fill with red
        canvas.drawColor(Color.RED);

        // draw green
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, winWidth, getHeight(), paint);

        // draw border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLACK);
        canvas.drawRect(0, 0, getWidth() + 1, getHeight() + 1, paint);
    }
}
