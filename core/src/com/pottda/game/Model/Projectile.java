package com.pottda.game.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public class Projectile extends ModelActor {
    int damage;
    List<ProjectileListener> projectileListeners;
    List<Character> hasDamaged;

    public Projectile(int damage, List<ProjectileListener> projectileListeners, List<Character> hasDamaged) {
        this.damage = damage;
        this.projectileListeners = projectileListeners;
        this.hasDamaged = hasDamaged;
    }

    public Projectile() {
        damage = 0;
        projectileListeners = new ArrayList<ProjectileListener>();
        hasDamaged = new ArrayList<Character>();
    }

    /*@Override
    public VectorType getMove() {
        return null;
    }*/
}
