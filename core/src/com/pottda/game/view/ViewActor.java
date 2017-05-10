package com.pottda.game.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 *
 */
public class ViewActor extends Image {

    /**
     * calls super class to set image for actor
     * @param texture the texture to set as image
     */
    public ViewActor(Texture texture) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        // set the rotation point to middle of image
        this.setOrigin(texture.getWidth() / 2, texture.getHeight() / 2);
    }

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
