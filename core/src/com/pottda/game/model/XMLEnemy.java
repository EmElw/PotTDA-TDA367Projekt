package com.pottda.game.model;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLEnemy {
    public final String name;
    public final int scoreValue;
    public final int difficulty;
    public final ModelActor.Behaviour behaviour;
    public final String inventoryName;
    public final Sprites sprite;

    public XMLEnemy(String name, String scoreValue, String difficulty, String behaviour, String inventoryName, String spriteName) {
        this.name = name;
        this.scoreValue = Integer.parseInt(scoreValue);
        this.difficulty = Integer.parseInt(difficulty);
        this.behaviour = ModelActor.Behaviour.valueOf(behaviour);
        this.inventoryName = inventoryName;
        this.sprite = Sprites.valueOf(spriteName);
    }
}
