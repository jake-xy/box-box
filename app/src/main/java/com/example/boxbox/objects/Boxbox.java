package com.example.boxbox.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Boxbox extends Rect {

    public final static int
        YELLOW = 0, CYAN = 1, RED = 2, GREEN = 3, ORANGE = 4, BLUE = 5, PURPLE = 6
    ;


    public Boxbox(double x, double y, double w, double h) {
        super(x, y, w, h);

    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawRect((float) left, (float) top, (float) right, (float) bot, paint);
    }

}
