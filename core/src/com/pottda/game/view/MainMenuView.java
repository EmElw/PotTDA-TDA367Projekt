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

    private Image titleImage;
    private Image startImage;
    private Image quitImage;
    private Image bgImage;
    private Image chooseDiffImage;
    private Image easyImage;
    private Image hardImage;

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
    }

    public void renderMainMenu() {
        titleImage.setVisible(true);
        bgImage.setVisible(true);
        startImage.setVisible(true);
        quitImage.setVisible(true);
        chooseDiffImage.setVisible(false);
        easyImage.setVisible(false);
        hardImage.setVisible(false);
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

}
