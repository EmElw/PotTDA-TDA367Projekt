package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class SoundsAndMusic {
    private static Music music;

    public SoundsAndMusic() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/DragonSlayer.mp3"));
    }

    /**
     *  Plays the current music track
     */
    public void play() {
        if (!music.isPlaying()) {
            music.setVolume(0.5f);
            music.setLooping(true);
            music.play();
        }
    }

    /**
     *  Pauses the music if playing
     */
    public void pauseMusic() {
        if (music.isPlaying()) {
            music.pause();
        }
    }

    /**
     * Sets the new music volume
     *
     * @param volume new music volume. A float value between 0 and 1
     */
    public void setMusicVolume(float volume) {
        music.setVolume(volume);
    }

    /**
     * Sets the new SFX volume
     *
     * @param volume new SFX volume. A float value between 0 and 1
     */
    public void setSFXVolume(float volume) {
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

    public void dispose() {
        music.dispose();
        //sound.dispose();
    }

}