package com.example.boxbox.panels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.boxbox.*;
import static com.example.boxbox.utils.Utils.*;
import com.example.boxbox.objects.*;
import com.example.boxbox.utils.XFont;

public class MainLoop extends GamePanel {

    Shape[] shapes = new Shape[0];
    Boxbox[][] board = new Boxbox[8][8];
    XFont scoreFont = new XFont(R.drawable.bahnshcrift_light, Game.screen.h*0.1);
    XFont plusScoreFont = new XFont(R.drawable.bahnshcrift_light, Game.screen.h*0.035);

    public static Rect boardRect;
    public int[] rowsToClear = new int[0], rowsCleared;
    public int[] colsToClear = new int[0], colsCleared;

    int score = 0;
    double displayScore = 0, checkGameOverStartTime = 0, gameOverTick = 0;

    private boolean startGameOver = false, gameOver = false;

    public MainLoop() {
        // initializing the board rect
        if (boardRect == null) {
            boardRect = new Rect(
              Game.screen.w/2 - Shape.boxSize*4,
              Game.screen.h * 0.15,
              Shape.boxSize*8, Shape.boxSize*8
            );
        }

        // initializing the shapes
        if (shapes.length == 0) {
            for (int i = 1; i <= 3; i++) {
                Shape shape = new Shape(i, randInt(Shape.shapesNum));
                shape.rotate(randInt(0, 4));
                shapes = append(shape, shapes);
            }
        }

//        boolean flag = false;
//        for (int r = 0; r < 7; r++) {
//            flag = flag ? false : true;
//            for (int c = 0; c < 8; c++) {
//                if (flag) {
//                    if (c % 2 == 0) {
//                        placeBox(r, c, randInt(Boxbox.colorsNum), board);
//                    }
//                }
//                else {
//                    if (c % 2 == 1) {
//                        placeBox(r, c, randInt(Boxbox.colorsNum), board);
//                    }
//                }
//            }
//        }

    }

    public boolean onTouch(MotionEvent event) {

        if (!startGameOver) {
            for (Shape shape : shapes) {
                if (shape.onTouch(event, board)) return true;
            }
        }

        return false;
    }


    @Override
    public void update() {

        boolean draggingAshape = false;

        for (Shape shape : shapes) {
            // once a shape is dropped
            if (shape.canDrop && !shape.dragging) {
                // remove the shape
                shapes = remove(shape, shapes);

                // activate the inactive boxes on the board
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if (board[r][c] != null && !board[r][c].active) {
                            board[r][c].active = true;
                            // 1 point for every box placed on the board
                            score += 1;
                        }
                    }
                }

                // check for rows and cols that can be cleared
                for (int i = 0; i < 8; i++) {
                    int horCount  = 0, verCount = 0;
                    for (int k = 0; k < 8; k++) {
                        // horizontal
                        if (board[i][k] != null) {
                            horCount++;
                        }
                        // vertical
                        if (board[k][i] != null) {
                            verCount++;
                        }
                    }

                    if (horCount == 8) {
                        rowsToClear = append(i, rowsToClear);
                    }
                    if (verCount == 8) {
                        colsToClear = append(i, colsToClear);
                    }
                }

                // remove the rows and cols that needs to be cleared
                for (int c : colsToClear) {
                    // clear that column
                    for (int r = 0; r < 8; r++) {
                        board[r][c] = null;
                    }
                }

                for (int r : rowsToClear) {
                    for (int c = 0; c < 8; c++) {
                        board[r][c] = null;
                    }
                }

                // Add score based on how many rows and cols are cleared
                // 10 points for every row and column cleared,
                // plus additional 20 points for every other column or row cleared
                score += (colsToClear.length + rowsToClear.length)*10;
                if (colsToClear.length + rowsToClear.length > 0) {
                    score += (colsToClear.length + rowsToClear.length - 1)*20;
                }

                // debug
                rowsCleared = rowsToClear.clone();
                colsCleared = colsToClear.clone();
                colsToClear = new int[0];
                rowsToClear = new int[0];

                checkGameOverStartTime = System.currentTimeMillis();
            }

            // update a flag that will be used to check for gameover
            if (shape.dragging) {
                draggingAshape = true;
            }

            // condition to start dragging
            if (!shape.dragging) {
                if (shape.pressStartTime > 0 && System.currentTimeMillis() - shape.pressStartTime > 50) {
                    shape.setDragging(true);
                }
            }
        }

        // checking game over
        if (System.currentTimeMillis() - checkGameOverStartTime > 600) {
            // updating shapes to either active or inactve
            if (!draggingAshape) {
                for (Shape shape : shapes) {

                    boolean canPlace = false;
                    Shape testShape = copy(shape);
                    Boxbox[][] testBoard = copy(board);
                    testShape.prevBoard = copy(testBoard);
                    testShape.updateBoundingRect();
                    testShape.dragging = true;
                    // placing the test shape all around the test board
                    for (int r = 0; r < 8; r++) {
                        for (int c = 0; c < 8; c++) {
                            double x, y;
                            x = boardRect.left + c*Shape.boxSize;
                            y = boardRect.top + r*Shape.boxSize;

                            testShape.setPos(x, y);
                            testShape.move(0, 0, testBoard);

                            if (testShape.canDrop) {
                                canPlace = true;
                                r = 8;
                                break;
                            }
                        }
                    }

                    shape.setActive(canPlace);
                }
            }

            // condition for gameover
            boolean hasActiveShape = false;
            for (Shape shape : shapes) {
                if (shape.active) {
                    hasActiveShape = true;
                    break;
                }
            }

            if (!hasActiveShape) {
                startGameOver = true;
            }

            checkGameOverStartTime = 0;
        }

        // refreshing the shapes
        if (shapes.length == 0) {
            for (int i = 1; i <= 3; i++) {
                Shape shape = new Shape(i, randInt(Shape.shapesNum));
                shape.rotate(randInt(0, 4));
                shapes = append(shape, shapes);
            }
        }

        // updating the display score
        if (displayScore < score) {
            displayScore += 0.5 + 1*(int)((score-displayScore)/10) *Game.dt;
        }
        else {
            displayScore = score;
        }


        if (startGameOver) {

            gameOverTick += 1 *Game.dt;
            int tick = (int)gameOverTick;

            // animation
            if (!gameOver && tick % (int)(Game.GAME_SPEED*0.07) == 0) {
                if (MainLoop.getBoxesNum(board) < 64) {
                    int r = randInt(8), c = randInt(8);
                    while (board[r][c] != null) {
                        r = randInt(8); c = randInt(8);
                    }

                    MainLoop.placeBox(r, c, Boxbox.DEAD_COLOR, board);
                }
                else {
                    // game over
                    gameOver = true;
                    gameOverTick = 0;
                }
            }

            if (gameOver) {
                if (tick > 0 && tick % (int)(Game.GAME_SPEED*1.5) == 0) {
                    // open gameover panel
                    Game.mainLoop = new MainLoop();
                }
            }


        }

    }

    @Override
    public void draw(Canvas canvas) {
        // background
        Paint paint = new Paint();
        paint.setColor(Color.rgb(60,80,150));
        canvas.drawRect(0, 0, (int)Game.screen.right, (int)Game.screen.bot, paint);

        // drawing the board
            // background
        paint.setColor(Color.argb(200, 30,40,70));
        int offSet = 20;
        canvas.drawRoundRect(
            (float) (boardRect.left - offSet/2),
            (float) (boardRect.top - offSet/2),
            (float) (boardRect.right + offSet/2),
            (float) (boardRect.bot + offSet/2),
            40,
            40,
            paint
        );
            // mini squares on the background
        paint.setColor(Color.rgb(35, 45, 80));
        double miniBoxSize = Shape.boxSize * 0.95;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                canvas.drawRoundRect(
                    (float) (boardRect.left + c*Shape.boxSize),
                    (float) (boardRect.top + r*Shape.boxSize),
                    (float) (boardRect.left + c*Shape.boxSize + miniBoxSize),
                    (float) (boardRect.top + r*Shape.boxSize + miniBoxSize),
                    30,
                    30,
                    paint
                );
            }
        }

        // drawing the contents of the board
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    board[r][c].draw(canvas);
                }
            }
        }

        // drawing the shapes
        for (Shape shape : shapes) {
            shape.draw(canvas);
        }

        // drawing the score
        scoreFont.render(
            Integer.toString((int)(displayScore)),
            (int) (Game.screen.w/2 - scoreFont.strWidth(Integer.toString((int)(displayScore)))/2),
            (int) (Game.screen.h * 0.04),
            canvas
        );
    }


    public static void placeBox(int r, int c, int color, Boxbox[][] board) {
        board[r][c] = new Boxbox(
            boardRect.left + c*Shape.boxSize,
            boardRect.top + r*Shape.boxSize,
            Shape.boxSize, Shape.boxSize,
            color
        );
    }


    public static int getBoxesNum(Boxbox[][] board) {
        int out = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board[r][c] != null) {
                    out += 1;
                }
            }
        }

        return out;
    }
}
