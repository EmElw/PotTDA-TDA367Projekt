package com.pottda.game.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public class Projectile extends ModelActor {
    int damage;
    List<ProjectileListener> projectileListeners;
    List<Character> hasDamaged;

    public Projectile(PhysicsActor physicsActor) {
        super(physicsActor);
        damage = 0;
        projectileListeners = new ArrayList<ProjectileListener>();
        hasDamaged = new ArrayList<Character>();
    }

    /*@Override
    public VectorType getMove() {
        return null;
    }*/
}
