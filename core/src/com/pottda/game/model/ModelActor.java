package com.pottda.game.model;

import javax.vecmath.Vector2f;


/**
 * Logical representation of an actor in the game
 */

public abstract class ModelActor {
    public boolean shouldBeRemoved = false;
    public boolean isProjectile;
    public int team;
    //    public AbstractController controller;
    float angle = 0;

    public final static int PLAYER_TEAM = 0;
    public final static int ENEMY_TEAM = 1;

    protected final PhysicsActor physicsActor;

    public ModelActor(PhysicsActor physicsActor) {
        this.physicsActor = physicsActor;
    }

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
     * Returns the angle of which the Actor is looking
     *
     * @return degrees    a float containing the angle the actor is looking
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Called by the controller every frame to handle any collisions that have occurred
     */
    public void handleCollisions() {
    }

    public PhysicsActor getPhysicsActor(){
        return physicsActor;
    }

}
