package com.example.boxbox.objects;

import android.media.MediaPlayer;

import com.example.boxbox.Game;
import com.example.boxbox.R;

public abstract class Sound {

    public static MediaPlayer
        pick_up, drop, clear, touch, game_over_fill_box
    ;

    public static void initialize() {

        pick_up = MediaPlayer.create(Game.context, R.raw.pickup);
        drop = MediaPlayer.create(Game.context, R.raw.drop);
        clear = MediaPlayer.create(Game.context, R.raw.clear);
        touch = MediaPlayer.create(Game.context, R.raw.touch);
        game_over_fill_box = MediaPlayer.create(Game.context, R.raw.game_over_fill_box);

    }


    public static void play(MediaPlayer mp) {
        if (mp.isPlaying()) {
            mp.seekTo(0);
        }
        else {
            mp.start();
        }
    }

}
