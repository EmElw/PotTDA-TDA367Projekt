package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.math.Vector2;

import javax.vecmath.Vector2f;

public class Box2DPhysicsProjectile extends Box2DPhysicsActor{
    public final static float PROJECTILE_SPEED = 100;
    @Override
    public void giveMovementVector(Vector2f movementVector) {
        Vector2 movementImpulse = new Vector2(movementVector.x * PROJECTILE_SPEED, movementVector.y * PROJECTILE_SPEED);
        body.applyLinearImpulse(movementImpulse, body.getWorldCenter(), true);
    }
}
