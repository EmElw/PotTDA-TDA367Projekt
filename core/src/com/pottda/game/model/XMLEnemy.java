package com.pottda.game.model;

/**
 * Created by Magnus on 2017-05-21.
 */
public class XMLEnemy {
    private final String name;
    private final int scoreValue;
    private final int difficulty;
    private final ModelActor.Behaviour behaviour;
    private final String inventoryName;

    public XMLEnemy(String name, int scoreValue, int difficulty, ModelActor.Behaviour behaviour, String inventoryName) {
        this.name = name;
        this.scoreValue = scoreValue;
        this.difficulty = difficulty;
        this.behaviour = behaviour;
        this.inventoryName = inventoryName;
    }
}
