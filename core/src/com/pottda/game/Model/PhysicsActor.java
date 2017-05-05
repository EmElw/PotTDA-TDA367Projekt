package com.pottda.game.model;

import javax.vecmath.Vector2f;


public abstract class PhysicsActor {

    /**
     * Returns the position of the physics body
     *
     * @return a Vector2f
     */
    public abstract Vector2f getPosition();

}