package com.pottda.game.model;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class Levels {
    private int currentLevel = 0;
    private int currentWave = 0;
    private int nrOfWaves = 0;
    private long startTime = 0;
    private static final int WAITING_TIME_SECONDS = 5;
    private int enemiesToSpawn = 0;

    /**
     * Sets variables to prepare for next level
     */
    public void initNextLevel() {
        ++currentLevel;
        currentWave = 0;
        nrOfWaves = 2 + (int) (Math.random() * 3);
        System.out.println("current level: " + currentLevel + ", nr of waves: " + nrOfWaves);
    }

    /**
     * Starts the next wave and spawns enemies
     */
    public void startWave() {
        ++currentWave;
        enemiesToSpawn = 5 + (int) (Math.random() * 3 * (currentLevel + currentWave - 2));
        System.out.println("Starting wave " + currentWave + " with " + enemiesToSpawn + " enemies");
    }

    public int getEnemiesToSpawn() {
        return enemiesToSpawn;
    }

    public boolean finishedWaves() {
        return currentWave >= nrOfWaves;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean waited() {
        final long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) / 1000 >= WAITING_TIME_SECONDS;
    }
}
