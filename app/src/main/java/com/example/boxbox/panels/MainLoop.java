package com.example.boxbox.panels;

import android.graphics.Canvas;

import com.example.boxbox.*;
import com.example.boxbox.objects.Shape;

public class MainLoop extends GamePanel {

    Game game;
    Shape shape;

    public MainLoop(Game game) {
        this.game = game;

        shape = new Shape(50, 50, Shape.T);
    }


    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        shape.draw(canvas);
    }
}
