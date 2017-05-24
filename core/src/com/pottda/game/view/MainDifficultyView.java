package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;


public class MainDifficultyView {

    private final Stage mainDifficultyStage;

    private Image easyImage;
    private Image hardImage;

    private enum imageEnum {
        CHOOSEDIFFIMAGE,
        EASYIMAGE,
        HARDIMAGE,
        BGIMAGE
    }

    public MainDifficultyView(final Stage mainDifficultyStage) {
        this.mainDifficultyStage = mainDifficultyStage;
        create();
    }

    private void create() {
        // Add background
        addToStage(mainDifficultyStage.getWidth(),
                mainDifficultyStage.getHeight());

        addToStage(Sprites.CHOOSETITLE,
                imageEnum.CHOOSEDIFFIMAGE,
                mainDifficultyStage.getWidth() / 2 - 256,
                mainDifficultyStage.getHeight() * 3 / 4);

        addToStage(Sprites.CHOOSEEASY,
                imageEnum.EASYIMAGE,
                mainDifficultyStage.getWidth() / 2 - 256,
                mainDifficultyStage.getHeight() * 2 / 4);

        addToStage(Sprites.CHOOSEHARD,
                imageEnum.HARDIMAGE,
                mainDifficultyStage.getWidth() / 2 - 256,
                mainDifficultyStage.getHeight() * 1 / 4);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        mainDifficultyStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.MAINMENUBG.fileName)), imageEnum.BGIMAGE);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        mainDifficultyStage.addActor(image2);
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
        mainDifficultyStage.draw();
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
