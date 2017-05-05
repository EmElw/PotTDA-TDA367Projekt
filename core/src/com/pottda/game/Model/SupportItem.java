package com.pottda.game.model;
/**
 * Created by rikar on 2017-04-07.
 */

public abstract class SupportItem extends Item {
    int health;
    float acceleration;

    public SupportItem(int health, float acceleration) {
        this.health = health;
        this.acceleration = acceleration;
    }
}
