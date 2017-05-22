package com.pottda.game.model;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;


/**
 * A class for abstracting enemy creation.
 * <p>
 * Generate a level with newLevel(), then poll
 * getToSpawn() for enemies that should be spawned
 */
public class WaveController {


    // Time til first spawn
    private static final long SPAWN_DELAY = 2000;

    // Maximum difference in time between an enemy group
    private static final int TIME_VARIANCE = 1500;

    //  ---- Default settings
    private static final float MAX_OF_DIFFICULTY_PER_SPAWN = 0.6f;
    private static final float MIN_DIFFICULTY_USAGE = 0.9f;
    private static final float DIFFICULTY_VARIANCE = 0.1f;
    private static final float MIN_TIME_BETWEEN_SPAWN_GROUPS = 5000;

    /**
     * The largest difficulty for a single spawn in terms of allocated difficulty
     * (i.e. no spawns with a difficulty above difficulty * maxDifficultyPerSpawn can be spawned)
     */
    private float maxDifficultyPerSpawn = MAX_OF_DIFFICULTY_PER_SPAWN;
    /**
     * One of two conditions to determine if a level-generation is completed
     * <p>
     * Sort of prevents small clutter
     */
    private final float minDifficultyUsage = MIN_DIFFICULTY_USAGE;
    /**
     * max variance in difficulty when generating a level
     */
    private final float difficultyVariance = DIFFICULTY_VARIANCE;
    /**
     * Ensures that no groups are within n milliseconds
     */
    private float minTimeBetweenSpawnGroups = MIN_TIME_BETWEEN_SPAWN_GROUPS;

    /**
     * The current levelData
     */
    private Level levelData;

    private int level = 0;

    public WaveController() {
        newLevel();
    }

    /**
     * Call to retrieve a list of {@code EnemyBlueprint} that should spawn at the current time
     *
     * @return a {@code List<EnemyBlueprint>}
     */
    public List<EnemyBlueprint> getToSpawn() {
        return levelData.getToSpawn();
    }

    /**
     * Returns true if there is nothing more to spawn
     *
     * @return
     */
    public boolean levelFinished() {
        return levelData.spawnEvents.size() == 0;
    }

    /**
     * Call to generate a new internal {@code Level}
     */
    public void newLevel() {
        level++;
        System.out.println("Starting level " + level);
        newLevel((6 * level + 9), (level + 30) * 1000); // TODO handle this better
    }

    private void newLevel(int difficulty, long length) {
        System.out.println("d: " + difficulty + " , l: " + length);
        double variance = 1 + (difficultyVariance - Math.random() * 2 * difficultyVariance);
        long currentTime = System.currentTimeMillis() + SPAWN_DELAY;
        int remainingDifficulty = (int) Math.round(difficulty * variance);
        long groupTime;

        List<EnemyGroup> pool = EnemyGroup.getAllBelowDifficulty(remainingDifficulty);
        levelData = new Level();

        while (remainingDifficulty > difficulty * (1 - minDifficultyUsage)
                && pool.size() > 0) {

            // Get a random group from the pool
            EnemyGroup group = pool.get((int) Math.floor(Math.random() * pool.size()));
            System.out.println("spawngroup: " + group.name);

            // The first spawn spawns immediatly
            if (levelData.spawnEvents.isEmpty()) {
                groupTime = currentTime + SPAWN_DELAY;
            } else {    // Consecutive groups spawn afterwards
                groupTime = (long) (currentTime + SPAWN_DELAY + length * Math.random());
            }


            // Create the levelData's SpawnEvents
            for (EnemyBlueprint bp : group.getEnemies()) {
                long timestamp = (long) (groupTime - TIME_VARIANCE + 2 * Math.random() * TIME_VARIANCE);
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
    public void quicken(long delta) {
        for (SpawnEvent evt : levelData.spawnEvents) {
            evt.quicken(delta);
        }
    }

    /**
     * Internal class for abstracting a levelData
     */
    private class Level {
        private final List<SpawnEvent> spawnEvents;
        private final List<SpawnEvent> toRemove;

        private final List<EnemyBlueprint> returnList;

        private Level() {
            spawnEvents = new ArrayList<SpawnEvent>();
            toRemove = new ArrayList<SpawnEvent>();
            returnList = new ArrayList<EnemyBlueprint>();
        }

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

    /**
     * Internal class for abstracting a timestamp where an enemy should spawn
     */
    private class SpawnEvent {
        private long timestamp;
        private final EnemyBlueprint enemyBlueprint;

        private SpawnEvent(long timestamp, EnemyBlueprint enemyBlueprint) {
            this.timestamp = timestamp;
            this.enemyBlueprint = enemyBlueprint;
        }

        /**
         * shortens the time to spawn by {@code delta} milliseconds
         *
         * @param delta a long
         */
        private void quicken(long delta) {
            timestamp -= delta;
        }

        /**
         * @return true if this SpawnEvent should spawn
         */
        private boolean shouldSpawn() {
            return (System.currentTimeMillis() > timestamp);
        }
    }
}
