package com.pottda.game.model;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ModelState implements DeathListener, ScoreChangeListener, NewModelListener {

    private int score;
    private int enemiesAlive;

    private Character player;

    private Storage storage;
    private Inventory inventory;

    public ModelState() {
        score = 0;
        enemiesAlive = 0;
    }

    public boolean enemiesAlive() {
        return enemiesAlive != 0;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void onDeath(Character character) {
        if (character.team == ModelActor.ENEMY_TEAM) {
            enemiesAlive--;
            System.out.println("eA:" + enemiesAlive);
            if (enemiesAlive < 0)
                throw new Error("Less than 0 enemies alive");
        } else {
            player = null;
        }
    }

    @Override
    public void scoreChanged(int points) {
        score += points;
    }

    @Override
    public void onNewModel(ModelActor m) {
        if (m instanceof Character) {
            if (m.team == ModelActor.ENEMY_TEAM) {
                enemiesAlive++;
                System.out.println("eA" + enemiesAlive);
            } else {
                if (player == null) {
                    player = (Character) m;
                } else throw new Error("Created another player");
            }
            ((Character) m).addDeathListener(this);
        }
    }

    public boolean playerAlive() {
        return player != null;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Storage getStorage() {
        return storage;
    }

    public Character getPlayer() {
        return player;
    }
}
