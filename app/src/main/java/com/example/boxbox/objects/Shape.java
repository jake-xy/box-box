package com.example.boxbox.objects;

import static com.example.boxbox.utils.Utils.*;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.boxbox.Game;
import com.example.boxbox.utils.Utils;
import com.example.boxbox.panels.MainLoop;

import java.io.Serializable;

public class Shape implements Serializable {

    public static final double boxSize = Game.screen.w*0.85 / 8;;
    public static final double displayBoxSize = boxSize*0.5;
    public final static int // ease-of-use constants for shape
        O = 0, I = 1, Z = 2, S = 3, L = 4, J = 5, T = 6, o = 7, I4 = 8, I2 = 9, I3 = 10, D = 11, j1 = 12, j2 = 13,
        shapesNum = 14
    ;

    public Boxbox[][][] matrices = new Boxbox[0][5][5];
    public Boxbox[][] matrix, prevBoard;
    Rect boundingRect;

    public int shape, rotation = -1, color, id;

    double initialX = 0, initialY = 0, prevX, prevY;

    public boolean dragging, drawBoundingRect = false, canDrop = false, active = true;
    public double pressStartTime = 0;

    public Shape(int id, int shape) {
        this.shape = shape;
        this.id = id;
        color = Utils.randInt(Boxbox.colorsNum);
        dragging = false;

        initializeMatrices();
        rotate(); // to get/initialize the matrix

        initialX = Game.screen.w/3 * id - (Game.screen.w/3/2) - boundingRect.w/2;
        initialY = MainLoop.boardRect.bot + Shape.boxSize*2 - boundingRect.h/2;
        setPos(initialX, initialY);
    }


    public boolean onTouch(MotionEvent event, Boxbox[][] board) {
        System.out.println("touch");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (boundingRect.collides((int) event.getX(), (int) event.getY())) {
                    pressStartTime = System.currentTimeMillis();
                    if (!dragging) {
                        prevX = event.getX();
                        prevY = event.getY();

                        prevBoard = copy(board);
                    }
                    return true;
                }

            case MotionEvent.ACTION_UP:
                pressStartTime = 0;
                if (dragging) {
                    dragging = false;
                    if (!canDrop) {
                        setPos(initialX, initialY);
                    }
                    return true;
                }

            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    double xVel = (event.getRawX() - prevX);
                    double yVel = (event.getRawY() - prevY);

                    move(xVel, yVel, board);

                    prevX = event.getX();
                    prevY = event.getY();
                }

            default:
                if (dragging) {
                    move(0, 0, board);
                }
        }

        return false;
    }


    public void move(double xVel, double yVel, Boxbox[][] board) {
        int boxesNum = 0, spacesNum = 0;

        // move all the box
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (matrix[r][c] != null) {
                    matrix[r][c].moveX(xVel);
                    matrix[r][c].moveY(yVel);

                    boxesNum += 1;

                    // handle collision/placement
                    int boardC = (int)((matrix[r][c].centerX - MainLoop.boardRect.left)/boxSize);
                    int boardR = (int)((matrix[r][c].centerY - MainLoop.boardRect.top)/boxSize);

                    if (MainLoop.boardRect.collides((int) matrix[r][c].centerX, (int) matrix[r][c].centerY)) {
                        if (boardR >= 0 && boardR < 8 && boardC >= 0 && boardC < 8) {
                            if (board[boardR][boardC] == null) {
                                spacesNum += 1;

                                // if canDrop == true, it means there are already shadow boxes present on the board.
                                // Coincidentally, if at least 1 box moves to a space on the board, it means that
                                // the shape has been moved. So, revert the board back to its previous state before
                                // any shadow box is placed in order to check if the shape can be dropped to the new position
                                // that it has been moved to.
                                if (canDrop) {
                                    canDrop = false;
                                    revertBoard(board);
                                }
                            }
                            else if (board[boardR][boardC] != null && board[boardR][boardC].active) {
                                if (canDrop) {
                                    canDrop = false;
                                    revertBoard(board);
                                }
                            }
                        }
                    }
                    else {
                        if (canDrop) {
                            canDrop = false;
                            revertBoard(board);
                        }
                    }
                }
            }
        }
        // update the bounding rect
        updateBoundingRect();

        // check if can be placed
        if (boxesNum == spacesNum) {
            // place shadow boxes
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (matrix[r][c] != null) {
                        int boardC = (int)((matrix[r][c].centerX - MainLoop.boardRect.left)/boxSize);
                        int boardR = (int)((matrix[r][c].centerY - MainLoop.boardRect.top)/boxSize);

                        MainLoop.placeBox(boardR, boardC, color, board);
                        board[boardR][boardC].active = false;
                    }
                }
            }

            canDrop = true;
            // check if there is a row or a column that can be cleared
            for (int i = 0; i < 8; i++) {
                int verCount = 0, horCount = 0;
                for (int k = 0; k < 8; k++) {
                    // horizontal
                    if (board[i][k] != null) {
                        horCount += 1;
                    }

                    // vertical
                    if (board[k][i] != null) {
                        verCount += 1;
                    }
                }

                // change the color
                if (horCount == 8) {
                    for (int l = 0; l < 8; l++) {
                        board[i][l].color = this.color;
                    }
                }

                if (verCount == 8) {
                    for (int l = 0; l < 8; l++) {
                        board[l][i].color = this.color;
                    }
                }
            }
        }
    }


    public void setPos(double x, double y) {
        double boxSize = dragging ? Shape.boxSize : Shape.displayBoxSize;

        // move the shape
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (matrix[r][c] != null) {
                    matrix[r][c].setX(x + boxSize*c);
                    matrix[r][c].setY(y + boxSize*r);
                    matrix[r][c].setWidth(boxSize);
                    matrix[r][c].setHeight(boxSize);
                }
            }
        }

        updateBoundingRect();
    }


    public void rotate() {
        rotation += 1;
        if (rotation >= matrices.length) {
            rotation = 0;
        }

        matrix = matrices[rotation];
        updateBoundingRect();

        initialX = Game.screen.w/3 * id - (Game.screen.w/3/2) - boundingRect.w/2;
        initialY = MainLoop.boardRect.bot + Shape.boxSize*2 - boundingRect.h/2;
        setPos(initialX, initialY);
    }

    public void rotate(int rotation) {
        this.rotation = rotation;
        if (rotation >= matrices.length) {
            rotation = 0;
        }

        matrix = matrices[rotation];
        updateBoundingRect();

        initialX = Game.screen.w/3 * id - (Game.screen.w/3/2) - boundingRect.w/2;
        initialY = MainLoop.boardRect.bot + Shape.boxSize*2 - boundingRect.h/2;
        setPos(initialX, initialY);
    }


    public void updateBoundingRect() {
        double leftMost = Double.POSITIVE_INFINITY, topMost = Double.POSITIVE_INFINITY,
            rightMost = Double.NEGATIVE_INFINITY, botMost = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < matrix.length; i++) {
            for (int k = 0; k < matrix[0].length; k++) {
                if (matrix[i][k] != null) {
                    if (matrix[i][k].left < leftMost) {
                        leftMost = matrix[i][k].left;
                    }
                    if (matrix[i][k].right > rightMost) {
                        rightMost = matrix[i][k].right;
                    }
                    if (matrix[i][k].bot > botMost) {
                        botMost = matrix[i][k].bot;
                    }
                    if (matrix[i][k].top < topMost) {
                        topMost = matrix[i][k].top;
                    }
                }
            }
        }

        boundingRect = new Rect(leftMost, topMost, rightMost-leftMost, botMost - topMost);
    }

    public void initializeMatrices() {
        Bitmap sprite = Sprites.shapes_layout;
        Boxbox[][] matrix = new Boxbox[5][5];

        boolean foundShape = false;

        int rPos = 0, cPos;
        for (int yPx = 0; yPx < sprite.getHeight(); yPx++) {
             for (int xPx = 0; xPx < sprite.getWidth(); xPx++) {
                int color = sprite.getPixel(xPx, yPx);
                int r = Color.red(color), g = Color.green(color), b = Color.blue(color), a = Color.alpha(color);

                 // If it's a color indicator and foundShape == true..
                 // then that means a rotation variant (or building the matrix) is completed
                 if (foundShape && r+g+b+a > 255*3) { // (r > 0, 255, 255, 255) --> color indicator
                     boolean hasBox = false;
                     for (int i = 0; i < 5; i++) {
                         for (int k = 0; k < 5; k++) {
                             if (matrix[i][k] != null) {
                                 hasBox = true;
                                 i = 8;
                                 break;
                             }
                         }
                     }
                     if (hasBox) {
                        matrices = append3D(matrix, matrices);
                     }
                     matrix = new Boxbox[5][5];
                 }

                // look for the shape indicator on the shape layout sprite
                    // only the red value in the shape indicator changes.
                    // Its red value is equavalent to the int value of the ease-of-use constants for shape
                if (r == shape && g == 255 & b == 255) {
                    // -1 because after finding the shape indicator, it will break out of the inner loop
                    // and increment rPos by 1. So, by the time the loop gets to the first row index of the shape rotation
                    // layout, the rPos is already 1 when it should be zero... Which is why I started at -1
                    rPos = -1;
                    foundShape = true;
                    break;
                }
                // once the shape is found and the r val of the color indicator is different from this Shape's current
                // shape, it means all the rotations are found. So, break out of the 2 loops
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
                        initialX + cPos*displayBoxSize,
                        initialY + rPos*displayBoxSize,
                            displayBoxSize, displayBoxSize,
                            this.color
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
                if (box != null) {
                    box.draw(canvas);
                }
            }
        }

        if (drawBoundingRect) {
            // debug -- draws the bounding rect
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
            canvas.drawRect((float) boundingRect.left, (float) boundingRect.top, (float) boundingRect.right, (float) boundingRect.bot, paint);
        }
    }


    public Rect getRect() {
        return boundingRect;
    }

    public void setActive(boolean flag) {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (matrix[r][c] != null) {
                    matrix[r][c].active = flag;
                }
            }
        }
        active = flag;
    }


    private void revertBoard(Boxbox[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (prevBoard[i][k] != null) {
                    Boxbox newBox = new Boxbox(
                            prevBoard[i][k].x, prevBoard[i][k].y,
                            prevBoard[i][k].w, prevBoard[i][k].h,
                            prevBoard[i][k].color
                    );
                    board[i][k] = newBox;
                }
                else {
                    board[i][k] = null;
                }
            }
        }
    }

    public void setDragging(boolean flag) {
        if (!dragging) {
            dragging = true;
            double newW = boundingRect.w/displayBoxSize * boxSize,
                    newH = boundingRect.h/displayBoxSize * boxSize;

            setPos(
                    Game.screen.w/3 * id - (Game.screen.w/3/2) - newW/2,
                    boundingRect.top - (newH + displayBoxSize) )
            ;
        }
    }
}
