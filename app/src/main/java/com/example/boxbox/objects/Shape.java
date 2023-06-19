package com.example.boxbox.objects;

import static com.example.boxbox.Utils.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Shape {

    public final static double tileSize = 125;

    public final static int
        O = 0, I = 1, Z = 2, S = 3, L = 4, J = 5, T = 6
    ;

    public Boxbox[][][] matrices = new Boxbox[0][4][4];
    public Boxbox[][] matrix;

    public int shape, rotation = 0;

    Rect boundingRect;
    double x, y;

    public Shape(double x, double y, int shape) {
        this.shape = shape;
        this.x = x;
        this.y = y;

        initializeMatrices();
        matrix = matrices[rotation];
    }

    public void initializeMatrices() {
        Bitmap sprite = Sprites.shapesLayout;
        Boxbox[][] matrix = new Boxbox[4][4];

        boolean foundShape = false;

        int rPos = 0, cPos;
        for (int yPx = 0; yPx < sprite.getHeight(); yPx++) {
             for (int xPx = 0; xPx < sprite.getWidth(); xPx++) {
                int color = sprite.getPixel(xPx, yPx);
                int r = Color.red(color), g = Color.green(color), b = Color.blue(color), a = Color.alpha(color);

                 // If it's a color indicator and foundShape == true..
                 // then that means a rotation variant (or building the matrix) is completed
                 if (foundShape && r+g+b+a > 255*3) { // (r > 0, 255, 255, 255) --> color indicator
                     matrices = append3D(matrix, matrices);
                     matrix = new Boxbox[4][4];
                 }

                // look for the shape indicator
                    // only the red value in the shape indicator changes
                    // and its red value is equavalent to the int value of the ease-of-use constants for shape
                if (r == shape && g == 255 & b == 255) {
                    // -1 because after finding the shape indicator, it will break out of the inner loop
                    // and increment rPos by 1. So, by the time the loop gets to the first row index of the shape rotation
                    // layout, the rPos is already 1 when it should be zero... Which is why I started at -1
                    rPos = -1;
                    foundShape = true;
                    break;
                }
                else if (foundShape && r != shape && g == 255 & b == 255) {
                    foundShape = false;
                    yPx = sprite.getHeight();
                    break;
                }

                // build the matrix
                if (foundShape) {
                    cPos = xPx;
                    // if the pixel is black (0, 0, 0, 255)
                    if (r+g+b+a == 255) {
                        matrix[rPos][cPos] = new Boxbox(
                        x + cPos*tileSize,
                        y + rPos*tileSize,
                            tileSize, tileSize
                        );
                    }
                    // if the pixel is nothing / if there is no pixel (0, 0, 0, 0)
                    else if (r+g+b+a == 0) {
                        matrix[rPos][cPos] = null;
                    }
                }
             }
             rPos += 1;
        }
    }

    public void draw(Canvas canvas) {
        for (Boxbox[] boxes : matrix) {
            for (Boxbox box : boxes) {
                if (box != null)
                box.draw(canvas);
            }
        }
    }

}
