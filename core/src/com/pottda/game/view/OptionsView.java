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

public class OptionsView {
    private static final String optionsMusicBarString = "hud/optionsVolumeBar.png";
    private static final String optionsMusicString = "hud/optionsVolume.png";
    private static final String optionsSFXBarString = "hud/optionsVolumeBar.png";
    private static final String optionsSFXString = "hud/optionsVolume.png";
    private static final String optionsSFXTextString = "hud/text/sfx.png";
    private static final String optionsMusicTextString = "hud/text/music.png";
    private static final String optionReturnString = "hud/optionsReturn.png";

    private final Stage stage;

    private Image optionsMusicBar;
    private Image optionsMusic;
    private Image optionsSFXBar;
    private Image optionsSFX;
    private Image optionsSFXText;
    private Image optionsMusicText;
    private Image optionReturn;

    public OptionsView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add options return button
        addToStage(optionReturnString, optionReturn, stage.getWidth() / 2 - 150, 30);

        // Add options music control
        addToStage(optionsMusicString, optionsMusic, stage.getWidth() / 2 - 150, (stage.getHeight() / 5) * 4);

        // Add options music bar
        addToStage(optionsMusicBarString, optionsMusicBar, stage.getWidth() / 2 - 150, (stage.getHeight() / 5) * 4);

        // Add options sfx control
        addToStage(optionsSFXString, optionsSFX, stage.getWidth() / 2 - 150, (stage.getHeight() / 5) * 3);

        // Add options sfx bar
        addToStage(optionsSFXBarString, optionsSFXBar, stage.getWidth() / 2 - 150, (stage.getHeight() / 5) * 3);

        // Add options sfx text
        addToStage(optionsSFXTextString, optionsSFXText, optionsSFXBar.getX() - 168, (stage.getHeight() / 5) * 3);

        // Add options music text
        addToStage(optionsMusicTextString, optionsMusicText, optionsMusicBar.getX() - 168, (stage.getHeight() / 5) * 4);
    }

    private void addToStage(String texturePath, Image image, float xPos, float yPos) {
        image = new Image(new Texture(Gdx.files.internal(texturePath)));
        image.setX(xPos);
        image.setY(yPos);
        stage.addActor(image);
    }

    public void render() {
        stage.draw();
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
