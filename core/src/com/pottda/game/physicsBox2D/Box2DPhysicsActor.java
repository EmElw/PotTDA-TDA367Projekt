package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pottda.game.model.PhysicsActor;

import javax.vecmath.Vector2f;

public class Box2DPhysicsActor extends PhysicsActor {
    Body body;

    @Override
    public Vector2f getPosition() {
        Vector2 bodyPos = body.getPosition();
        return new Vector2f(bodyPos.x, bodyPos.y);
    }

    /**
     * Should be overridden by bodies that can change their movement vector
     * @param movementVector
     */
    @Override
    public void giveMovementVector(Vector2f movementVector) {
    }
}
