package com.pottda.game.model;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A class for abstracting enemy creation.
 * <p>
 * Generate a level with newLevel(), then poll
 * getToSpawn() for enemies that should be spawned
 * <p>
 * Variables dealing with time are in seconds
 */
public class WaveController {

    private static final float SPAWN_DELAY = 2;
    private static final float TIME_VARIANCE = 1.5f;
    private static final float MAX_OF_DIFFICULTY_PER_SPAWN = 0.6f;
    private static final float MIN_DIFFICULTY_USAGE = 0.9f;
    private static final float DIFFICULTY_VARIANCE = 0.1f;
    private static final float MIN_TIME_BETWEEN_SPAWN_GROUPS = 5000;

    private Level levelData;

    private int level = 0;

    public WaveController() {
        newLevel();
    }

    public List<EnemyBlueprint> getToSpawn() {
        return levelData.getToSpawn();
    }

    public boolean levelFinished() {
        return levelData.spawnEvents.size() == 0;
    }

    public void newLevel() {
        level++;
        System.out.println("Starting level " + level);
        newLevel((6 * level + 9), (level + 30)); // TODO handle this better
    }

    /**
     * Semi-randomly generates a level based based on a difficulty "pool"
     * <p>
     * Randomly fetches enemyGroups from the blueprint-class until the pool is depleted
     *
     * @param difficulty
     * @param lengthMS
     */
    private void newLevel(int difficulty, float length) {
        System.out.println("d: " + difficulty + " , l: " + length);

        double variance = 1 + plusMinus(DIFFICULTY_VARIANCE);
        int remainingDifficulty = (int) Math.round(difficulty * variance);
        float groupTime;

        List<EnemyGroup> pool = EnemyGroup.getAllBelowDifficulty(remainingDifficulty);
        levelData = new Level();

        while (remainingDifficulty > difficulty * (1 - MIN_DIFFICULTY_USAGE)
                && pool.size() > 0) {

            // Get a random group from the pool
            EnemyGroup group = pool.get((int) Math.floor(Math.random() * pool.size()));
            System.out.println("spawngroup: " + group.name);

            // The first spawn spawns immediately
            if (levelData.spawnEvents.isEmpty()) {
                groupTime = SPAWN_DELAY;
            } else {    // Consecutive groups spawn afterwards
                groupTime = (long) (SPAWN_DELAY + length * Math.random());
            }

            // Create the levelData's SpawnEvents
            for (EnemyBlueprint bp : group.getEnemies()) {
                long timestamp = (long) (groupTime + plusMinus(TIME_VARIANCE));
                levelData.spawnEvents.add(new SpawnEvent(timestamp, bp));
            }

            // Reduce the remaining diffulty
            remainingDifficulty -= group.getDifficulty();

            // Create a new pool with the remaining dificulty
            pool = EnemyGroup.getAllBelowDifficulty(remainingDifficulty);
        }
    }

    /**
     * Speeds up time progression causing spawns to appear {@code delta} quicker.
     *
     * @param delta a float in milliseconds
     */
    public void progressTime(float delta) {
        for (SpawnEvent evt : levelData.spawnEvents) {
            evt.timeToSpawn -= delta;
        }
    }

    private class Level {
        private final List<SpawnEvent> spawnEvents = new ArrayList<SpawnEvent>();
        private final List<SpawnEvent> toRemove = new ArrayList<SpawnEvent>();
        private final List<EnemyBlueprint> returnList = new ArrayList<EnemyBlueprint>();

        private List<EnemyBlueprint> getToSpawn() {
            returnList.clear();
            toRemove.clear();

            for (SpawnEvent evt : spawnEvents) {
                if (evt.shouldSpawn()) {
                    System.out.println("spawn enemy: " + evt.enemyBlueprint.getName());
                    returnList.add(evt.enemyBlueprint);
                    toRemove.add(evt);
                }
            }
            if (!toRemove.isEmpty()) {
                spawnEvents.removeAll(toRemove);
            }
            return returnList;
        }
    }

    private class SpawnEvent {
        private float timeToSpawn;
        private final EnemyBlueprint enemyBlueprint;

        private SpawnEvent(long timeToSpawn, EnemyBlueprint enemyBlueprint) {
            this.timeToSpawn = timeToSpawn;
            this.enemyBlueprint = enemyBlueprint;
        }

        private boolean shouldSpawn() {
            return (timeToSpawn <= 0);
        }
    }

    // Returns a random value n: -x <= n <= x
    private double plusMinus(double x) {
        return (Math.random() * 2 * x - x);
    }
}
