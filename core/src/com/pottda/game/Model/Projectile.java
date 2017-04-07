package com.pottda.game.Model;

import java.util.List;
import java.util.Vector;

/**
 * Created by rikar on 2017-04-07.
 */

public class Projectile extends Actor {
    int damage;
    List<ProjectileListener> projectileListeners;
    List<Character> hasDamaged;

    @Override
    public Vector getMove() {
        return null;
    }
}
