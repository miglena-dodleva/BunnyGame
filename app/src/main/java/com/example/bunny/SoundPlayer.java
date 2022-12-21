package com.example.bunny;

import static android.media.AudioManager.STREAM_MUSIC;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;

class SoundPlayer {
    private static SoundPool soundPool;
    private static int collectSound, overSound;

    @SuppressLint("ObsoleteSdkInt")
    public SoundPlayer(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // SoundPool is deprecated in API level 21.(Lollipop)
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(2)
                    .build();
        } else {
            soundPool = new SoundPool(2, STREAM_MUSIC, 0);
        }

        collectSound = soundPool.load(context, R.raw.collect, 1);
        overSound = soundPool.load(context, R.raw.over, 1);
    }

    public void playCollectSound(){
        soundPool.play(collectSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playOverSound(){
        soundPool.play(overSound,1.0f, 1.0f, 1, 0, 1.0f);
    }

}
















