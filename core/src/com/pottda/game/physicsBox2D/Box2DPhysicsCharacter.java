package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import javax.vecmath.Vector2f;

class Box2DPhysicsCharacter extends Box2DPhysicsActor {

    Box2DPhysicsCharacter(Body body) {
        super(body);
    }

    @Override
    public void giveMovementVector(Vector2f movementVector) {
        Vector2 movementForce = new Vector2(movementVector.x, movementVector.y);
        body.applyForceToCenter(movementForce, true);
    }

}
