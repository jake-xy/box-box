package com.example.boxbox.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Boxbox extends Rect {

    public final static int
        YELLOW = 0, CYAN = 1, RED = 2, GREEN = 3, ORANGE = 4, BLUE = 5, PURPLE = 6,
        DEAD_COLOR = 7,
        colorsNum = 7;

    public int color;
    public boolean active = true;

    public Boxbox(double x, double y, double w, double h, int color) {
        super(x, y, w, h);
        this.color = color;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        Bitmap box = Sprites.boxes_neon[color];
        box = Bitmap.createScaledBitmap(box, (int) w, (int) h, false);

        paint.setAlpha(active ? 255 : 100);

        canvas.drawBitmap(box, (float) left, (float) top, paint);
    }

}
