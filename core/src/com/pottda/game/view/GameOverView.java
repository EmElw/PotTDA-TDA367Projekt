package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Created by Rikard on 2017-05-19.
 */

public class GameOverView {

    private final Stage gameOverStage;

    private Image restartButton;
    private Image quitButton;

    private enum imageEnum {
        TITLE,
        RESTARTBUTTON,
        QUITBUTTON,
        BG
    }

    public GameOverView(final Stage gameOverStage) {
        this.gameOverStage = gameOverStage;
        create();
    }

    private void create() {
        // Add background
        addToStage(gameOverStage.getWidth(),
                gameOverStage.getHeight());

        addToStage(Sprites.TITLE,
                imageEnum.TITLE,
                gameOverStage.getWidth() / 2 - 256,
                gameOverStage.getHeight() * 6 / 8);

        addToStage(Sprites.RESTARTBUTTON,
                imageEnum.RESTARTBUTTON,
                gameOverStage.getWidth() / 2 - 256,
                gameOverStage.getHeight() * 4 / 8);

        addToStage(Sprites.QUITBUTTON,
                imageEnum.QUITBUTTON,
                gameOverStage.getWidth() / 2 - 256,
                gameOverStage.getHeight() * 2 / 8);
    }

    public void render() {
        gameOverStage.draw();
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        gameOverStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.MAINMENUBG.fileName)), imageEnum.TITLE);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        gameOverStage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case BG:
                return new Image(texturePath);
            case TITLE:
                return new Image(texturePath);
            case QUITBUTTON:
                return quitButton = new Image(texturePath);
            case RESTARTBUTTON:
                return restartButton = new Image(texturePath);
        }
        return null;
    }

    /**
     * Checks if the user touches the pause button while the game is running
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingRestartButton(Vector3 vector3) {
        Rectangle tr = new Rectangle(restartButton.getX(), restartButton.getY(), restartButton.getWidth(), restartButton.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the quit button while the game is running
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingQuitButton(Vector3 vector3) {
        Rectangle tr = new Rectangle(quitButton.getX(), quitButton.getY(), quitButton.getWidth(), quitButton.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

}
