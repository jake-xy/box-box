package com.example.boxbox.objects;
import android.graphics.*;

import com.example.boxbox.Game;
import com.example.boxbox.R;
import static com.example.boxbox.utils.Utils.*;


public abstract class Sprites {

    public static Bitmap
        shapes_layout, boxes_simple_sprite, boxes_neon_sprite, boxes_neon_alt_sprite
    ;
    public static  Bitmap[]
        boxes_simple = new Bitmap[0],
        boxes_neon = new Bitmap[0],
        boxes_neon_alt = new Bitmap[0],
        font_sprites = new Bitmap[0]
    ;

    public static void initialize() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        shapes_layout = BitmapFactory.decodeResource(Game.res, R.drawable.shapes_layout, options);
        boxes_simple_sprite = BitmapFactory.decodeResource(Game.res, R.drawable.boxes_simple_sprite, options);
        boxes_neon_sprite = BitmapFactory.decodeResource(Game.res, R.drawable.boxes_neon_sprite, options);
        boxes_neon_alt_sprite = BitmapFactory.decodeResource(Game.res, R.drawable.boxes_neon_alt_sprite, options);

        // getting the individual box sprite
        int spriteBoxSize = boxes_simple_sprite.getHeight();
        int boxesNum = boxes_simple_sprite.getWidth()/spriteBoxSize;
        for (int i = 0; i < boxesNum; i++) {
            Bitmap box = Bitmap.createBitmap(
                boxes_simple_sprite,
                spriteBoxSize*i, 0,
                    spriteBoxSize, spriteBoxSize
            );

            boxes_simple = append(box, boxes_simple);
        }

        spriteBoxSize = boxes_neon_sprite.getHeight();
        boxesNum = boxes_neon_sprite.getWidth()/spriteBoxSize;
        for (int i = 0; i < boxesNum; i++) {
            Bitmap box = Bitmap.createBitmap(
                    boxes_neon_sprite,
                    spriteBoxSize*i, 0,
                    spriteBoxSize, spriteBoxSize
            );

            boxes_neon = append(box, boxes_neon);
        }

        spriteBoxSize = boxes_neon_alt_sprite.getHeight();
        boxesNum = boxes_neon_alt_sprite.getWidth()/spriteBoxSize;
        for (int i = 0; i < boxesNum; i++) {
            Bitmap box = Bitmap.createBitmap(
                    boxes_neon_alt_sprite,
                    spriteBoxSize*i, 0,
                    spriteBoxSize, spriteBoxSize
            );

            boxes_neon_alt = append(box, boxes_neon_alt);
        }

    }

}
