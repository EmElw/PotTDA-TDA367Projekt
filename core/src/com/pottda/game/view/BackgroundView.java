package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Created by Rikard Teodorsson on 2017-05-22.
 */

/**
 * Draws the background
 */
public class BackgroundView {

    private final Stage stage;

    private static final float extraSize = 30f;

    public BackgroundView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        addToStage(stage.getWidth(), stage.getHeight());
    }

    /**
     * Renders the background
     * @param camera the camera that is centered on the player. Creates a moving background effect
     */
    public void render(Camera camera) {
        stage.getCamera().position.set(new Vector2(camera.position.x / 2, camera.position.y / 2),0);
        stage.draw();
    }

    /**
     * Add the background to the stage
     * @param width the width of the playing area
     * @param height the height of the playing area
     */
    private void addToStage(float width, float height) {
        Image image2 = new Image(new Texture(Gdx.files.internal(Sprites.MAINBACKGROUND.fileName)));
        image2.setX(-extraSize); // To make it cover the whole background
        image2.setY(-extraSize);
        image2.setWidth(width + extraSize);
        image2.setHeight(height + extraSize);
        stage.addActor(image2);
    }

}
