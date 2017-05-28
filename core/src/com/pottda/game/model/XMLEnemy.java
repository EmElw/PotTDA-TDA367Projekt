package com.pottda.game.model;

import com.pottda.game.assets.Sprites;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLEnemy {
    private final String name;
    final int scoreValue;
    private final int difficulty;
    private final ModelActor.Behaviour behaviour;
    private final String inventoryName;
    private final com.pottda.game.assets.Sprites sprite;

    public XMLEnemy(String name, String scoreValue, String difficulty, String behaviour, String inventoryName, String spriteName) {
        this.name = name;
        this.scoreValue = Integer.parseInt(scoreValue);
        this.difficulty = Integer.parseInt(difficulty);
        this.behaviour = ModelActor.Behaviour.valueOf(behaviour);
        this.inventoryName = inventoryName;
        this.sprite = com.pottda.game.assets.Sprites.valueOf(spriteName);
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public ModelActor.Behaviour getBehaviour() {
        return behaviour;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public Sprites getSprite() {
        return sprite;
    }
}
