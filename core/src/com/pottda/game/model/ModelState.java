package com.pottda.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ModelState implements DeathListener, NewModelListener {

    private int score;
    private int enemiesAlive;

    private Character player;

    private Storage storage;
    private Inventory inventory;
    private List<ScoreChangeListener> scoreChangeListeners;

    public ModelState() {
        storage = new Storage();
        score = 0;
        enemiesAlive = 0;
        scoreChangeListeners = new ArrayList<ScoreChangeListener>();
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
            storage.addItems(character.inventory.getItemDropList());
            enemiesAlive--;
            score += character.getScoreValue();
            for (ScoreChangeListener scl : scoreChangeListeners) {
                scl.scoreChanged(character.getScoreValue());
            }
            if (enemiesAlive < 0)
                throw new Error("Less than 0 enemies alive");
        } else {
            player = null;
        }
    }


    @Override
    public void onNewModel(ModelActor m) {
        if (m instanceof Character) {
            if (m.team == ModelActor.ENEMY_TEAM) {
                enemiesAlive++;
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

    public void addScoreChangeListener(ScoreChangeListener listener) {
        scoreChangeListeners.add(listener);
    }
}
