package com.example.boxbox.utils;

import android.graphics.*;

import com.example.boxbox.Game;
import com.example.boxbox.objects.Sprites;

import static com.example.boxbox.utils.Utils.*;

public class XFont {

    final static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    static int[] widthOfChars = new int[chars.length()];
    static int[] xOfChars = new int[chars.length()];
    static int spriteHeight = 0, ID = 0;
    int id;
    int fontSpriteID;

    public double height, charSpacing = 4;

    public XFont(int fontSpriteID, double height) {
        this.height = height;
        this.fontSpriteID = fontSpriteID;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap sprite = BitmapFactory.decodeResource(Game.res, fontSpriteID, options);

        Sprites.font_sprites = append(sprite, Sprites.font_sprites);

        // initialize the width of every char
        int idxCount = 0, startPixel = 0, width = 0;
        for (int x = 0; x < sprite.getWidth(); x++) {
            int color = sprite.getPixel(x, 0);
            int r = Color.red(color), g = Color.green(color), b = Color.blue(color), a = Color.alpha(color);

            // get the widths of each char
            if (r == 118 && g == 118 && b == 118) {
                if (x > startPixel) {
                    width = x - startPixel;
                    widthOfChars[idxCount] = width;
                    xOfChars[idxCount] = startPixel;
                    startPixel = x + 1; // in the sprites, each char is separated by 1 pixel of gray
                    idxCount += 1;
                }
            }
        }
        spriteHeight = sprite.getHeight();

        id = ID;
        ID += 1;
    }

    public void render(String text, int xPos, int yPos, Canvas canvas) {

        Bitmap sprite = Sprites.font_sprites[id];

        int x = xPos;

        for (int i = 0; i < text.length(); i++) {
            int charI = XFont.chars.indexOf(text.charAt(i));

            // get scaled width and height
            int scaledH = (int) this.height;
            int scaledW = widthOfChars[charI] * scaledH / sprite.getHeight();

            Bitmap bmp = Bitmap.createBitmap(sprite, xOfChars[charI], 0, widthOfChars[charI], sprite.getHeight());
            bmp = Bitmap.createScaledBitmap(bmp, scaledW, scaledH, false);

            canvas.drawBitmap(bmp, x, yPos, null);

            x += scaledW + charSpacing;
        }

    }

    public int strWidth(String text) {
        int out = 0;

        for (int i = 0; i < text.length(); i++) {
            int charI = XFont.chars.indexOf(text.charAt(i));

            // get scaled width and height
            int scaledH = (int) this.height;
            int scaledW = widthOfChars[charI] * scaledH / spriteHeight;

            out += scaledW + charSpacing;
        }

        return out;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setCharSpacing(double pixels) {
        this.charSpacing = pixels;
    }



}
