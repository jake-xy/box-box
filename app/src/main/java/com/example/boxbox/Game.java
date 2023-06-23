package com.example.boxbox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.example.boxbox.objects.*;
import com.example.boxbox.panels.MainLoop;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    // init objects
    GameLoop gameLoop;
    public static Resources res;
    public static Rect screen;

    // panels
    public static MainLoop mainLoop;

    // misc
    public static double dt, prevTime, GAME_SPEED = 30;


    public Game(Context context) {
        super(context);

        // get surface holder
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        setFocusable(true);
        gameLoop = new GameLoop(this, surfaceHolder);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        // initialize static variables
        screen = new Rect(0, 0, getWidth(), getHeight());
        res = this.getResources();
        prevTime = System.currentTimeMillis();
        Sprites.initialize();

        // intialize panels
        mainLoop = new MainLoop();

        // start game loop
        gameLoop.startLoop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mainLoop.onTouch(event)) return true;

        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    public void update() {
        // calculate delta time
        dt = (System.currentTimeMillis() - prevTime) / 1000;
        dt *= GAME_SPEED;
        prevTime = System.currentTimeMillis();

        // update panels
        mainLoop.update();

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        mainLoop.draw(canvas);
    }
}
