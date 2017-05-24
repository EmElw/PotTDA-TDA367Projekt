package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Draws the background
 */
public class BackgroundView {

    private final Stage backgroundStage;

    private static final float extraSize = 30f;

    public BackgroundView(final Stage backgroundStage) {
        this.backgroundStage = backgroundStage;
        create();
    }

    private void create() {
        addToStage(backgroundStage.getWidth(), backgroundStage.getHeight());
    }

    /**
     * Renders the background
     * @param camera the camera that is centered on the player. Creates a moving background effect
     */
    public void render(Camera camera) {
        backgroundStage.getCamera().position.set(new Vector2(camera.position.x / 2, camera.position.y / 2),0);
        backgroundStage.draw();
    }

    /**
     * Add the background to the backgroundStage
     * @param width the width of the playing area
     * @param height the height of the playing area
     */
    private void addToStage(float width, float height) {
        Image image2 = new Image(new Texture(Gdx.files.internal(Sprites.MAINBACKGROUND.fileName)));

        // Add extraSize in every direction to cover the whole visible background
        image2.setX(-extraSize);
        image2.setY(-extraSize);
        image2.setWidth(width + extraSize);
        image2.setHeight(height + extraSize);

        backgroundStage.addActor(image2);
    }

}
