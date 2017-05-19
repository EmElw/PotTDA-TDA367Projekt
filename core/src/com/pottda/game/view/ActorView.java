package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pottda.game.PoTDA;
import com.pottda.game.model.Sprites;

import javax.vecmath.Vector2f;

/**
 *
 */
public class ActorView extends Image {

    /**
     * calls super class to set image for actor
     *
     * @param texture the texture to set as image
     */
    public ActorView(Texture texture) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        // set the rotation point to middle of image
        this.setOrigin((texture.getWidth() * PoTDA.WIDTH_RATIO) / 2, (texture.getHeight() * PoTDA.HEIGHT_RATIO) / 2);
        this.setSize(this.getWidth() * PoTDA.WIDTH_RATIO, this.getHeight() * PoTDA.HEIGHT_RATIO); // Resize to make in meters instead of pixels
    }

    /**
     * calls super class to set image for actor
     *
     * @param texture the texture to set as image
     * @param size    vector with width and height of image
     */
    public ActorView(Texture texture, Vector2f size) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.setSize(size.x, size.y);
    }

    public ActorView(Sprites sprite) {
        this(new Texture(Gdx.files.internal(sprite.fileName)));
    }

    public ActorView(Sprites sprite, Vector2f size) {
        this(new Texture(Gdx.files.internal(sprite.fileName)), size);
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
     * @param degrees The angle that the actor is looking
     */
    public void setAngle(float degrees) {
        this.setRotation(degrees);
    }


}
