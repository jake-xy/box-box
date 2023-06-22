package com.example.boxbox.objects;

import java.io.Serializable;

public class Rect implements Serializable{

    public double x, y, w, h;
    public double top, left, bot, right, centerX, centerY;

    public Rect(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        updateRect();
    }


    private void updateRect() {
        top = y;
        left = x;
        bot = y + h;
        right = x + w;

        centerX = x + w/2;
        centerY = y + h/2;
    }


    public boolean collides(int x, int y) {

        if (x > this.x && x < this.x + this.w) {
            if (y > this.y && y < this.y + this.h) {
                return true;
            }
        }

        return false;
    }


    public boolean isInsideRect(Rect rect) {
        // if this rect is inside the passed rect

        if (left > rect.left && right < rect.right) {
            if (top > rect.top && bot < rect.bot) {
                return true;
            }
        }

        return false;
    }


    public boolean covers(Rect rect) {
        // if the passed rect is inside this rect

        return false;
    }


    public boolean collides(Rect rect) {

        if (right > rect.left && left < rect.right) {
            if (bot > rect.top && top < rect.bot) {
                return true;
            }
        }

        return false;
    }

    public void setY(double y) {
        this.y = y;
        updateRect();
    }

    public void setX(double x) {
        this.x = x;
        updateRect();
    }

    public void setHeight(double h) {
        this.h = h;
        updateRect();
    }

    public void setWidth(double w) {
        this.w = w;
        updateRect();
    }

    public void moveY(double val) {
        this.y += val;
        updateRect();
    }

    public void moveX(double val) {
        this.x += val;
        updateRect();
    }

    public String toString() {
        return "x: " + x + ", y: " + y + ", w: " + w + ", h: " + h;
    }

    public double getCenterX() {
        return left + w/2;
    }

    public double getCenterY() {
        return top + h/2;
    }

}
