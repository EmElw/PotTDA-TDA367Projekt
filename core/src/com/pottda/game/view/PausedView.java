package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class PausedView {

    private final Stage stage;

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
        addToStage(Sprites.OPTIONSBG, imageEnum.BACKGROUND, 0, 0, stage.getWidth(), stage.getHeight());

        // Add pause resume button
        addToStage(Sprites.PAUSERESUME, imageEnum.RESUME, stage.getWidth() / 2 - 150, stage.getHeight() - 150);

        // Add pause options button
        addToStage(Sprites.PAUSEOPTIONS, imageEnum.OPTIONS, stage.getWidth() / 2 - 150, stage.getHeight() - 300);

        // Add pause quit button
        addToStage(Sprites.PAUSEQUIT, imageEnum.QUIT, stage.getWidth() / 2 - 150, 30);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos, float width, float height) {
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        image2.setX(xPos);
        image2.setY(yPos);
        image2.setWidth(width);
        image2.setHeight(height);
        stage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case BACKGROUND:
                return new Image(texturePath);
            case RESUME:
                return pauseResume = new Image(texturePath);
            case OPTIONS:
                return pauseOptions = new Image(texturePath);
            case QUIT:
                return pauseQuit = new Image(texturePath);
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
