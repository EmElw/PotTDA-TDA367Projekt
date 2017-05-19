package com.pottda.game.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class MainControlsView {

    private final Stage stage;

    private Image controlsTouchImage;
    private Image controlsKeyOnlyImage;
    private Image controlsKeyMouseImage;

    private enum imageEnum {
        TITLE,
        TOUCH,
        KEYONLY,
        KEYMOUSE,
        BGIMAGE
    }

    public MainControlsView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        addToStage(Sprites.MAINMENUBG, imageEnum.BGIMAGE, 0, 0, stage.getWidth(), stage.getHeight());

        // Add choose controls title
        addToStage(Sprites.CONTROLSTITLE, imageEnum.TITLE, stage.getWidth() / 2 - 256, stage.getHeight() * 6.5f / 8);

        // Add touch button
        addToStage(Sprites.CONTROLSTOUCH, imageEnum.TOUCH, stage.getWidth() / 2 - 256, stage.getHeight() * 3 / 8);

        // Add keyboard only button
        addToStage(Sprites.CONTROLSKEYBOARDONLY, imageEnum.KEYONLY, stage.getWidth() / 2 - 256, stage.getHeight() * 5 / 8);

        // Add keyboard mouse button
        addToStage(Sprites.CONTROLSKEYBOARDMOUSE, imageEnum.KEYMOUSE, stage.getWidth() / 2 - 256, stage.getHeight() * 1 / 8);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(texturePath.texture, image);
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos, float width, float height) {
        Image image2 = getImage(texturePath.texture, image);
        image2.setX(xPos);
        image2.setY(yPos);
        image2.setWidth(width);
        image2.setHeight(height);
        stage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case TITLE:
                return new Image(texturePath);
            case TOUCH:
                return controlsTouchImage = new Image(texturePath);
            case KEYONLY:
                return controlsKeyOnlyImage = new Image(texturePath);
            case KEYMOUSE:
                return controlsKeyMouseImage = new Image(texturePath);
            case BGIMAGE:
                return new Image(texturePath);
        }
        return null;
    }

    public void render() {
        stage.draw();
    }

    /**
     * Checks if the user touches the touch button when choosing controls
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingTouch(Vector3 vector3) {
        Rectangle tr = new Rectangle(controlsTouchImage.getX(), controlsTouchImage.getY(), controlsTouchImage.getWidth(), controlsTouchImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the keyboard only button when choosing controls
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingKeyboardOnly(Vector3 vector3) {
        Rectangle tr = new Rectangle(controlsKeyOnlyImage.getX(), controlsKeyOnlyImage.getY(), controlsKeyOnlyImage.getWidth(), controlsKeyOnlyImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the keyboard mouse button when choosing controls
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingKeyboardMouse(Vector3 vector3) {
        Rectangle tr = new Rectangle(controlsKeyMouseImage.getX(), controlsKeyMouseImage.getY(), controlsKeyMouseImage.getWidth(), controlsKeyMouseImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

}
