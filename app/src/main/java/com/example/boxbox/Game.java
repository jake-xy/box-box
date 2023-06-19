package com.example.boxbox;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.boxbox.panels.MainLoop;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    GameLoop gameLoop;
    // panels
    MainLoop mainLoop;

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
        mainLoop = new MainLoop(this);

        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }


    public void update() {
        mainLoop.update();
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        mainLoop.draw(canvas);
    }
}
