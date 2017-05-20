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

    public Sprites sprite;

    public final static int PLAYER_TEAM = 0;
    public final static int ENEMY_TEAM = 1;

    protected PhysicsActor physicsActor;
    public Behaviour behaviour;

    public ModelActor() {

    }

    /**
     * Called by the controller once per frame, should be overridden
     *
     * @param movementVector a Vector2f containing the direction to move in
     * @param attackVector   a Vector2f containing the direction to attack in
     */
    public void giveInput(Vector2f movementVector, Vector2f attackVector) {

    }


    public void setPosition(Vector2f position) {
        physicsActor.setPosition(position);
    }
    public void setPosition(float x, float y) {
        physicsActor.setPosition(new Vector2f(x, y));
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

    public void setPhysicsActor(PhysicsActor physicsActor) {
        this.physicsActor = physicsActor;
    }

    public PhysicsActor getPhysicsActor() {
        return physicsActor;
    }

    public enum Behaviour {
        NONE,
        DUMB
    }
}
