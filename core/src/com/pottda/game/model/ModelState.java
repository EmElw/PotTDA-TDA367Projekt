package com.pottda.game.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Magnus on 2017-05-26.
 */
public class ModelState implements DeathListener, NewModelListener {

    private final int MAX_INVENTORY_WIDTH = 16;
    private final int MAX_INVENTORY_HEIGHT = 12;

    private int score;
    private int enemiesAlive;

    private Character player;

    private final Storage storage;
    private final List<ScoreChangeListener> scoreChangeListeners;

    private Set<Item> droppedItems;

    public ModelState() {
        storage = new Storage();
        score = 0;
        enemiesAlive = 0;
        scoreChangeListeners = new ArrayList<ScoreChangeListener>();
        droppedItems = new HashSet<Item>();
    }

    public boolean enemiesAlive() {
        return enemiesAlive != 0;
    }

    public int getScore() {
        return score;
    }

    public Set<Item> getDroppedItems() {
        return droppedItems;
    }

    @Override
    public void onDeath(Character character) {
        if (character.getTeam() == ModelActor.ENEMY_TEAM) {
            droppedItems = character.getInventory().getItemDropList();
            storage.addItems(droppedItems);
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
            if (m.getTeam() == ModelActor.ENEMY_TEAM) {
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

    public void expandPlayerInventory() {
        Inventory playerInventory = player.inventory;
        playerInventory.setDimensions(Math.min(MAX_INVENTORY_WIDTH, playerInventory.getWidth() + 1),
                Math.min(MAX_INVENTORY_HEIGHT, playerInventory.getHeight() + 1));
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
