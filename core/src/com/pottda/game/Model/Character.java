package com.pottda.game.Model;

import java.util.List;
import java.util.Vector;

/**
 * Created by rikar on 2017-04-07.
 */

public class Character extends Actor {
    Inventory inventory;
    int cooldown;
    long lastAttack;
    int health;
    int currentHealth;
    float acceleration;

    public List<Projectile> attack(float attack) {
        return null;
    }

    @Override
    public Vector getMove() {
        return null;
    }
}
