package com.example.boxbox.utils;

import android.graphics.Bitmap;

import com.example.boxbox.objects.*;

import java.util.Random;

public abstract class Utils {

    public static Boxbox[] append(Boxbox item, Boxbox[] array) {
        Boxbox[] out = new Boxbox[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }


    public static Bitmap[] append(Bitmap item, Bitmap[] array) {
        Bitmap[] out = new Bitmap[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }


    public static Shape[] append(Shape item, Shape[] array) {
        Shape[] out = new Shape[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }


    public static int[] append(int item, int[] array) {
        int[] out = new int[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }


    public static Boxbox[][][] append3D(Boxbox[][] item, Boxbox[][][] array) {
        Boxbox[][][] out = new Boxbox[array.length+1][item.length][item[0].length];

        for (int i = 0; i < array.length; i++) {
            Boxbox[][] array2D = array[i].clone();
            out[i] = array2D;
        }

        out[out.length-1] = item;

        return out;
    }

    public static boolean isIn(char item, char[] array) {

        for (char x : array) {
            if (x == item) return true;
        }

        return false;
    }


    public static int randInt(int max) {
        /*
        Returns a pseudo random integer from 0 to max.
        Max is exclusive
        */
        Random rand = new Random();
        return rand.nextInt(max);
    }

    public static int randInt(int min, int max) {
        /*
        Returns a pseudo random integer from min to max.
        Max is exclusive
        */
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }

    public static Boxbox[][] copy(Boxbox[][] array) {
        Boxbox[][] out = new Boxbox[array.length][array[0].length];

        for (int i = 0; i < array.length; i++) {
            for (int k = 0; k < array[0].length; k++) {
                Boxbox box = array[i][k];
                if (box == null) {
                    out[i][k] = null;
                }
                else {
                    out[i][k] = new Boxbox(box.x, box.y, box.w, box.h, box.color);
                }
            }
        }

        return out;
    }


    public static Shape copy(Shape item) {
        Shape out = new Shape(item.id, item.shape);
        out.rotate(item.rotation);
        return out;
    }


    public static Shape[] remove(Shape item, Shape[] array) {
        Shape[] out = new Shape[array.length - 1];

        int offSet = 0;
        for (int i = 0; i < array.length; i++) {
            if (!item.equals(array[i])) {
                out[i-offSet] = array[i];
            }
            else {
                offSet += 1;
            }
        }

        return out;
    }

}
