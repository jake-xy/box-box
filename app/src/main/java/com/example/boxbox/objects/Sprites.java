package com.example.boxbox.objects;
import android.graphics.*;

import com.example.boxbox.Game;
import com.example.boxbox.R;


public abstract class Sprites {

    public static Bitmap shapesLayout;

    public static void initialize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        shapesLayout = BitmapFactory.decodeResource(Game.res, R.drawable.shapes_layout, options);
    }

}
