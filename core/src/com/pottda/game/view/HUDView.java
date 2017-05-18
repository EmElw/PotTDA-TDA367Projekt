package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-03.
 */

public class HUDView {
    private static final String healthbarString = "hud/healthbar.png";
    private static final String healthbarRedString = "hud/health.png";
    private static final String pauseButtonString = "hud/pauseButton.png";

    private Texture texture;
    private final Stage stage;

    private Image healthbar;
    private Image healthbarRed;
    private Image pauseButton;

    public HUDView(final Stage stage) {
        this.stage = stage;
        create();
    }

    public void create() {
        // Add health bar
        addToStage(healthbarString, healthbar, 10, stage.getHeight() - 30);

        // Add health to health bar
        addToStage(healthbarRedString, healthbarRed, healthbar.getX(), stage.getHeight() - 30);

        // Add pause button
        addToStage(pauseButtonString, pauseButton, stage.getWidth() - 60, stage.getHeight() - 50);
    }

    public void render() {
        stage.draw();
    }

    private void addToStage(String texturePath, Image image, float xPos, float yPos) {
        image = new Image(new Texture(Gdx.files.internal(texturePath)));
        image.setX(xPos);
        image.setY(yPos);
        stage.addActor(image);
    }

    /**
     * Checks if the user touches the pause button while the game is running
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseButton(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseButton.getX(), pauseButton.getY(), pauseButton.getWidth(), pauseButton.getHeight());
        return tr.contains(vector3.x, vector3.y);

    }

    /**
     * Sets the players health bar to new health
     *
     * @param health new health. A value between 0 and 100
     */
    public void setHealthbar(float health) {
        healthbarRed.setWidth(health);
    }

    public void dispose() {
        texture.dispose();
    }
}