package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class PausedView {
    private static final String pauseBackgroundString = "hud/pauseBackground.png";
    private static final String pauseResumeString = "hud/pauseResume.png";
    private static final String pauseOptionsString = "hud/pauseOptions.png";
    private static final String pauseQuitString = "hud/pauseQuit.png";

    private final Stage stage;

    private Image pauseBackground;
    private Image pauseResume;
    private Image pauseOptions;
    private Image pauseQuit;

    private enum imageEnum {
        BACKGROUND,
        RESUME,
        OPTIONS,
        QUIT
    }

    public PausedView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add pause background
        addToStage(pauseBackgroundString, imageEnum.BACKGROUND, 0, 0, stage.getWidth(), stage.getHeight());

        // Add pause resume button
        addToStage(pauseResumeString, imageEnum.RESUME, stage.getWidth() / 2 - 150, stage.getHeight() - 150);

        // Add pause options button
        addToStage(pauseOptionsString, imageEnum.OPTIONS, stage.getWidth() / 2 - 150, stage.getHeight() - 300);

        // Add pause quit button
        addToStage(pauseQuitString, imageEnum.QUIT, stage.getWidth() / 2 - 150, 30);
    }

    private void addToStage(String texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(texturePath, image);
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private void addToStage(String texturePath, imageEnum image, float xPos, float yPos, float width, float height) {
        Image image2 = getImage(texturePath, image);
        image2.setX(xPos);
        image2.setY(yPos);
        image2.setWidth(width);
        image2.setHeight(height);
        stage.addActor(image2);
    }

    private Image getImage(String texturePath, imageEnum image) {
        switch (image) {
            case BACKGROUND:
                return pauseBackground = new Image(new Texture(Gdx.files.internal(texturePath)));
            case RESUME:
                return pauseResume = new Image(new Texture(Gdx.files.internal(texturePath)));
            case OPTIONS:
                return pauseOptions = new Image(new Texture(Gdx.files.internal(texturePath)));
            case QUIT:
                return pauseQuit = new Image(new Texture(Gdx.files.internal(texturePath)));
        }
        return null;
    }

    public void render() {
        stage.draw();
    }

    /**
     * Checks if the user touches the resume button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseResume(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseResume.getX(), pauseResume.getY(), pauseResume.getWidth(), pauseResume.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the options button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseOptions(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseOptions.getX(), pauseOptions.getY(), pauseOptions.getWidth(), pauseOptions.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the quit button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseQuit(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseQuit.getX(), pauseQuit.getY(), pauseQuit.getWidth(), pauseQuit.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

}
