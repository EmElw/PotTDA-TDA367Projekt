package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;


public abstract class AttackItem extends Item {
    int damage;
    int cooldown;

    @Override
    public void init() {
        damage = 0;
        cooldown = 0;
        super.init();
    }

    public void attack(Vector2f direction) {
        // TODO implement attack

    }
}
