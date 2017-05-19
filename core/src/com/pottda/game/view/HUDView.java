package com.pottda.game.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-03.
 */

public class HUDView extends ApplicationAdapter {
    private static final String healthbarString = "hud/healthbar.png";
    private static final String healthbarRedString = "hud/health.png";
    private static final String pauseButtonString = "hud/pauseButton.png";
    private static final String pauseBackgroundString = "hud/pauseBackground.png";
    private static final String pauseResumeString = "hud/pauseResume.png";
    private static final String pauseOptionsString = "hud/pauseOptions.png";
    private static final String pauseQuitString = "hud/pauseQuit.png";
    private static final String optionReturnString = "hud/optionsReturn.png";
    private static final String specialAttacksBarString = "hud/specialattacksbar.png";
    private static final String optionsMusicBarString = "hud/optionsVolumeBar.png";
    private static final String optionsMusicString = "hud/optionsVolume.png";
    private static final String optionsSFXBarString = "hud/optionsVolumeBar.png";
    private static final String optionsSFXString = "hud/optionsVolume.png";
    private static final String optionsSFXTextString = "hud/text/sfx.png";
    private static final String optionsMusicTextString = "hud/text/music.png";

    private Texture texture;
    private final Stage stage;

    private Image healthbar;
    private Image healthbarRed;
    private Image pauseButton;
    private Image pauseBackground;
    private Image pauseResume;
    private Image pauseOptions;
    private Image pauseQuit;
    private Image optionReturn;
    private Image specialAttacksBar;
    private Image optionsMusicBar;
    private Image optionsMusic;
    private Image optionsSFXBar;
    private Image optionsSFX;
    private Image optionsSFXText;
    private Image optionsMusicText;

    public HUDView(final Stage stage) {
        this.stage = stage;
        create();
    }

    @Override
    public void create() {
        // Add health bar
        texture = new Texture(Gdx.files.internal(healthbarString));
        healthbar = new Image(texture);
        healthbar.setX(10);
        healthbar.setY(stage.getHeight() - 30);
        stage.addActor(healthbar);

        // Add health to health bar
        texture = new Texture(Gdx.files.internal(healthbarRedString));
        healthbarRed = new Image(texture);
        healthbarRed.setX(healthbar.getX());
        healthbarRed.setY(stage.getHeight() - 30);
        stage.addActor(healthbarRed);

        // Add special attacks bar
        texture = new Texture(Gdx.files.internal(specialAttacksBarString));
        specialAttacksBar = new Image(texture);
        specialAttacksBar.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        specialAttacksBar.setY(30);
        specialAttacksBar.setColor(specialAttacksBar.getColor().r, specialAttacksBar.getColor().g, specialAttacksBar.getColor().b, (float) 0.6);
        stage.addActor(specialAttacksBar);

        // Add pause button
        texture = new Texture(Gdx.files.internal(pauseButtonString));
        pauseButton = new Image(texture);
        pauseButton.setX(stage.getWidth() - 60);
        pauseButton.setY(stage.getHeight() - 50);
        stage.addActor(pauseButton);

        // Add pause background
        texture = new Texture(Gdx.files.internal(pauseBackgroundString));
        pauseBackground = new Image(texture);
        pauseBackground.setX(0);
        pauseBackground.setY(0);
        pauseBackground.setWidth(stage.getWidth());
        pauseBackground.setHeight(stage.getHeight());
        pauseBackground.setVisible(false);
        stage.addActor(pauseBackground);

        // Add pause resume button
        texture = new Texture(Gdx.files.internal(pauseResumeString)); // 300x120px
        pauseResume = new Image(texture);
        pauseResume.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseResume.setY(stage.getHeight() - 150);
        pauseResume.setVisible(false);
        stage.addActor(pauseResume);

        // Add pause options button
        texture = new Texture(Gdx.files.internal(pauseOptionsString));
        pauseOptions = new Image(texture);
        pauseOptions.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseOptions.setY(stage.getHeight() - 300);
        pauseOptions.setVisible(false);
        stage.addActor(pauseOptions);

        // Add pause quit button
        texture = new Texture(Gdx.files.internal(pauseQuitString));
        pauseQuit = new Image(texture);
        pauseQuit.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        pauseQuit.setY(30);
        pauseQuit.setVisible(false);
        stage.addActor(pauseQuit);

        // Add options return button
        texture = new Texture(Gdx.files.internal(optionReturnString));
        optionReturn = new Image(texture);
        optionReturn.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        optionReturn.setY(30);
        optionReturn.setVisible(false);
        stage.addActor(optionReturn);

        // Add options music control
        texture = new Texture(Gdx.files.internal(optionsMusicString));
        optionsMusic = new Image(texture);
        optionsMusic.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        optionsMusic.setY((stage.getHeight() / 5) * 4);
        optionsMusic.setVisible(false);
        stage.addActor(optionsMusic);

        // Add options music bar
        texture = new Texture(Gdx.files.internal(optionsMusicBarString));
        optionsMusicBar = new Image(texture);
        optionsMusicBar.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        optionsMusicBar.setY((stage.getHeight() / 5) * 4);
        optionsMusicBar.setVisible(false);
        stage.addActor(optionsMusicBar);

        // Add options sfx control
        texture = new Texture(Gdx.files.internal(optionsSFXString));
        optionsSFX = new Image(texture);
        optionsSFX.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        optionsSFX.setY((stage.getHeight() / 5) * 3);
        optionsSFX.setVisible(false);
        stage.addActor(optionsSFX);

        // Add options sfx bar
        texture = new Texture(Gdx.files.internal(optionsSFXBarString));
        optionsSFXBar = new Image(texture);
        optionsSFXBar.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        optionsSFXBar.setY((stage.getHeight() / 5) * 3);
        optionsSFXBar.setVisible(false);
        stage.addActor(optionsSFXBar);

        // Add options sfx text
        texture = new Texture(Gdx.files.internal(optionsSFXTextString));
        optionsSFXText = new Image(texture);
        optionsSFXText.setX(optionsSFXBar.getX() - 168);
        optionsSFXText.setY((stage.getHeight() / 5) * 3);
        optionsSFXText.setVisible(false);
        stage.addActor(optionsSFXText);

        // Add options music text
        texture = new Texture(Gdx.files.internal(optionsMusicTextString));
        optionsMusicText = new Image(texture);
        optionsMusicText.setX(optionsMusicBar.getX() - 168);
        optionsMusicText.setY((stage.getHeight() / 5) * 4);
        optionsMusicText.setVisible(false);
        stage.addActor(optionsMusicText);
    }

    /**
     * Checks if the user touches the pause button while the game is running
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseButton(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseButton.getX(), pauseButton.getY(), pauseButton.getWidth(), pauseButton.getHeight());
        return tr.contains(vector3.x, vector3.y);

    }

    /**
     * Checks if the user touches the resume button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseResume(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseResume.getX(), pauseResume.getY(), pauseResume.getWidth(), pauseResume.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the options button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseOptions(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseOptions.getX(), pauseOptions.getY(), pauseOptions.getWidth(), pauseOptions.getHeight());
        return tr.contains(vector3.x, vector3.y);
    }

    /**
     * Checks if the user touches the quit button in the pause menu
     *
     * @param vector3 Vector with touch coordinates to check
     * @return true if touching button
     */
    public boolean checkIfTouchingPauseQuit(Vector3 vector3) {
        Rectangle tr = new Rectangle(pauseQuit.getX(), pauseQuit.getY(), pauseQuit.getWidth(), pauseQuit.getHeight());
        return tr.contains(vector3.x, vector3.y);
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

    /**
     *  Renders pause menu when paused
     */
    public void renderPaused() {
        pauseBackground.setVisible(true);
        pauseResume.setVisible(true);
        pauseOptions.setVisible(true);
        pauseQuit.setVisible(true);
        optionReturn.setVisible(false);
        optionsMusicBar.setVisible(false);
        optionsMusic.setVisible(false);
        optionsSFXBar.setVisible(false);
        optionsSFX.setVisible(false);
        optionsMusicText.setVisible(false);
        optionsSFXText.setVisible(false);
        specialAttacksBar.setVisible(false);
    }

    /**
     *  Renders options menu when in options
     */
    public void renderOptions() {
        pauseBackground.setVisible(true);
        pauseResume.setVisible(false);
        pauseOptions.setVisible(false);
        pauseQuit.setVisible(false);
        optionReturn.setVisible(true);
        optionsMusicBar.setVisible(true);
        optionsMusic.setVisible(true);
        optionsSFXBar.setVisible(true);
        optionsSFX.setVisible(true);
        optionsMusicText.setVisible(true);
        optionsSFXText.setVisible(true);
        specialAttacksBar.setVisible(false);
    }

    /**
     *  Renders the HUD when not paused
     */
    public void renderRunning() {
        pauseBackground.setVisible(false);
        pauseResume.setVisible(false);
        pauseOptions.setVisible(false);
        pauseQuit.setVisible(false);
        optionReturn.setVisible(false);
        optionsMusicBar.setVisible(false);
        optionsMusic.setVisible(false);
        optionsSFXBar.setVisible(false);
        optionsSFX.setVisible(false);
        optionsMusicText.setVisible(false);
        optionsSFXText.setVisible(false);
        // Hide special attacks for now
        specialAttacksBar.setVisible(false);
    }

    // text? https://gamedev.stackexchange.com/questions/65788/spritebatch-being-drawn-outside-of-stage

    /**
     * Sets the players health bar to new health
     *
     * @param health new health. A value between 0 and 100
     */
    public void setHealthbar(float health) {
        healthbarRed.setWidth(health);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}