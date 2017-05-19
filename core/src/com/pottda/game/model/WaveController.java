package com.pottda.game.model;

import com.pottda.game.model.Levels;
import com.pottda.game.model.Sprites;
import com.pottda.game.model.builders.CharacterBuilder;

import javax.vecmath.Vector2f;
import java.lang.*;

public class WaveController {
    private final float WIDTH_METERS;
    private final float HEIGHT_METERS;
    private final float scaling;

    private Levels levels;

    /**
     * Creates the controller
     *
     * @param widthMeters  the game stage width
     * @param heightMeters the game stage height
     * @param scale        scale with this value
     */
    public WaveController(float widthMeters, float heightMeters, float scale) {
        WIDTH_METERS = widthMeters;
        HEIGHT_METERS = heightMeters;
        scaling = scale;
        levels = new Levels();
    }

    /**
     * Checks if all waves are completed
     *
     * @return true if all waves in a level is completed
     */
    public boolean finishedWaves() {
        return levels.finishedWaves();
    }

    /**
     * Prepares the next level
     */
    public void initNextLevel() {
        levels.initNextLevel();
    }

    /**
     * Sets the start time of the timer that checks waiting time
     *
     * @param startTime current time
     */
    public void setStartTime(long startTime) {
        levels.setStartTime(startTime);
    }

    /**
     * Checks if the user has waited at least five seconds
     *
     * @return true if waiting time is more than five seconds
     */
    public boolean waited() {
        return levels.waited();
    }

    /**
     * Starts the next wave and spawns more enemies
     */
    public void startWave() {
        levels.startWave();
        spawnEnemies(levels.getEnemiesToSpawn());
    }

    /**
     * Spawns more enemies in the game
     *
     * @param nrOfEnemies the number of enemies to spawn
     */
    private void spawnEnemies(int nrOfEnemies) {
        // Add some enemies
        for (int i = 0; i < nrOfEnemies; i++) {
            float xx = (float) (Math.random() * WIDTH_METERS);
            float yy = (float) (Math.random() * HEIGHT_METERS);
            try {
//                ActorFactory.get().buildEnemy(Sprites.ENEMY, new Vector2f(xx, yy), "inventoryblueprint/playerStartInventory.xml");
                new CharacterBuilder().
                        setTeam(Character.ENEMY_TEAM).
                        setInventoryFromFile("playerStartInventory.xml").
                        setBehaviour(Character.Behaviour.DUMB).
                        setPosition(new Vector2f(xx, yy)).
                        setSprite(Sprites.ENEMY).
                        create();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
