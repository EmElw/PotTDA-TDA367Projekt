package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pottda.game.model.Constants;
import com.pottda.game.assets.Sprites;

import javax.vecmath.Vector2f;

public class ActorView extends Image {
    private final Texture texture;
    private Vector2f size;

    /**
     * calls super class to set image for actor
     *
     * @param texture the texture to set as image
     */
    public ActorView(Texture texture) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.texture = texture;
        // set the rotation point to middle of image
        this.setOrigin((texture.getWidth() * Constants.WIDTH_RATIO) / 2, (texture.getHeight() * Constants.HEIGHT_RATIO) / 2);
        this.setSize(this.getWidth() * Constants.WIDTH_RATIO, this.getHeight() * Constants.HEIGHT_RATIO); // Resize to make in meters instead of pixels
    }

    /**
     * calls super class to set image for actor
     *
     * @param texture the texture to set as image
     * @param size    vector with width and height of image
     */
    public ActorView(Texture texture, Vector2f size) {
        super(new TextureRegionDrawable(new TextureRegion(texture)));
        this.size = size;
        this.texture = texture;
        this.setOrigin(-(texture.getWidth() * Constants.WIDTH_RATIO) / 2, -(texture.getHeight() * Constants.HEIGHT_RATIO) / 2);
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
    public void setPoint(float xPosition, float yPosition, boolean isObstacle) {
        if (!isObstacle) {
            this.setPosition(xPosition - (texture.getWidth() * Constants.WIDTH_RATIO) / 2, yPosition - (texture.getHeight() * Constants.HEIGHT_RATIO) / 2);
        } else {
            this.setPosition(xPosition - size.x / 2, yPosition - size.y / 2);
        }
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
