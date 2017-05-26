package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

public class PausedView {

    private final Stage pausedStage;

    private Image pauseResume;
    private Image pauseOptions;
    private Image pauseQuit;

    private enum imageEnum {
        BACKGROUND,
        RESUME,
        OPTIONS,
        QUIT
    }

    public PausedView(final Stage pausedStage) {
        this.pausedStage = pausedStage;
        create();
    }

    private void create() {
        // Add pause background
        addToStage(pausedStage.getWidth(),
                pausedStage.getHeight());

        addToStage(Sprites.PAUSERESUME,
                imageEnum.RESUME,
                pausedStage.getWidth() / 2 - 150,
                pausedStage.getHeight() - 150);

        addToStage(Sprites.PAUSEOPTIONS,
                imageEnum.OPTIONS,
                pausedStage.getWidth() / 2 - 150,
                pausedStage.getHeight() - 300);

        addToStage(Sprites.PAUSEQUIT,
                imageEnum.QUIT,
                pausedStage.getWidth() / 2 - 150,
                30);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        pausedStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.OPTIONSBG.fileName)), imageEnum.BACKGROUND);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        pausedStage.addActor(image2);
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
        pausedStage.draw();
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
