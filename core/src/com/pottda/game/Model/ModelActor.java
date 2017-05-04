package com.pottda.game.model;

import com.pottda.game.controller.AbstractController;

import javax.vecmath.Vector2f;

/**
 * Created by Gustav Lahti on 2017-04-07.
 */

public abstract class ModelActor {
    public boolean isProjectile;
    public int team;
    public AbstractController controller;

    private PhysicsActor physicsActor;

    /**
     * Called by the controller once per frame, should be overridden
     *
     * @param movementVector a Vector2f containing the direction to move in
     * @param attackVector   a Vector2f containing the direction to attack in
     */
    public void giveInput(Vector2f movementVector, Vector2f attackVector) {
    }

    /**
     * Returns the position of the Actor
     *
     * @return a Vector2f
     */
    public Vector2f getPosition() {
        return physicsActor.getPosition();
    }

    /**
     * Called by the controller every frame to handle any collisions that have occurred
     */
    public void handleCollisions() {
    }

}
