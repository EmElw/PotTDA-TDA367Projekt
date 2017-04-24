package com.pottda.game.Model;

import javax.vecmath.Vector2f;

/**
 * Interface for Objects that wishes to listen to a PhysicsActor
 */
public interface PhysicsListener {

    /**
     * Called from a PhysicsActor if its associated body is updated
     *
     * @param position
     */
    void onNewPosition(Vector2f position);


    // TODO sketchy to use this signature, but perhaps the right choice
    /**
     *
     * @param other
     */
    void onCollision(Object other);
}
