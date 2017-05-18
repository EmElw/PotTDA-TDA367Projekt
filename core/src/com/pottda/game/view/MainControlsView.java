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

public class MainControlsView {
    private static final String controlsTitle = "menu/chooseControls.png";
    private static final String controlsTouch = "menu/touch.png";
    private static final String controlsKeyboardOnly = "menu/keyOnly.png";
    private static final String controlsKeyboardMouse = "menu/keyMouse.png";

    private final Stage stage;

    private Image controlsTitleImage;
    private Image controlsTouchImage;
    private Image controlsKeyOnlyImage;
    private Image controlsKeyMouseImage;

    private enum imageEnum {
        TITLE,
        TOUCH,
        KEYONLY,
        KEYMOUSE
    }

    public MainControlsView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add choose controls title
        addToStage(controlsTitle, imageEnum.TITLE, stage.getWidth() / 2 - 256, stage.getHeight() * 6.5f / 8);

        // Add touch button
        addToStage(controlsTouch, imageEnum.TOUCH, stage.getWidth() / 2 - 256, stage.getHeight() * 3 / 8);

        // Add keyboard only button
        addToStage(controlsKeyboardOnly, imageEnum.KEYONLY, stage.getWidth() / 2 - 256, stage.getHeight() * 5 / 8);

        // Add keyboard mouse button
        addToStage(controlsKeyboardMouse, imageEnum.KEYMOUSE, stage.getWidth() / 2 - 256, stage.getHeight() * 1 / 8);
    }

    private void addToStage(String texturePath, imageEnum image, float xPos, float yPos) {
        Image image2 = getImage(texturePath, image);
        image2.setX(xPos);
        image2.setY(yPos);
        stage.addActor(image2);
    }

    private Image getImage(String texturePath, imageEnum image) {
        switch (image) {
            case TITLE:
                return controlsTitleImage = new Image(new Texture(Gdx.files.internal(texturePath)));
            case TOUCH:
                return controlsTouchImage = new Image(new Texture(Gdx.files.internal(texturePath)));
            case KEYONLY:
                return controlsKeyOnlyImage = new Image(new Texture(Gdx.files.internal(texturePath)));
            case KEYMOUSE:
                return controlsKeyMouseImage = new Image(new Texture(Gdx.files.internal(texturePath)));
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