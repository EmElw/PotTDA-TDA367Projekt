package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundsAndMusic {
    private static Music music;

    /**
     * Plays the current music track
     */
    public static void play() {
        if (music == null) {
            initMusic();
        }
        if (!music.isPlaying()) {
            music.setVolume(0.5f);
            music.setLooping(true);
            music.play();
        }
    }

    private static void initMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/DragonSlayer.mp3"));
    }

    /**
     * Pauses the music if playing
     */
    public static void pauseMusic() {
        if (music == null) {
            initMusic();
        }
        if (music.isPlaying()) {
            music.pause();
        }
    }

    /**
     * Sets the new music volume
     *
     * @param volume new music volume. A float value between 0 and 1
     */
    public static void setMusicVolume(float volume) {
        if (music == null) {
            initMusic();
        }
        music.setVolume(volume);
    }

    public static float getMusicVolume() {
        if (music == null) {
            initMusic();
        }
        return music.getVolume();
    }

    /**
     * Sets the new SFX volume
     *
     */
    public void setSFXVolume() {
        //sound.setVolume(0, volume); //id?
    }

    /**
     * Checks if any music is playing
     *
     * @return true if any music is playing
     */
    public static boolean isMusicPlaying() {
        return music != null && music.isPlaying();
    }

    public static void dispose() {
        if (music != null) {
            music.dispose();
        }
        //sound.dispose();
    }

}