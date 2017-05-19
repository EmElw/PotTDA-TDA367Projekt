package com.pottda.game.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-18.
 */

public class MainDifficultyView {

    private final Stage stage;

    private Image easyImage;
    private Image hardImage;

    private enum imageEnum {
        CHOOSEDIFFIMAGE,
        EASYIMAGE,
        HARDIMAGE,
        BGIMAGE
    }

    public MainDifficultyView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        addToStage(Sprites.MAINMENUBG, imageEnum.BGIMAGE, 0, 0, stage.getWidth(), stage.getHeight());

        // Add choose difficulty title
        addToStage(Sprites.CHOOSETITLE, imageEnum.CHOOSEDIFFIMAGE, stage.getWidth() / 2 - 256, stage.getHeight() * 3 / 4);

        // Add easy button
        addToStage(Sprites.CHOOSEEASY, imageEnum.EASYIMAGE, stage.getWidth() / 2 - 256, stage.getHeight() * 2 / 4);

        // Add hard button
        addToStage(Sprites.CHOOSEHARD, imageEnum.HARDIMAGE, stage.getWidth() / 2 - 256, stage.getHeight() * 1 / 4);
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
            case CHOOSEDIFFIMAGE:
                return new Image(texturePath);
            case EASYIMAGE:
                return easyImage = new Image(texturePath);
            case HARDIMAGE:
                return hardImage = new Image(texturePath);
            case BGIMAGE:
                return new Image(texturePath);
        }
        return null;
    }

    public void render() {
        stage.draw();
    }

    /**
     * Checks if the user touches the easy button when choosing difficulty
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingEasy(Vector3 vector3) {
        Rectangle tr = new Rectangle(easyImage.getX(), easyImage.getY(), easyImage.getWidth(), easyImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the hard button when choosing difficulty
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingHard(Vector3 vector3) {
        Rectangle tr = new Rectangle(hardImage.getX(), hardImage.getY(), hardImage.getWidth(), hardImage.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

}
