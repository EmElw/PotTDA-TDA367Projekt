package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Created by Rikard Teodorsson on 2017-05-03.
 */

public class HUDView {

    private Texture texture;
    private final Stage stage;

    private Image healthbar;
    private Image healthbarRed;
    private Image pauseButton;

    private enum imageEnum {
        HEALTHBAR,
        HEALTHBARRED,
        PAUSEBUTTON
    }

    public HUDView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add health bar
        addToStage(Sprites.HEALTHBAR, imageEnum.HEALTHBAR, 10, stage.getHeight() - 30);

        // Add health to health bar
        addToStage(Sprites.HEALTHBARRED, imageEnum.HEALTHBARRED, healthbar.getX(), stage.getHeight() - 30);

        // Add pause button
        addToStage(Sprites.PAUSEBUTTON, imageEnum.PAUSEBUTTON, stage.getWidth() - 60, stage.getHeight() - 50);
    }

    public void render() {
        stage.draw();
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case HEALTHBAR:
                return healthbar = new Image(texturePath);
            case HEALTHBARRED:
                return healthbarRed = new Image(texturePath);
            case PAUSEBUTTON:
                return pauseButton = new Image(texturePath);
        }
        return null;
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
        if (health < 0) {
            health = 0;
        }
        healthbarRed.setWidth(health);
    }

    public float getHealth() {
        return healthbarRed.getWidth();
    }

    public void dispose() {stage.dispose();}

}