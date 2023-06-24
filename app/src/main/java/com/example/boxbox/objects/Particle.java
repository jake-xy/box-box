package com.example.boxbox.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.boxbox.Game;

public class Particle {

    double[] pos, vel;
    public double size;
    int boxColor;

    public Particle(double[] pos, double[] vel, double size, int boxColor) {
        this.pos = pos;
        this.vel = vel;
        this.size = size;
        this.boxColor = boxColor;
    }


    public void update() {
        pos[0] += vel[0] *Game.dt;
        pos[1] += vel[1] *Game.dt;
        double vel = (Math.abs(this.vel[0])+Math.abs(this.vel[1]))/2;
        size -= vel *Game.dt;
    }


    public void draw(Canvas canvas) {
        Bitmap box = Sprites.boxes_neon[boxColor];
        box = Bitmap.createScaledBitmap(box, (int) size, (int) size, false);

        canvas.drawBitmap(box, (float) pos[0], (float) pos[1], null);
    }


}
