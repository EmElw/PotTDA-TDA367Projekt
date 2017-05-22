package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

/**
 * Created by Rikard Teodorsson on 2017-05-11.
 */

public class MainMenuView {

    private final Stage stage;

    private Image startImage;
    private Image quitImage;

    private enum imageEnum {
        BGIMAGE,
        TITLEIMAGE,
        STARTIMAGE,
        QUITIMAGE
    }

    public MainMenuView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        addToStage(stage.getWidth(), stage.getHeight());

        // Add title
        addToStage(Sprites.MAINMENUTITLE, imageEnum.TITLEIMAGE, stage.getWidth() / 2 - 512 / 2, stage.getHeight() * 3 / 4);

        // Add start button
        addToStage(Sprites.MAINMENUSTART, imageEnum.STARTIMAGE, stage.getWidth() * 1 / 4 - 128 / 2, stage.getHeight() * 1 / 4);

        // Add quit button
        addToStage(Sprites.MAINMENUQUIT, imageEnum.QUITIMAGE, stage.getWidth() * 3 / 4 - 128 / 2, stage.getHeight() * 1 / 4);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.MAINMENUBG.fileName)), imageEnum.BGIMAGE);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        stage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case BGIMAGE:
                return new Image(texturePath);
            case TITLEIMAGE:
                return new Image(texturePath);
            case STARTIMAGE:
                return startImage = new Image(texturePath);
            case QUITIMAGE:
                return quitImage = new Image(texturePath);
        }
        return null;
    }

    public void render() {
        stage.draw();
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
