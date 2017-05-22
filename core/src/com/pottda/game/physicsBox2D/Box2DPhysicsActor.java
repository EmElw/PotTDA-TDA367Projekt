package com.pottda.game.physicsBox2D;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.pottda.game.model.PhysicsActor;

import javax.vecmath.Vector2f;

public class Box2DPhysicsActor extends PhysicsActor {
    Body body;

    public Box2DPhysicsActor(Body body) {
        this.body = body;
    }

    @Override
    public Vector2f getPosition() {
        if (body != null) {
            Vector2 bodyPos = body.getPosition();
            return new Vector2f(bodyPos.x, bodyPos.y);
        } else {
            return null;
        }
    }

    /**
     * Should be overridden by bodies that can change their movement vector
     *
     * @param movementVector
     */
    @Override
    public void giveMovementVector(Vector2f movementVector) {
    }

    @Override
    public void destroyBody() {
        if (body != null) {
            body.setUserData(null);
            body.getWorld().destroyBody(body);
            body = null;
        }
    }

    @Override
    public void setPosition(Vector2f position) {
        body.setTransform(position.x, position.y, 0);
    }
}
