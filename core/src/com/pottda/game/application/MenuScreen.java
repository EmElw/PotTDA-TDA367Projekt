package com.pottda.game.application;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pottda.game.controller.ControllerOptions;
import com.pottda.game.view.MainControlsView;
import com.pottda.game.view.MainDifficultyView;
import com.pottda.game.view.MainMenuView;

import static com.pottda.game.application.GameState.MAIN_CHOOSE;
import static com.pottda.game.application.GameState.MAIN_CONTROLS;
import static com.pottda.game.application.GameState.MAIN_MENU;
import static com.pottda.game.application.GameState.RUNNING;
import static com.pottda.game.application.GameState.gameState;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.KEYBOARD_MOUSE;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.KEYBOARD_ONLY;
import static com.pottda.game.controller.ControllerOptions.ControllerMode.TOUCH_JOYSTICK;
import static com.pottda.game.controller.ControllerOptions.joystickStage;
import static com.pottda.game.model.Constants.HEIGHT;
import static com.pottda.game.model.Constants.WIDTH;

/**
 * Screen that acts as top level for menu-navigation
 */
class MenuScreen {
    private Stage mainMenuStage;
    private Stage mainControlsStage;
    private Stage mainDifficultyStage;

    private MainMenuView mainMenuView;
    private MainControlsView mainControlsView;
    private MainDifficultyView mainDifficultyView;

    private final GameScreen gameScreen;

    MenuScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        create();
    }

    public void create() {
        mainMenuStage = new Stage(new StretchViewport(WIDTH, HEIGHT));

        mainControlsStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        mainDifficultyStage = new Stage(new StretchViewport(WIDTH, HEIGHT));
        mainMenuView = new MainMenuView(mainMenuStage);
        mainDifficultyView = new MainDifficultyView(mainDifficultyStage);
        mainControlsView = new MainControlsView(mainControlsStage);
        Gdx.input.setInputProcessor(mainMenuStage);
        Box2D.init();
        gameState = MAIN_MENU;
    }

    public void render(float delta) {
        switch (gameState) {
            case MAIN_MENU:
                // Draw the main menu
                mainMenuView.render();
                break;
            case MAIN_CHOOSE:
                // Draw the choose difficulty menu
                mainDifficultyView.render();
                break;
            case MAIN_CONTROLS:
                // Draw the choose controller menu
                mainControlsView.render();
                break;
        }
        checkTouch();
    }

    void resize(int width, int height) {
        mainMenuStage.getViewport().update(width, height, false);
        mainControlsStage.getViewport().update(width, height, false);
        mainDifficultyStage.getViewport().update(width, height, false);
    }

    void dispose() {
        mainControlsStage.dispose();
        mainDifficultyStage.dispose();
        mainMenuStage.dispose();
    }

    private void checkTouch() {
        if (Gdx.input.justTouched()) {
            Vector3 vector3 = mainMenuStage.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameState) {
                case MAIN_MENU:
                    if (mainMenuView.checkIfTouchingStart(vector3)) {
                        gameState = MAIN_CONTROLS;
                    } else if (mainMenuView.checkIfTouchingQuit(vector3)) {
                        // Exit game
                        Gdx.app.exit();
                    }
                    break;
                case MAIN_CHOOSE:
                    if (mainDifficultyView.checkIfTouchingEasy(vector3)) {
                        // TODO Set easy mode
                        gameScreen.doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    } else if (mainDifficultyView.checkIfTouchingHard(vector3)) {
                        // TODO Set hard mode
                        gameScreen.doOnStartGame();
                        gameState = RUNNING;
                        Gdx.input.setInputProcessor(joystickStage);
                    }
                    break;
                case MAIN_CONTROLS:
                    if (mainControlsView.checkIfTouchingTouch(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = TOUCH_JOYSTICK;
                        ControllerOptions.joystickStage = gameScreen.getJoystickStage();
                    } else if (mainControlsView.checkIfTouchingKeyboardOnly(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_ONLY;
                    } else if (mainControlsView.checkIfTouchingKeyboardMouse(vector3)) {
                        gameState = MAIN_CHOOSE;
                        ControllerOptions.controllerSettings = KEYBOARD_MOUSE;
                    }
                    break;
            }
        }
    }
}
