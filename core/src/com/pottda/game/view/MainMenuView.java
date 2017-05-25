package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

public class MainMenuView {

    private final Stage mainMenuStage;

    private Image startImage;
    private Image quitImage;

    private enum imageEnum {
        BGIMAGE,
        TITLEIMAGE,
        STARTIMAGE,
        QUITIMAGE
    }

    public MainMenuView(final Stage mainMenuStage) {
        this.mainMenuStage = mainMenuStage;
        create();
    }

    private void create() {
        // Add background
        addToStage(mainMenuStage.getWidth(),
                mainMenuStage.getHeight());

        addToStage(Sprites.MAINMENUTITLE,
                imageEnum.TITLEIMAGE,
                mainMenuStage.getWidth() / 2 - 512 / 2,
                mainMenuStage.getHeight() * 3 / 4);

        addToStage(Sprites.MAINMENUSTART,
                imageEnum.STARTIMAGE,
                mainMenuStage.getWidth() * 1 / 4 - 128 / 2,
                mainMenuStage.getHeight() * 1 / 4);

        addToStage(Sprites.MAINMENUQUIT,
                imageEnum.QUITIMAGE,
                mainMenuStage.getWidth() * 3 / 4 - 128 / 2,
                mainMenuStage.getHeight() * 1 / 4);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        mainMenuStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.MAINMENUBG.fileName)), imageEnum.BGIMAGE);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        mainMenuStage.addActor(image2);
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
        mainMenuStage.draw();
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
