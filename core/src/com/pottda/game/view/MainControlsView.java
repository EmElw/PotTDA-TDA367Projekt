package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

public class MainControlsView {

    private final Stage mainControlsStage;

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

    public MainControlsView(final Stage mainControlsStage) {
        this.mainControlsStage = mainControlsStage;
        create();
    }

    private void create() {
        // Add background
        addToStage(mainControlsStage.getWidth(),
                mainControlsStage.getHeight());

        addToStage(Sprites.CONTROLSTITLE,
                imageEnum.TITLE,
                mainControlsStage.getWidth() / 2 - 256,
                mainControlsStage.getHeight() * 6.5f / 8);

        addToStage(Sprites.CONTROLSTOUCH,
                imageEnum.TOUCH,
                mainControlsStage.getWidth() / 2 - 256,
                mainControlsStage.getHeight() * 3 / 8);

        addToStage(Sprites.CONTROLSKEYBOARDONLY,
                imageEnum.KEYONLY,
                mainControlsStage.getWidth() / 2 - 256,
                mainControlsStage.getHeight() * 5 / 8);

        addToStage(Sprites.CONTROLSKEYBOARDMOUSE,
                imageEnum.KEYMOUSE,
                mainControlsStage.getWidth() / 2 - 256,
                mainControlsStage.getHeight() * 1 / 8);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        mainControlsStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.MAINMENUBG.fileName)), imageEnum.BGIMAGE);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        mainControlsStage.addActor(image2);
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
        mainControlsStage.draw();
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
