package com.pottda.game.model;

import com.pottda.game.assets.Sprites;

import javax.vecmath.Vector2f;


/**
 * Logical representation of an actor in the game
 */

public abstract class ModelActor {
    private boolean shouldBeRemoved = false;
    boolean isProjectile;
    private int team;
    //    public AbstractController controller;
    float angle = 0;

    private com.pottda.game.assets.Sprites sprite;

    public final static int PLAYER_TEAM = 0;
    public final static int ENEMY_TEAM = 1;

    PhysicsActor physicsActor;
    private Behaviour behaviour;

    ModelActor() {

    }

    public Behaviour getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(Behaviour behaviour) {
        this.behaviour = behaviour;
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

    public void setPhysicsActor(PhysicsActor physicsActor) {
        this.physicsActor = physicsActor;
    }

    public PhysicsActor getPhysicsActor() {
        return physicsActor;
    }

    public Sprites getSprite() {
        return sprite;
    }

    public void setSprite(Sprites sprite) {
        this.sprite = sprite;
    }

    public boolean isShouldBeRemoved() {
        return shouldBeRemoved;
    }

    public void setShouldBeRemoved(boolean shouldBeRemoved) {
        this.shouldBeRemoved = shouldBeRemoved;
    }

    public enum Behaviour {
        NONE,
        DUMB,
        STATIONARY
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
