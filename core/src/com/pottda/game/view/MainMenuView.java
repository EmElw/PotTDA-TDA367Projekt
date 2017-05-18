package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-11.
 */

public class MainMenuView {
    private static final String mainMenuTitle = "menu/title.png";
    private static final String mainMenuStart = "menu/start.png";
    private static final String mainMenuQuit = "menu/quit.png";
    private static final String mainMenuBG = "menu/bg.png";

    private Texture texture;
    private final Stage stage;

    private Image titleImage;
    private Image startImage;
    private Image quitImage;
    private Image bgImage;

    public MainMenuView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        addToStage(mainMenuBG, bgImage, 0, 0, stage.getWidth(), stage.getHeight());

        // Add title
        addToStage(mainMenuTitle, titleImage, stage.getWidth() / 2 - texture.getWidth() / 2, stage.getHeight() * 3 / 4);

        // Add start button
        addToStage(mainMenuStart, startImage, stage.getWidth() * 1 / 4 - texture.getWidth() / 2, stage.getHeight() * 1 / 4);

        // Add quit button
        addToStage(mainMenuQuit, quitImage, stage.getWidth() * 3 / 4 - texture.getWidth() / 2, stage.getHeight() * 1 / 4);
    }

    private void addToStage(String texturePath, Image image, float xPos, float yPos) {
        image = new Image(new Texture(Gdx.files.internal(texturePath)));
        image.setX(xPos);
        image.setY(yPos);
        stage.addActor(image);
    }

    private void addToStage(String texturePath, Image image, float xPos, float yPos, float width, float height) {
        image = new Image(new Texture(Gdx.files.internal(texturePath)));
        image.setX(xPos);
        image.setY(yPos);
        image.setWidth(width);
        image.setHeight(height);
        stage.addActor(image);
    }

    public void render() {
        stage.draw();
    }

    public void dispose() {
        texture.dispose();
    }

    /**
     * Checks if the user touches the start button on first screen
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingStart(Vector3 vector3) {
        Rectangle tr = new Rectangle(startImage.getX(), startImage.getY(), startImage.getWidth(), startImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the quit button on first screen
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingQuit(Vector3 vector3) {
        Rectangle tr = new Rectangle(quitImage.getX(), quitImage.getY(), quitImage.getWidth(), quitImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

}
