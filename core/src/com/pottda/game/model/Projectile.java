package com.pottda.game.model;

import javax.vecmath.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikar on 2017-04-07.
 */

public class Projectile extends ModelActor {
    int damage;
    public List<ProjectileListener> projectileListeners;
    public List<Character> hasDamaged;

    public Projectile(PhysicsActor physicsActor, int damage, List<ProjectileListener> projectileListeners) {
        super(physicsActor);
        this.damage = damage;
        this.projectileListeners = projectileListeners;
        hasDamaged = new ArrayList<Character>();
    }

    /*@Override
    public VectorType getMove() {
        return null;
    }*/
    @Override
    public void giveInput(Vector2f movementVector, Vector2f attackVector) {
        physicsActor.giveMovementVector(movementVector);
        this.angle = (float)Math.toDegrees(Math.atan2(attackVector.getY(), attackVector.getX()));
    }
}
