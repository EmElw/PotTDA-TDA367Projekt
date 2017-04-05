package com.pottda.game;

import com.badlogic.gdx.math.Vector2;

/**
 * An interface for objects that rely on an AbstractController to decide what to do
 */
public interface Controllable {

    /**
     * An interface method for controlling the movement of an object
     * @param movementVector a Vector2, where the direction of the vector is the desired direction and the size is
     *                       a factor between 0 and 1 for how much it wants to move.
     */
    void move(Vector2 movementVector);

    /**
     * An interface method for attacking with an object of some sort
     *
     * @param direction the direction the object desires to attack in, expressed in radians
     */
    void attack(float direction);

    /**
     * @return the position of the object given as a Vector2
     */
    Vector2 getPosition();
}
