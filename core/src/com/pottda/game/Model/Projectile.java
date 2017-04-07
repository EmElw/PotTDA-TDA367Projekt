package com.pottda.game.Model;

import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public class Projectile extends Actor {
    int damage;
    List<ProjectileListener> projectileListeners;
    List<Character> hasDamaged;

    @Override
    public void collide(Actor other) {

    }

    @Override
    public VectorType getMove() {
        return null;
    }
}
