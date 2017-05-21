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
    public final String spriteEnum;

    public XMLEnemy(String name, int scoreValue, int difficulty, ModelActor.Behaviour behaviour, String inventoryName, String spriteName) {
        this.name = name;
        this.scoreValue = scoreValue;
        this.difficulty = difficulty;
        this.behaviour = behaviour;
        this.inventoryName = inventoryName;
        this.spriteEnum = spriteName;
    }
}
