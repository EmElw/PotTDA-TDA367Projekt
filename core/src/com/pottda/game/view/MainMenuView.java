package com.pottda.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by Rikard Teodorsson on 2017-05-11.
 */

public class MainMenuView {

    private Texture texture;
    private final Stage stage;

    private static final String mainMenuTitle = "menu/title.png";
    private static final String mainMenuStart = "menu/start.png";
    private static final String mainMenuQuit = "menu/quit.png";
    private static final String mainMenuBG = "menu/bg.png";
    private static final String chooseTitle = "menu/chooseDiff.png";
    private static final String chooseEasy = "menu/easy.png";
    private static final String chooseHard = "menu/hard.png";
    private static final String controlsTitle = "menu/chooseControls.png";
    private static final String controlsTouch = "menu/touch.png";
    private static final String controlsKeyboardOnly = "menu/keyOnly.png";
    private static final String controlsKeyboardMouse = "menu/keyMouse.png";

    private Image titleImage;
    private Image startImage;
    private Image quitImage;
    private Image bgImage;
    private Image chooseDiffImage;
    private Image easyImage;
    private Image hardImage;
    private Image controlsTitleImage;
    private Image controlsTouchImage;
    private Image controlsKeyOnlyImage;
    private Image controlsKeyMouseImage;

    public MainMenuView(final Stage stage) {
        this.stage = stage;
        create();
    }

    private void create() {
        // Add background
        texture = new Texture(Gdx.files.internal(mainMenuBG));
        bgImage = new Image(texture);
        bgImage.setX(0);
        bgImage.setY(0);
        bgImage.setWidth(stage.getWidth());
        bgImage.setHeight(stage.getHeight());
        stage.addActor(bgImage);

        // Add title
        texture = new Texture(Gdx.files.internal(mainMenuTitle));
        titleImage = new Image(texture);
        titleImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        titleImage.setY(stage.getHeight() * 3 / 4);
        stage.addActor(titleImage);

        // Add start button
        texture = new Texture(Gdx.files.internal(mainMenuStart));
        startImage = new Image(texture);
        startImage.setX(stage.getWidth() * 1 / 4 - texture.getWidth() / 2);
        startImage.setY(stage.getHeight() * 1 / 4);
        stage.addActor(startImage);

        // Add quit button
        texture = new Texture(Gdx.files.internal(mainMenuQuit));
        quitImage = new Image(texture);
        quitImage.setX(stage.getWidth() * 3 / 4 - texture.getWidth() / 2);
        quitImage.setY(stage.getHeight() * 1 / 4);
        stage.addActor(quitImage);

        // Add choose difficulty title
        texture = new Texture(Gdx.files.internal(chooseTitle));
        chooseDiffImage = new Image(texture);
        chooseDiffImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        chooseDiffImage.setY(stage.getHeight() * 3 / 4);
        stage.addActor(chooseDiffImage);

        // Add easy button
        texture = new Texture(Gdx.files.internal(chooseEasy));
        easyImage = new Image(texture);
        easyImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        easyImage.setY(stage.getHeight() * 2 / 4);
        stage.addActor(easyImage);

        // Add hard button
        texture = new Texture(Gdx.files.internal(chooseHard));
        hardImage = new Image(texture);
        hardImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        hardImage.setY(stage.getHeight() * 1 / 4);
        stage.addActor(hardImage);

        // Add choose controls title
        texture = new Texture(Gdx.files.internal(controlsTitle));
        controlsTitleImage = new Image(texture);
        controlsTitleImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        controlsTitleImage.setY(stage.getHeight() * 3 / 4);
        stage.addActor(controlsTitleImage);

        // Add touch button
        texture = new Texture(Gdx.files.internal(controlsTouch));
        controlsTouchImage = new Image(texture);
        controlsTouchImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        controlsTouchImage.setY(stage.getHeight() * 3 / 8);
        stage.addActor(controlsTouchImage);

        // Add keyboard only button
        texture = new Texture(Gdx.files.internal(controlsKeyboardOnly));
        controlsKeyOnlyImage = new Image(texture);
        controlsKeyOnlyImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        controlsKeyOnlyImage.setY(stage.getHeight() * 5 / 8);
        stage.addActor(controlsKeyOnlyImage);

        // Add keyboard mouse button
        texture = new Texture(Gdx.files.internal(controlsKeyboardMouse));
        controlsKeyMouseImage = new Image(texture);
        controlsKeyMouseImage.setX(stage.getWidth() / 2 - texture.getWidth() / 2);
        controlsKeyMouseImage.setY(stage.getHeight() * 1 / 8);
        stage.addActor(controlsKeyMouseImage);
    }

    public void renderMainMenu() {
        titleImage.setVisible(true);
        bgImage.setVisible(true);
        startImage.setVisible(true);
        quitImage.setVisible(true);
        chooseDiffImage.setVisible(false);
        easyImage.setVisible(false);
        hardImage.setVisible(false);
        controlsTitleImage.setVisible(false);
        controlsTouchImage.setVisible(false);
        controlsKeyOnlyImage.setVisible(false);
        controlsKeyMouseImage.setVisible(false);
        stage.draw();
    }

    public void renderChooseDiff() {
        titleImage.setVisible(false);
        bgImage.setVisible(true);
        startImage.setVisible(false);
        quitImage.setVisible(false);
        chooseDiffImage.setVisible(true);
        easyImage.setVisible(true);
        hardImage.setVisible(true);
        controlsTitleImage.setVisible(false);
        controlsTouchImage.setVisible(false);
        controlsKeyOnlyImage.setVisible(false);
        controlsKeyMouseImage.setVisible(false);
        stage.draw();
    }

    public void renderChooseControls() {
        titleImage.setVisible(false);
        bgImage.setVisible(true);
        startImage.setVisible(false);
        quitImage.setVisible(false);
        chooseDiffImage.setVisible(false);
        easyImage.setVisible(false);
        hardImage.setVisible(false);
        controlsTitleImage.setVisible(true);
        controlsTouchImage.setVisible(true);
        controlsKeyOnlyImage.setVisible(true);
        controlsKeyMouseImage.setVisible(true);
        stage.draw();
    }

    public void dispose() {
        texture.dispose();
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
