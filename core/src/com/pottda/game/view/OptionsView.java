package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.pottda.game.model.Sprites;

public class OptionsView {

    private final Stage optionsStage;

    private Image optionsMusicBar;
    private Image optionsMusic;
    private Image optionsSFXBar;
    private Image optionsSFX;
    private Image optionReturn;

    private enum imageEnum {
        BACKGROUND,
        MUSICBAR,
        MUSIC,
        SFXBAR,
        SFX,
        SFXTEXT,
        MUSICTEXT,
        RETURN
    }

    public OptionsView(final Stage optionsStage) {
        this.optionsStage = optionsStage;
        create();
    }

    private void create() {
        // Add pause background
        addToStage(optionsStage.getWidth(),
                optionsStage.getHeight());

        addToStage(Sprites.OPTIONSRETURN,
                imageEnum.RETURN,
                optionsStage.getWidth() / 2 - 150,
                30);

        addToStage(Sprites.OPTIONSMUSIC,
                imageEnum.MUSIC,
                optionsStage.getWidth() / 2 - 150,
                (optionsStage.getHeight() / 5) * 4);

        addToStage(Sprites.OPTIONSMUSICBAR,
                imageEnum.MUSICBAR,
                optionsStage.getWidth() / 2 - 150,
                (optionsStage.getHeight() / 5) * 4);

        addToStage(Sprites.OPTIONSSFX,
                imageEnum.SFX,
                optionsStage.getWidth() / 2 - 150,
                (optionsStage.getHeight() / 5) * 3);

        addToStage(Sprites.OPTIONSSFXBAR,
                imageEnum.SFXBAR,
                optionsStage.getWidth() / 2 - 150,
                (optionsStage.getHeight() / 5) * 3);

        addToStage(Sprites.OPTIONSSFXTEXT,
                imageEnum.SFXTEXT,
                optionsSFXBar.getX() - 168,
                (optionsStage.getHeight() / 5) * 3);

        addToStage(Sprites.OPTIONSMUSICTEXT,
                imageEnum.MUSICTEXT,
                optionsMusicBar.getX() - 168,
                (optionsStage.getHeight() / 5) * 4);
    }

    private void addToStage(Sprites texturePath, imageEnum image, float xPos, float yPos) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(texturePath.fileName)), image);
        assert image2 != null;
        image2.setX(xPos);
        image2.setY(yPos);
        optionsStage.addActor(image2);
    }

    private void addToStage(float width, float height) {
        // TODO generalise views with an abstract class
        Image image2 = getImage(new Texture(Gdx.files.internal(Sprites.OPTIONSBG.fileName)), imageEnum.BACKGROUND);
        assert image2 != null;
        image2.setX(0f);
        image2.setY(0f);
        image2.setWidth(width);
        image2.setHeight(height);
        optionsStage.addActor(image2);
    }

    private Image getImage(Texture texturePath, imageEnum image) {
        switch (image) {
            case BACKGROUND:
                return new Image(texturePath);
            case RETURN:
                return optionReturn = new Image(texturePath);
            case MUSIC:
                return optionsMusic = new Image(texturePath);
            case MUSICBAR:
                return optionsMusicBar = new Image(texturePath);
            case SFX:
                return optionsSFX = new Image(texturePath);
            case SFXBAR:
                return optionsSFXBar = new Image(texturePath);
            case SFXTEXT:
                return new Image(texturePath);
            case MUSICTEXT:
                return new Image(texturePath);
        }
        return null;
    }

    public void render() {
        optionsStage.draw();
    }

    /**
     * Checks if the user touches the return button in options
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingOptionsReturn(Vector3 vector3) {
        Rectangle tr = new Rectangle(optionReturn.getX(), optionReturn.getY(), optionReturn.getWidth(), optionReturn.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the music volume
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingOptionsMusic(Vector3 vector3) {
        Rectangle tr = new Rectangle(optionsMusicBar.getX(), optionsMusicBar.getY(), optionsMusicBar.getWidth(), optionsMusicBar.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the SFX volume
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingOptionsSFX(Vector3 vector3) {
        Rectangle tr = new Rectangle(optionsSFXBar.getX(), optionsSFXBar.getY(), optionsSFXBar.getWidth(), optionsSFXBar.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * returns the new volume after touching image
     *
     * @param vector3 Vector with touch coordinates to check
     * @return a float value between 0 and 1 with the new music volume
     */
    public float getNewMusicVolume(Vector3 vector3) {
        final float width = vector3.x - optionsMusicBar.getX();
        setOptionsMusicWidth(width);
        return ((vector3.x - optionsMusicBar.getX()) / optionsMusicBar.getWidth());
    }

    /**
     * returns the new volume after touching image
     *
     * @param vector3 Vector with touch coordinates to check
     * @return a float value between 0 and 1 with the new SFX volume
     */
    public float getNewSFXVolume(Vector3 vector3) {
        final float width = vector3.x - optionsSFXBar.getX();
        setOptionsSFXWidth(width);
        return ((vector3.x - optionsSFXBar.getX()) / optionsSFXBar.getWidth());
    }

    /**
     * Sets the width of the music volume image
     *
     * @param width new width of the music image
     */
    private void setOptionsMusicWidth(float width) {
        optionsMusic.setWidth(width);
    }

    /**
     * Sets the width of the SFX volume image
     *
     * @param width new width of the SFX image
     */
    private void setOptionsSFXWidth(float width) {
        optionsSFX.setWidth(width);
    }

}
