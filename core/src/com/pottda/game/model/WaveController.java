package com.pottda.game.model;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class WaveController {


    // Time til first spawn
    private static final long SPAWN_DELAY = 2000;

    // Maximum difference in time between an enemy group
    private static final int TIME_VARIANCE = 1500;

    // Default settings
    private static final float MAX_OF_DIFFICULTY_PER_SPAWN = 0.6f;
    private static final float MAX_DIFFICULTY_PER_WAVE = 0.3f;


    private static final float MIN_DIFFICULTY_USAGE = 0.9f;
    private static final float DIFFICULTY_VARIANCE = 0.1f;
//    private static final long TIME_PER_WAVE = 15000;

    //The largest % of the total alloted difficulty allowed per spawn
    private float maxDifficultyPerSpawn = MAX_OF_DIFFICULTY_PER_SPAWN;
    private float maxDifficultyPerWave = MAX_DIFFICULTY_PER_WAVE;
    private float minDifficultyUsage = MIN_DIFFICULTY_USAGE;
    private float difficultyVariance = DIFFICULTY_VARIANCE;
//    private long timePerWave = TIME_PER_WAVE;

    /**
     * The current level
     */
    private Level level;

    /**
     * Call to retrieve a list of {@code EnemyBlueprint} that should spawn at the current time
     *
     * @return a {@code List<EnemyBlueprint>}
     */
    public List<EnemyBlueprint> getToSpawn() {
        return level.getToSpawn();
    }

    /**
     * Generates a new Level based on the given difficulty
     *
     * @param difficulty an int for the alloted difficulty
     */
    public void newLevel(int difficulty, long length) {
        double variance = 1 + (difficultyVariance - Math.random() * 2 * difficultyVariance);
        long currentTime = System.currentTimeMillis() + SPAWN_DELAY;
        int remainingDifficulty = (int) Math.round(difficulty * variance);
        long groupTime;

        List<EnemyGroup> pool = EnemyGroup.getAllBelowDifficulty(remainingDifficulty);
        level = new Level();

        while (remainingDifficulty > difficulty * (1 - minDifficultyUsage)
                && pool.size() > 0) {

            // Get a random group from the pool
            EnemyGroup group = pool.get((int) Math.floor(Math.random() * pool.size()));

            // The first spawn spawns immediatly
            if (level.spawnEvents.isEmpty()) {
                groupTime = currentTime + SPAWN_DELAY;
            } else {    // Consecutive groups spawn afterwards
                groupTime = (long) (currentTime + SPAWN_DELAY + length * Math.random());
            }


            // Create the level's SpawnEvents
            for (EnemyBlueprint bp : group.getEnemies()) {
                long timestamp = (long) (groupTime - TIME_VARIANCE + 2 * Math.random() * TIME_VARIANCE);
                level.spawnEvents.add(new SpawnEvent(timestamp, bp));
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
        for (SpawnEvent evt : level.spawnEvents) {
            evt.quicken(delta);
        }
    }

    /**
     * Internal class for abstracting a level
     */
    private class Level {
        private List<SpawnEvent> spawnEvents;
        private List<SpawnEvent> toRemove;

        private List<EnemyBlueprint> returnList;

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
                    returnList.add(evt.enemyBlueprint);
                    toRemove.add(evt);
                }
            }
            if (!toRemove.isEmpty()) {
                spawnEvents.remove(toRemove);
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

//
//
//    private final float WIDTH_METERS;
//    private final float HEIGHT_METERS;
//    private final float scaling;
//
//    private Levels levels;
//
//    /**
//     * Creates the controller
//     *
//     * @param widthMeters  the game stage width
//     * @param heightMeters the game stage height
//     * @param scale        scale with this value
//     */
//    public WaveController(float widthMeters, float heightMeters, float scale) {
//        WIDTH_METERS = widthMeters;
//        HEIGHT_METERS = heightMeters;
//        scaling = scale;
//        levels = new Levels();
//    }
//
//    /**
//     * Checks if all waves are completed
//     *
//     * @return true if all waves in a level is completed
//     */
//    public boolean finishedWaves() {
//        return levels.finishedWaves();
//    }
//
//    /**
//     * Prepares the next level
//     */
//    public void initNextLevel() {
//        levels.initNextLevel();
//    }
//
//    /**
//     * Sets the start time of the timer that checks waiting time
//     *
//     * @param startTime current time
//     */
//    public void setStartTime(long startTime) {
//        levels.setStartTime(startTime);
//    }
//
//    /**
//     * Checks if the user has waited at least five seconds
//     *
//     * @return true if waiting time is more than five seconds
//     */
//    public boolean waited() {
//        return levels.waited();
//    }
//
//    /**
//     * Starts the next wave and spawns more enemies
//     */
//    public void startWave() {
//        levels.startWave();
//        spawnEnemies(levels.getEnemiesToSpawn());
//    }
//
//    /**
//     * Spawns more enemies in the game
//     *
//     * @param nrOfEnemies the number of enemies to spawn
//     */
//    private void spawnEnemies(int nrOfEnemies) {
//        // Add some enemies
//        for (int i = 0; i < nrOfEnemies; i++) {
//            float xx = (float) (Math.random() * WIDTH_METERS);
//            float yy = (float) (Math.random() * HEIGHT_METERS);
//            try {
////                ActorFactory.get().buildEnemy(Sprites.ENEMY, new Vector2f(xx, yy), "inventoryblueprint/playerStartInventory.xml");
//                new CharacterBuilder().
//                        setTeam(Character.ENEMY_TEAM).
//                        setInventoryFromFile("playerStartInventory.xml").
//                        setBehaviour(Character.Behaviour.DUMB).
//                        setPosition(new Vector2f(xx, yy)).
//                        setSprite(Sprites.ENEMY).
//                        create();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

}
