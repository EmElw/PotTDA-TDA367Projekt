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

    private final Stage stage;

    private Image title;
    private Image restartButton;
    private Image quitButton;
    private Image bg;

    private enum imageEnum {
        TITLE,
        RESTARTBUTTON,
        QUITBUTTON,
        BG
    }

    public GameOverView(final Stage stage) {
        this.stage = stage;
        create();
    }

    public void create() {
        // Add background
        addToStage(Sprites.MAINMENUBG, imageEnum.TITLE, 0, 0, stage.getWidth(), stage.getHeight());

        // Add title
        addToStage(Sprites.TITLE, imageEnum.TITLE, stage.getWidth() / 2 - 256, stage.getHeight() * 6 / 8);

        // Add restart button
        addToStage(Sprites.RESTARTBUTTON, imageEnum.RESTARTBUTTON, stage.getWidth() / 2 - 256, stage.getHeight() * 4 / 8);

        // Add quit button
        addToStage(Sprites.QUITBUTTON, imageEnum.QUITBUTTON, stage.getWidth() / 2 - 256, stage.getHeight() * 2 / 8);
    }

    public void render() {
        stage.draw();
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
            case BG:
                return bg = new Image(texturePath);
            case TITLE:
                return title = new Image(texturePath);
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
