package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import javax.vecmath.Vector2f;

class Box2DPhysicsProjectile extends Box2DPhysicsActor {
    private final static float PROJECTILE_SPEED = 1;

    Box2DPhysicsProjectile(Body body) {
        super(body);
    }

    @Override
    public void giveMovementVector(Vector2f movementVector) {
        Vector2 movementImpulse = new Vector2(movementVector.x * PROJECTILE_SPEED, movementVector.y * PROJECTILE_SPEED);
//        body.applyLinearImpulse(movementImpulse, body.getWorldCenter(), true);
        body.applyForceToCenter(movementImpulse,true);
    }
}
