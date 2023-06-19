package com.example.boxbox;

import com.example.boxbox.objects.*;

public abstract class Utils {

    public static Boxbox[] append(Boxbox item, Boxbox[] array) {
        Boxbox[] out = new Boxbox[array.length + 1];

        for (int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }

        out[out.length-1] = item;

        return out;
    }


    public static Boxbox[][][] append3D(Boxbox[][] item, Boxbox[][][] array) {
        Boxbox[][][] out = new Boxbox[array.length+1][item[0].length][item[0].length];

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

}
