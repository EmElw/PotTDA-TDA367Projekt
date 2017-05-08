package com.pottda.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;

import javax.vecmath.Vector2f;

/**
 *
 */
public class ViewActor extends Actor {

    /**
     * Sets the position of the actor
     *
     * @param xPosition The x position of the actor
     * @param yPosition The y position of the actor
     */
    public void setPoint(float xPosition, float yPosition) {
        this.setPosition(xPosition, yPosition);
    }

    /**
     * Sets the angle which the actor is looking
     *
     * @param degrees   The angle that the actor is looking
     */
    public void setAngle(float degrees) {
        this.setRotation(degrees);
    }

}
